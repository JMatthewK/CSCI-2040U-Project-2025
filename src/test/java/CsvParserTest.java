import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
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
}
