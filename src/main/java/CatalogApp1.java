import java.io.*;
import java.util.*;

public class CatalogApp {
    private static Scanner scanner = new Scanner(System.in);
    private static String username = "admin";
    private static String password = "password123";
    public static boolean login() {
        int attempts = 5;
        System.out.println("This action requires a login. Proceed? (Y/N)");
        String ans = scanner.nextLine().trim().toUpperCase();

        if (ans.equals("Y")) {
            while (attempts > 0) {
                System.out.println("Username:");
                String inputUser = scanner.nextLine();
                System.out.println("Password:");
                String inputPass = scanner.nextLine();

                if (inputUser.equals(username) && inputPass.equals(password)) {
                    System.out.println("Login Successful");
                    return true;
                } else {
                    System.out.println("Invalid username or password!");
                    attempts--;
                    if (attempts > 0) {
                        System.out.println("Attempts remaining: " + attempts);
                    }
                }
            }
            // If attempts reach 0
            System.out.println("Exceeded number of attempts, you do not have permission to continue.");
            System.out.println("Exiting to main menu...");
            return false;
        } else if (ans.equals("N")) {
            System.out.println("Exiting to main menu...");
            return false;
        } else {
            System.out.println("Invalid input. Returning to main menu...");
            return false;
        }
    }
    public static void addItem(List<ClothingItem> clothingItemList) {
        if(login()) {
            // Prompting for item details
            System.out.println("What is the name of the item?");
            String name = scanner.nextLine();

            System.out.println("What is the brand of the item?");
            String brand = scanner.nextLine();

            System.out.println("What is the color of the item?");
            String color = scanner.nextLine();

            // Validate Category Input
            String[] validCategories = {"Shirt", "Sweater", "Overshirt", "Pants", "Shorts", "Skirt"};
            String category;
            while (true) {
                System.out.println("What is the category of the item? (Shirt, Sweater, Overshirt, Pants, Shorts, Skirt)");
                category = scanner.nextLine();
                if (isValidInput(category, validCategories)) break;
                System.out.println("Invalid category! Please enter one of the listed categories.");
            }

            // Validate and Read Price
            double price;
            while (true) {
                System.out.println("How much does the item cost?");
                if (scanner.hasNextDouble()) {
                    price = scanner.nextDouble();
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Invalid input! Please enter a valid number.");
                    scanner.nextLine();
                }
            }

            System.out.println("What is the material of the item?");
            String material = scanner.nextLine();

            // Validate Style Input
            String[] validStyles = {"Casual", "Sport", "Loungewear", "Formal"};
            String style;
            while (true) {
                System.out.println("What is the style of the item? (Casual, Sport, Loungewear, Formal)");
                style = scanner.nextLine();
                if (isValidInput(style, validStyles)) break;
                System.out.println("Invalid style! Please enter one of the listed styles.");
            }

            // Validate Fit Input
            String[] validFits = {"Standard", "Slim", "Loose", "Oversized"};
            String fit;
            while (true) {
                System.out.println("What is the fit of the item? (Standard, Slim, Loose, Oversized)");
                fit = scanner.nextLine();
                if (isValidInput(fit, validFits)) break;
                System.out.println("Invalid fit! Please enter one of the listed fits.");
            }

            // Generate Unique Item ID (adding item at end of existing catalogue)
            int id = clothingItemList.size() + 1;

            // Create and Add the Clothing Item
            ClothingItem item = new ClothingItem(id, name, brand, color, category, price, material, style, fit);
            clothingItemList.add(item);

            System.out.println("Item successfully added: " + item);
        }
    }

    public static boolean isValidInput(String input, String[] validValues) {
        for (String value : validValues) {
            if (input.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

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
        System.out.println("ID, Name, Brand, Color, Category, Price, Material, Style, Fit");
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
        addItem(clothingItemList);
    }
}
