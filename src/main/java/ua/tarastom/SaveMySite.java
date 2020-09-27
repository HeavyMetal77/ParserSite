package ua.tarastom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SaveMySite {
    public static final String urlMySite = "https://***.com.ua/";
    public static final int POOL_SIZE = 4;
    public static volatile List<Product> ALL_PRODUCTS_LIST = new ArrayList<>();

    public static void getAllProduct(List<Category> listCategory) {
        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(POOL_SIZE);
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 1; i++) {
            WORKER_THREAD_POOL.submit(() -> {
                listCategory.forEach(category -> {
                    List<Product> listProductsInCategory = getListProductsInCategory(category.getUrl(), category.getName());
                    ALL_PRODUCTS_LIST.addAll(listProductsInCategory);
                });
            });
            WORKER_THREAD_POOL.shutdown();
            latch.countDown();
        }
        // wait for the latch to be decremented by the two remaining threads
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<Category> getCategoryList(String url) {
        List<Category> categoryList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements elementsULClass = document.getElementsByAttributeValue("class", "b-menu__sub-nav b-sub-menu");
            Element element1 = elementsULClass.get(0);
            for (int i = 0; i < element1.childNodeSize(); i++) {
                String titileCategory = element1.child(i).getElementsByAttribute("href").text();
                String urlCategory = element1.child(i).getElementsByAttribute("href").attr("href");
                categoryList.add(new Category(urlMySite + urlCategory, titileCategory));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public static List<Product> getListProductsInCategory(String url, String categoryName) {
        List<Product> productsList = new ArrayList<>();
        int quantityPage = 1; //кількість сторінок у категорії
        int nextPage = 1; //наступна сторінка

        try {
            do {
                String nextUrl;
                if (quantityPage != 1) {
                    nextUrl = url + "/page_" + nextPage;
                } else {
                    nextUrl = url;
                }
                Document document = Jsoup.connect(nextUrl).get();

                //вираховую кількість сторінок у категорії - тільки перший раз
                if (quantityPage == 1) {
                    Elements elementsPosLastPage = document.getElementsByAttributeValue("class", "b-pager");
                    if (!elementsPosLastPage.isEmpty()) {
                        String text = elementsPosLastPage.get(0).text();
                        String[] s = text.split(" ");
                        quantityPage = Integer.parseInt(s[s.length - 2]);
                    }
                }

                Elements elementsULClass = document.getElementsByAttributeValue("class", "b-product-gallery");
                Element element = elementsULClass.get(0);
                Elements children = element.children();
                children.forEach(child -> {
                    String articul = child.getElementsByAttributeValue("title", "Артикул: ").text();
                    int productId = Integer.parseInt(child.getElementsByAttribute("data-product-id").attr("data-product-id"));
                    String productName = child.getElementsByAttribute("data-product-name").attr("data-product-name");
                    String productUrl = child.getElementsByAttribute("data-product-url").attr("data-product-url");
                    String priceProduct = child.getElementsByAttribute("data-product-price").attr("data-product-price");
                    //data-price="5.00000" data-price-text="5 грн."
                    String productMinQuantity = child.getElementsByAttribute("data-quantity").attr("data-quantity");
                    String productBigPicture = child.getElementsByAttribute("data-product-big-picture").attr("data-product-big-picture");
                    String productSmallPicture = child.getElementsByAttribute("data-product-small-picture").attr("data-product-small-picture");
                    String presenceData = child.getElementsByAttributeValue("data-qaid", "presence_data").text();

                    if (productName.equals("")) {
                        productName = child.getElementsByAttributeValue("class", "b-product-gallery__title").text();
                        productUrl = child.getElementsByAttributeValue("class", "b-product-gallery__title").attr("href");
                        priceProduct = child.getElementsByAttributeValue("class", "b-product-gallery__btn b-custom-link js-one-click-order-member-site js-product-ad-conv-action").attr("data-price-text");
                        productBigPicture = child.getElementsByAttributeValue("class", "b-product-gallery__image img-ondemand").attr("longdesc");
                        productSmallPicture = child.getElementsByAttributeValue("class", "b-product-gallery__image img-ondemand").attr("content");
                    }
                    if (productName.equals("") || productUrl.equals("") || priceProduct.equals("") || productMinQuantity.equals("") || productBigPicture.equals("") || productSmallPicture.equals("") || presenceData.equals("")) {
                        productBigPicture = child.getElementsByAttributeValue("class", "b-product-gallery__image").attr("src");
                        productSmallPicture = child.getElementsByAttributeValue("class", "b-product-gallery__btn b-custom-link js-one-click-order-member-site js-product-ad-conv-action").attr("data-img-url");
                    }
                    if (!productUrl.startsWith("http")) {
                        productUrl = urlMySite + productUrl;
                    }

                    Product product = new Product(articul, productId, productUrl, productName, priceProduct,
                            productMinQuantity, productBigPicture, productSmallPicture, presenceData);
                    product.setCategory(categoryName);
                    productsList.add(product);
                });
            } while (quantityPage >= ++nextPage);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return productsList;
    }
}
