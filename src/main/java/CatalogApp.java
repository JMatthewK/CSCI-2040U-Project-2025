import java.io.*;
import java.util.*;

public class CatalogApp {
    public static void main(String[] args) throws IOException {

        CatalogController catalogController = new CatalogController();
        catalogController.startProgram();

        // Create a csvparser object
        CsvParser csvParser = new CsvParser();
        // Make a list of clothingItems to use
        List<ClothingItem> clothingItemList;

        // Use the csvparser object with the catalogCSV in our data folder
        // Try a catch incase it doesn't work
        try{
            clothingItemList = csvParser.parseCsv("data/CatalogData.csv");
        } catch (java.lang.Exception e) {
            throw new IOException("error parsing file", e);
        }


    }
}