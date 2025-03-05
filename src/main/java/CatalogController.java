import java.io.IOException;
import java.util.List;

public class CatalogController {
    public CatalogController(){

    }

    public void startProgram() throws IOException {
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

        CatalogViewer catalogViewer = new CatalogViewer();
        catalogViewer.startGUI(clothingItemList);
    }

}