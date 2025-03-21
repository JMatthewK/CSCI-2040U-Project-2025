
        import java.io.*;
        import java.util.*;
public class Testing {
    public static void main(String[] args) throws IOException {
        System.out.println("Test Case 1: Getters");
        ClothingItem item = new ClothingItem(
                1, "T-Shirt",
                "Nike", "Black",
                "Shirt", 29.99,
                "Cotton", "Casual",
                "Standard", "https://nike.com/tshirt"
        );
        System.out.println("ID: " + item.getId() + " (Expected: 1)");
        System.out.println("Name: " + item.getName() + " (Expected: T-Shirt)");
        System.out.println("Brand: " + item.getBrand() + " (Expected: Nike)");
        System.out.println("Color: " + item.getColor() + " (Expected: Black)");
        System.out.println("Category: " + item.getCategory() + " (Expected: Shirt)");
        System.out.println("Price: " + item.getPrice() + " (Expected: 29.99)");
        System.out.println("Material: " + item.getMaterial() + " (Expected: Cotton)");
        System.out.println("Style: " + item.getStyle() + " (Expected: Casual)");
        System.out.println("Fit: " + item.getFit() + " (Expected: Standard)");

        System.out.println();
        System.out.println();

        System.out.println("Test Case 2: ToString() ");
        ClothingItem item2 = new ClothingItem(
                1, "Classic T-Shirt",
                "Nike", "Black",
                "Shirt", 29.99,
                "Cotton", "Casual",
                "Standard", "https://nike.com/classictshirt"
        );
        System.out.println("toString(): " + item2.toString());
        System.out.println("Expected: 1,Classic T-Shirt,Nike,Black,Shirt,29.99,Cotton,Casual,Standard,https://nike.com/classictshirt");

        System.out.println();
        System.out.println();

        ClothingItem item3 = new ClothingItem(
                1, "Classic T-Shirt",
                "Nike", "Black",
                "Shirt", 29.99,
                "Cotton", "Casual",
                "Standard", "https://example.com/nike-tshirt"
        );

        System.out.println("Test Case 4: Setters");
        item3.setName("T-Shirt");
        item3.setBrand("Adidas");
        item3.setColor("White");
        item3.setCategory("Sweater");
        item3.setPrice(39.99);
        item3.setMaterial("Polyester");
        item3.setStyle("Sport");
        item3.setFit("Loose");
        item3.setLink("https://adidas.com/tshirt");

        System.out.println("Updated Name: " + item3.getName() + " (Expected: T-Shirt)");
        System.out.println("Updated Brand: " + item3.getBrand() + " (Expected: Adidas)");
        System.out.println("Updated Color: " + item3.getColor() + " (Expected: White)");
        System.out.println("Updated Category: " + item3.getCategory() + " (Expected: Sweater)");
        System.out.println("Updated Price: " + item3.getPrice() + " (Expected: 39.99)");
        System.out.println("Updated Material: " + item3.getMaterial() + " (Expected: Polyester)");
        System.out.println("Updated Style: " + item3.getStyle() + " (Expected: Sport)");
        System.out.println("Updated Fit: " + item3.getFit() + " (Expected: Loose)");
        System.out.println("Updated Link: " + item3.getlink() + " (Expected: https://adidas.com/tshirt");

        System.out.println();
        System.out.println();

        //list for csv
        List<ClothingItem> testItems = new ArrayList<>();
        testItems.add(item);
        testItems.add(item2);
        testItems.add(item3);

        CatalogViewer catalogViewer = new CatalogViewer(testItems);
        //path to testingcsv function
        String filePath = "data/testingdata.csv";
        //call writecsv
        catalogViewer.writecsv(testItems, filePath);

        System.out.println("Test Case 5: Writecsv()");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println("Reading file: " + filePath);

            // header
            String header = reader.readLine();
            System.out.println("Header: " + header);
            System.out.println("Expected Header: ID,Product Name,Brand,Color,Category,Price (CAD),Material,Style,Fit,Link");

            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                System.out.println();
                ClothingItem currentitem = testItems.get(lineNumber - 1);
                String expectedLine = currentitem.toString();
                if (line.equals(expectedLine)) {
                    System.out.println("Line: " + line);
                    System.out.println("Expected: " + expectedLine);
                    System.out.println("matches expected output.");
                } else {
                    System.out.println("Line: " + line);
                    System.out.println("Expected: " + expectedLine);
                    System.out.println("doesn't matches expected output.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}


