package ua.tarastom;

import java.io.*;
import java.util.List;

public class Comparison {
    private final List<Product> productListMySite;
    private final List<Product> productListDonorSite;
    private BufferedWriter bufferedWriter;

    public Comparison(List<Product> productListMySite, List<Product> productListDonorSite) {
        this.productListMySite = productListMySite;
        this.productListDonorSite = productListDonorSite;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("Analiz.txt"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readDataFromFile() {
        BufferedReader bufferedReaderFileMySite = null;
        BufferedReader bufferedReaderFileDonorSite;
        try {
            bufferedReaderFileMySite = new BufferedReader(new InputStreamReader(new FileInputStream("productListMySite.txt")));
            bufferedReaderFileDonorSite = new BufferedReader(new InputStreamReader(new FileInputStream("productListDonorSite.txt")));
            String line = bufferedReaderFileMySite.readLine();
            while (line != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDataToFile() {
        BufferedWriter bufferedWriterMySite = null;
        BufferedWriter bufferedWriterDonorSite = null;
        try {
            bufferedWriterMySite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("productListMySite.txt"))));
            bufferedWriterMySite.write(SaveMySite.ALL_PRODUCTS_LIST.toString());
            bufferedWriterMySite.write(SaveMySite.ALL_PRODUCTS_LIST.toString());
            bufferedWriterMySite.flush();

            bufferedWriterDonorSite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("productListDonorSite.txt"))));
            bufferedWriterDonorSite.write(SaveDonor.ALL_PRODUCTS_LIST.toString());
            bufferedWriterDonorSite.write(SaveDonor.ALL_PRODUCTS_LIST.toString());
            bufferedWriterDonorSite.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriterMySite != null) {
                    bufferedWriterMySite.close();
                }
                if (bufferedWriterDonorSite != null) {
                    bufferedWriterDonorSite.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void doCompareMyToDonor() throws IOException {
        bufferedWriter.write("\n\nНа нашому сайті є наступні позиції, яких не має на сайті донорі: ");
        bufferedWriter.flush();
        boolean flag = false;
        int i = 0;
        for (Product productMySite : productListMySite) {
            String productNameMySite = productMySite.getProductName();
            for (Product productDonorSite : productListDonorSite) {
                String donorSiteProductName = productDonorSite.getProductName();
                if (productNameMySite.equalsIgnoreCase(donorSiteProductName)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                bufferedWriter.write("\n" + ++i + " - " +  productMySite.getCategory() + ", " + productNameMySite + ", " + productMySite.getProductUrl());
                bufferedWriter.flush();
            }
            flag = false;
        }
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************\n");
        bufferedWriter.flush();
    }

    public void doCompareDonorToMy() throws IOException {
        bufferedWriter.write("\n\nНа сайті донорі є наступні позиції, яких не має на нашому сайті: ");
        bufferedWriter.flush();
        boolean flag = false;
        int i = 0;
        for (Product productDonorSite : productListDonorSite) {
            String donorSiteProductName = productDonorSite.getProductName();
            for (Product productMySite : productListMySite) {
                String productNameMySite = productMySite.getProductName();
                if (donorSiteProductName.equalsIgnoreCase(productNameMySite)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                bufferedWriter.write("\n" + ++i + " - " + productDonorSite.getCategory() + ", " + productDonorSite.getArticul() + ", " + productDonorSite.getProductName()
                        + ", " + productDonorSite.getProductUrl() + ", " + productDonorSite.getProductPresenceData() + ", " + productDonorSite.getProductPrice());
                bufferedWriter.flush();
            }
            flag = false;
        }
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************\n");
        bufferedWriter.flush();
    }

    public void doCompareMyToDonorByPresence() throws IOException {
        bufferedWriter.write("\n\nВсього на сайті : " + SaveMySite.urlMySite + " - " + SaveMySite.ALL_PRODUCTS_LIST.size() + " позицій");
        bufferedWriter.write("\n\nВсього на сайті : " + SaveDonor.urlDonorSite + " - " + SaveDonor.ALL_PRODUCTS_LIST.size() + " позицій");
        bufferedWriter.write("\n\nПо наявності не співпадають наступні позиції: ");
        bufferedWriter.flush();
        int i = 0;
        for (Product productMySite : productListMySite) {
            String productNameMySite = productMySite.getProductName();
            for (Product productDonorSite : productListDonorSite) {
                String donorSiteProductName = productDonorSite.getProductName();
                if (productNameMySite.equalsIgnoreCase(donorSiteProductName)) {
//                    if (productNameMySite.equalsIgnoreCase("БАЙКАЛ ЭМ-1 1 Л")) {
//                        System.out.println("+");
//                    }
                    String productPresenceDataMySite = productMySite.getProductPresenceData();
                    String productPresenceDataDonor = productDonorSite.getProductPresenceData();
                    if(productPresenceDataMySite.equalsIgnoreCase("Ожидается")){
                        productPresenceDataMySite = "Нет в наличии";
                    }
                    if(productPresenceDataDonor.equalsIgnoreCase("Ожидается")){
                        productPresenceDataDonor = "Нет в наличии";
                    }
                    if(productPresenceDataDonor.equalsIgnoreCase("Гарантированное наличие")){
                        productPresenceDataDonor = "В наличии";
                    }
                    if (!productPresenceDataMySite.equalsIgnoreCase(productPresenceDataDonor)) {
                        bufferedWriter.write("\n\n" + ++i +"\n\t" + productNameMySite + ", " + productMySite.getProductUrl() + ", " + productMySite.getProductPresenceData());
                        bufferedWriter.write("\n\t" + donorSiteProductName + ", " + productDonorSite.getProductUrl() + ", " + productDonorSite.getProductPresenceData());
                        bufferedWriter.flush();
                    }
                }
            }
        }
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************\n");
        bufferedWriter.flush();
    }

    public void writeData() {
        try {
            bufferedWriter.write("\n\nТовари з нашого сайту:");
            bufferedWriter.write(SaveMySite.ALL_PRODUCTS_LIST.toString());
            bufferedWriter.write("\n*********************************************************************************");
            bufferedWriter.write("\n*********************************************************************************");
            bufferedWriter.write("\n*********************************************************************************");
            bufferedWriter.write("\n*********************************************************************************\n");
            bufferedWriter.flush();
            bufferedWriter.write("\n\nТовари з сайту донора:");
            bufferedWriter.write(SaveDonor.ALL_PRODUCTS_LIST.toString());
            bufferedWriter.write("\n*********************************************************************************");
            bufferedWriter.write("\n*********************************************************************************");
            bufferedWriter.write("\n*********************************************************************************");
            bufferedWriter.write("\n*********************************************************************************\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doCompareMyToDonorByPrice() throws IOException {
        bufferedWriter.write("\n\nПорівняння цін позицій з нашого сайту відносно донорського (менше 145% та більше 155%): ");
        bufferedWriter.flush();
        int i = 0;
        for (Product productMySite : productListMySite) {
            String productNameMySite = productMySite.getProductName();
            for (Product productDonorSite : productListDonorSite) {
                String donorSiteProductName = productDonorSite.getProductName();
                if (productNameMySite.equalsIgnoreCase(donorSiteProductName)) {
                    String mySiteProductPrice = productMySite.getProductPrice();
                    String donorSiteProductPrice = productDonorSite.getProductPrice();
                    String[] mySitePriceString = mySiteProductPrice.split(" ");
                    String[] donorSitePriceString = donorSiteProductPrice.split(" ");
                    float percentCostByRound = 0;

                    try {
                        if (mySitePriceString.length == 3) {
                            mySitePriceString[0] = mySitePriceString[0] + mySitePriceString[1];
                        }
                        if (donorSitePriceString.length == 3) {
                            donorSitePriceString[0] = donorSitePriceString[0] + donorSitePriceString[1];
                        }

                        String mySitePriceS1 = mySitePriceString[0].replace(',', '.');
                        String donorSitePriceS2 = donorSitePriceString[0].replace(',', '.');

                        if ((productMySite.getProductName().equalsIgnoreCase("Гербицид «Напалм» 20 л, оригинал"))) {
                            System.out.println();
                        }

                        int i1 = mySitePriceS1.indexOf(160);
                        if (i1 != -1) {
                            String substring1 = mySitePriceS1.substring(0, i1);
                            String substring2 = mySitePriceS1.substring(i1+1);
                            mySitePriceS1 = substring1 + substring2;
                        }

                        int i2 = donorSitePriceS2.indexOf(160);
                        if (i2 != -1) {
                            String substring1 = donorSitePriceS2.substring(0, i2);
                            String substring2 = donorSitePriceS2.substring(i2+1);
                            donorSitePriceS2 = substring1 + substring2;
                        }

                        int i3 = mySitePriceS1.indexOf(' ');
                        if (i3 != -1) {
                            String substring1 = mySitePriceS1.substring(0, i3);
                            String substring2 = mySitePriceS1.substring(i3+1);
                            mySitePriceS1 = substring1 + substring2;
                        }

                        int i4 = donorSitePriceS2.indexOf(' ');
                        if (i4 != -1) {
                            String substring3 = donorSitePriceS2.substring(0, i4);
                            String substring4 = donorSitePriceS2.substring(i4+1);
                            donorSitePriceS2 = substring3 + substring4;
                        }

                        float mySitePrice = Float.parseFloat(mySitePriceS1);
                        float donorSitePrice = Float.parseFloat(donorSitePriceS2);
                        float percentCost = (mySitePrice / donorSitePrice)*10000;

                        int result = Math.round(percentCost);
                        percentCostByRound = (float) result / 100;
                    } catch (Exception e) {
                        System.out.println(productNameMySite);
                    }
                    if (percentCostByRound < 145 || percentCostByRound > 155) {
                        bufferedWriter.write("\n\n" + ++i + " - " + productNameMySite + ": " + percentCostByRound + "%");
                        bufferedWriter.write("\n" + productMySite.getCategory() + ", " + productMySite.getProductUrl() + " - " + productMySite.getProductPrice());
                        bufferedWriter.write("\n" + productDonorSite.getCategory() + ", " + productMySite.getProductUrl() + " - " + productDonorSite.getProductPrice());
                        bufferedWriter.flush();
                    }
                }
            }
        }
        bufferedWriter.write("\n\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************");
        bufferedWriter.write("\n*********************************************************************************\n");
        bufferedWriter.flush();
    }
}
