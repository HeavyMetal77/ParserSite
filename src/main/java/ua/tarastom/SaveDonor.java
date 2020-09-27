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

public class SaveDonor {
    public static final String urlDonorSite = "https://***.com.ua";
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
                WORKER_THREAD_POOL.shutdown();
                latch.countDown();
            });
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
            Elements elementsULClass = document.getElementsByAttributeValue("class", "b-nav__item");
            elementsULClass.forEach(element -> {
                Element aElement = element.child(0);
                String urlCategory = aElement.attr("href");
                String titileCategory = aElement.text();
                categoryList.add(new Category(urlDonorSite + urlCategory, titileCategory));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        categoryList.remove(0);
        categoryList.remove(categoryList.size() - 1);
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
                    String productMinQuantity = child.getElementsByAttribute("data-product-min-quantity").attr("data-product-min-quantity");
                    String productBigPicture = child.getElementsByAttribute("data-product-big-picture").attr("data-product-big-picture");
                    String productSmallPicture = child.getElementsByAttribute("data-product-small-picture").attr("data-product-small-picture");
                    String presenceData = child.getElementsByAttributeValue("data-qaid", "presence_data").text();

                    if (productName.equals("")) {
                        productName = child.getElementsByAttributeValue("class", "b-goods-title").text();
                        productUrl = child.getElementsByAttributeValue("class", "b-goods-title").attr("href");
                        priceProduct = child.getElementsByAttributeValue("class", "b-goods-price").text();
                        productBigPicture = child.getElementsByAttributeValue("class", "b-product-gallery__image img-ondemand").attr("longdesc");
                        productSmallPicture = child.getElementsByAttributeValue("class", "b-product-gallery__image img-ondemand").attr("content");
                    }
                    if (!productUrl.startsWith("http")) {
                        productUrl = urlDonorSite + productUrl;
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
