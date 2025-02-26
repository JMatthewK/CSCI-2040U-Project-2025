import java.io.*;
import java.util.*;

public class CatalogApp {
    public static void main(String[] args) throws IOException {
        CsvParser csvParser = new CsvParser();
        List<ClothingItem> clothingItemList;

        try{
            clothingItemList = csvParser.parseCsv("data/CatalogData.csv");
        } catch (java.lang.Exception e) {
            throw new IOException("error parsing file", e);
        }
        System.out.println("Catalog Data");
        for (ClothingItem item : clothingItemList){
            System.out.println();
            System.out.print(item.getId() + " ");
            System.out.print(item.getName() + " ");
            System.out.print(item.getBrand() + " ");
            System.out.print(item.getColor() + " ");
            System.out.print(item.getCategory() + " ");
            System.out.print(item.getPrice() + " ");
            System.out.print(item.getMaterial() + " ");
            System.out.print(item.getStyle() + " ");
            System.out.print(item.getFit() + " ");
        }
    }
}