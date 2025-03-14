import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CatalogViewerTest {
    private CatalogViewer catalogViewer;
    private List<ClothingItem> testClothingList;

    void setUp() throws IOException {
        testClothingList = new ArrayList<>();
        testClothingList.add(new ClothingItem(1, "T-Shirt", "Nike", "Blue", "Shirt", 19.99, "Cotton", "Casual", "Standard"));
        testClothingList.add(new ClothingItem(2, "Sweater", "Adidas", "Grey", "Sweater", 39.99, "Wool", "Formal", "Loose"));
        catalogViewer = new CatalogViewer(testClothingList);
    }

    void testFindItemById_ValidId() {
        ClothingItem item = catalogViewer.findItemById(1);
        if (item == null || !item.getName().equals("T-Shirt")) {
            throw new RuntimeException("testFindItemById_ValidId failed");
        }
    }

    void testFindItemById_InvalidId() {
        ClothingItem item = catalogViewer.findItemById(10);
        if (item != null) {
            throw new RuntimeException("testFindItemById_InvalidId failed");
        }
    }

    public static void main(String[] args) throws IOException {
        CatalogViewerTest test = new CatalogViewerTest();
        test.setUp();
        test.testFindItemById_ValidId();
        test.testFindItemById_InvalidId();
        System.out.println("All tests passed!");
    }
}
