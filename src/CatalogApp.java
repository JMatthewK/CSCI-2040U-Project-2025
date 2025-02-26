import java.io.*;
import java.util.*;

public class CatalogApp {
    public static void main(String[] args) throws IOException {
        // Create a csvparser object
        CsvParser csvParser = new CsvParser();
        // Make a list of clothingItems to use
        List<ClothingItem> clothingItemList;

        // Use the csvparser object with the catalogCSV in our data folder
        // Try an catch incase it doesn't work
        try{
            clothingItemList = csvParser.parseCsv("data/CatalogData.csv");
        } catch (java.lang.Exception e) {
            throw new IOException("error parsing file", e);
        }
        // Print out all of the data in this catalog
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