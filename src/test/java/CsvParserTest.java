import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvParserTest {
    @Test
    public void testParseCsv_WithRealCatalogData() throws IOException {
        String filePath = "data/CatalogDataForTesting.csv"; // Adjust if needed

        CsvParser parser = new CsvParser();
        List<ClothingItem> items = parser.parseCsv(filePath);

        assertEquals(5, items.size(), "Should load 5 items from CSV");

        // === Test Item 1 ===
        ClothingItem item1 = items.get(0);
        assertEquals(1, item1.getId());
        assertEquals("Nike Strike Men's Dri-FIT Short-Sleeve Football Top", item1.getName());
        assertEquals("Nike", item1.getBrand());
        assertEquals("Black", item1.getColor());
        assertEquals("Shirts", item1.getCategory());
        assertEquals(55.0, item1.getPrice(), 0.001);
        assertEquals("Polyester", item1.getMaterial());
        assertEquals("Sport", item1.getStyle());
        assertEquals("Slim", item1.getFit());
        assertEquals("https://www.nike.com/ca/t/strike-dri-fit-short-sleeve-football-top-DFnTwg/FN2399-010", item1.getlink());

        // === Test Item 3 ===
        ClothingItem item3 = items.get(2);
        assertEquals(3, item3.getId());
        assertEquals("Slim Fit Cargo Joggers", item3.getName());
        assertEquals("H&M", item3.getBrand());
        assertEquals("Black", item3.getColor());
        assertEquals("Pants", item3.getCategory());
        assertEquals(49.99, item3.getPrice(), 0.001);
        assertEquals("Cotton", item3.getMaterial());
        assertEquals("Sport", item3.getStyle());
        assertEquals("Slim", item3.getFit());
        assertEquals("https://www2.hm.com/en_ca/productpage.1261665001.html", item3.getlink());

        // === Test Item 5 (last item) ===
        ClothingItem item5 = items.get(4);
        assertEquals(5, item5.getId());
        assertEquals("Mens Classic T-shirt", item5.getName());
        assertEquals("SKIMS", item5.getBrand());
        assertEquals("White", item5.getColor());
        assertEquals("Shirts", item5.getCategory());
        assertEquals(74.0, item5.getPrice(), 0.001);
        assertEquals("Cotton", item5.getMaterial());
        assertEquals("Loungewear", item5.getStyle());
        assertEquals("Standard", item5.getFit());
        assertEquals("https://skims.com/en-ca/products/skims-cotton-mens-classic-t-shirt-chalk", item5.getlink());
    }

    @Test
    public void testParseAccount_WithSampleData() throws IOException {
        String fakeCsv =
                "Username,Password,Admin\n" +
                        "user1,pass1,false\n" +
                        "adminUser,adminpass,true\n";

        File tempFile = File.createTempFile("testAccounts", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fakeCsv);
        }

        CsvParser parser = new CsvParser();
        List<Account> accounts = parser.parseAccount(tempFile.getAbsolutePath());

        assertEquals(2, accounts.size());

        Account user1 = accounts.get(0);
        assertEquals("user1", user1.getUsername());
        assertEquals("pass1", user1.getPassword());
        assertFalse(user1.isAdmin());

        Account admin = accounts.get(1);
        assertEquals("adminUser", admin.getUsername());
        assertEquals("adminpass", admin.getPassword());
        assertTrue(admin.isAdmin());

        tempFile.deleteOnExit(); // Clean up
    }

    @Test
    public void testLoadImage_PlaceholderFallback() {
        // ID 999 is assumed to have no matching image
        ClothingItem item = new ClothingItem(999, "Test", "TestBrand", "Red", "Shirts",
                100.0, "Cotton", "Casual", "Standard", "https://example.com");

        ImageIcon image = item.getImage();

        assertNotNull(image, "Image should not be null.");
        assertTrue(image.getIconWidth() > 0 && image.getIconHeight() > 0,
                "Image dimensions should be greater than 0.");

        // Confirm it's the placeholder (not foolproof, but helps)
        assertFalse(item.hasImage(), "hasImage should return false if placeholder is used.");
    }

    @Test
    public void testLoadImage_ValidImageFile() {
        // Setup: Create a dummy item that has a valid image file (e.g., 1.png)
        int testId = 1;
        File imgFile = new File("data/img/" + testId + ".png");
        if (!imgFile.exists()) {
            System.out.println("⚠️ Skipping testLoadImage_ValidImageFile: image file not found.");
            return; // Skip test if the real image doesn't exist
        }

        ClothingItem item = new ClothingItem(testId, "RealImageItem", "Brand", "Blue", "Jacket",
                150.0, "Wool", "Formal", "Slim", "https://example.com");

        ImageIcon image = item.getImage();

        assertNotNull(image, "Image should not be null.");
        assertTrue(image.getIconWidth() > 0 && image.getIconHeight() > 0,
                "Image dimensions should be greater than 0.");
        assertTrue(item.hasImage(), "hasImage should return true if image exists.");
    }

    @Test
    public void testLoadImage_IsOnlyLoadedOnce() {
        ClothingItem item = new ClothingItem(999, "Test", "TestBrand", "Red", "Shirts",
                100.0, "Cotton", "Casual", "Standard", "https://example.com");

        item.loadImage(); // First load
        ImageIcon firstImage = item.getImage();

        // Force call again to verify it doesn't reload
        item.loadImage();
        ImageIcon secondImage = item.getImage();

        assertSame(firstImage, secondImage, "Image should be the same instance after repeated loads.");
    }

    @Test
    public void testSettersAndGetters() {
        ClothingItem item = new ClothingItem(42, "Tee", "Uniqlo", "Blue", "Shirts",
                19.99, "Cotton", "Casual", "Loose", "http://example.com");

        item.setName("Tank Top");
        item.setBrand("Zara");
        item.setColor("Red");
        item.setCategory("Tops");
        item.setPrice(24.99);
        item.setMaterial("Linen");
        item.setStyle("Relaxed");
        item.setFit("Slim");
        item.setLink("http://zara.com/tank");

        assertEquals("Tank Top", item.getName());
        assertEquals("Zara", item.getBrand());
        assertEquals("Red", item.getColor());
        assertEquals("Tops", item.getCategory());
        assertEquals(24.99, item.getPrice(), 0.001);
        assertEquals("Linen", item.getMaterial());
        assertEquals("Relaxed", item.getStyle());
        assertEquals("Slim", item.getFit());
        assertEquals("http://zara.com/tank", item.getlink());
    }

    @Test
    public void testHideUnhide() {
        ClothingItem item = new ClothingItem(43, "Sneakers", "Adidas", "White", "Shoes",
                89.99, "Synthetic", "Sport", "Standard", "http://adidas.com");

        assertFalse(item.getIfRemoved(), "Should not be hidden by default.");

        item.hide();
        assertTrue(item.getIfRemoved(), "Should be hidden after calling hide().");

        item.unhide();
        assertFalse(item.getIfRemoved(), "Should not be hidden after calling unhide().");
    }

    @Test
    public void testSetImageManually() {
        ClothingItem item = new ClothingItem(44, "Beanie", "Carhartt", "Green", "Hats",
                29.99, "Wool", "Street", "One Size", "http://carhartt.com");

        ImageIcon customImage = new ImageIcon("data/img/placeholder.png");
        item.setImage(customImage);

        assertSame(customImage, item.getImage(), "Image should match the manually set one.");
    }

    @Test
    public void testToStringFormat() {
        ClothingItem item = new ClothingItem(10, "Blazer", "Topman", "Navy", "Jackets",
                120.00, "Wool", "Formal", "Tailored", "http://topman.com");

        String expected = "10,Blazer,Topman,Navy,Jackets,120.0,Wool,Formal,Tailored,http://topman.com";
        assertEquals(expected, item.toString(), "toString should return CSV-style format.");
    }
}
