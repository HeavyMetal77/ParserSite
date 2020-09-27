package ua.tarastom;

import java.io.IOException;
import java.util.List;

public class App {


    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        List<Category> categoryListMySite = SaveMySite.getCategoryList(SaveMySite.urlMySite);
        SaveMySite.getAllProduct(categoryListMySite);

        List<Category> categoryListDonor = SaveDonor.getCategoryList(SaveDonor.urlDonorSite);
        SaveDonor.getAllProduct(categoryListDonor);

        Comparison comparison = new Comparison(SaveMySite.ALL_PRODUCTS_LIST, SaveDonor.ALL_PRODUCTS_LIST);
        comparison.writeDataToFile();
        comparison.doCompareMyToDonorByPresence();
        comparison.doCompareMyToDonorByPrice();
        comparison.doCompareMyToDonor();
        comparison.doCompareDonorToMy();
        comparison.writeData();
        Long duration = (System.currentTimeMillis() - startTime)/1000;
        System.out.println(duration);


    }


}
