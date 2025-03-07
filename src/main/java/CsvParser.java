import java.io.*;
import java.util.*;

// Class to help parse through the csv data file
public class CsvParser {
    public List<ClothingItem> parseCsv(String file) throws IOException{
        List<ClothingItem> clothingItems = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        // Skip the header row with the category names
        br.readLine();

        while((line = br.readLine()) != null){
            // Comma delimiter to read the CSV file attribute by attribute
            String[] attributes = line.split(",");
            if(attributes.length == 10){
                int id = Integer.parseInt(attributes[0]);
                String name = attributes[1];
                String brand = attributes[2];
                String color = attributes[3];
                String category = attributes[4];
                double price = Double.parseDouble(attributes[5]);
                String material = attributes[6];
                String style = attributes[7];
                String fit = attributes[8];

                ClothingItem item = new ClothingItem(id, name, brand, color, category, price, material, style, fit);
                clothingItems.add(item);
            }
        }
        // Close bufferedReader
        br.close();
        // Return arrayList of clothingItems
        return clothingItems;
    }

}