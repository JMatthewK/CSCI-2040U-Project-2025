import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CatalogViewer {
    public CatalogViewer() {

    }

    public void startGUI(List<ClothingItem> clothingItemList){
        JFrame frame = new JFrame("CTLG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create column names for the table
        String[] columnNames = {"ID", "Name", "Brand", "Color", "Category", "Price", "Material", "Style", "Fit", "Image"};

        // Create data for the table
        Object[][] rowData = new Object[clothingItemList.size()][10]; // 10 columns (one for each attribute, one for image)

        for(int i = 0; i < clothingItemList.size(); i++){
            ClothingItem clothingItem = clothingItemList.get(i);
            rowData[i][0] = clothingItem.getId();
            rowData[i][1] = clothingItem.getName();
            rowData[i][2] = clothingItem.getBrand();
            rowData[i][3] = clothingItem.getColor();
            rowData[i][4] = clothingItem.getCategory();
            rowData[i][5] = clothingItem.getPrice();
            rowData[i][6] = clothingItem.getMaterial();
            rowData[i][7] = clothingItem.getStyle();
            rowData[i][8] = clothingItem.getFit();
            rowData[i][9] = clothingItem.getImage();
        }

        DefaultTableModel model = new DefaultTableModel(rowData, columnNames);
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);

        frame.setVisible(true);
    }
}