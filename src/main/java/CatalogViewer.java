import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogViewer {
    //clothings
    private List<ClothingItem> clothingItemList;
    //panels for ui
    private JPanel imagePanel;
    private JFrame frame;

    //current filters
    private Set<String> selectedColors = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedMaterials = new HashSet<>();
    private Set<String> selectedStyles = new HashSet<>();
    private Set<String> selectedFits = new HashSet<>();

    //block list
    private Set<String> noShowList = new HashSet<>();
    public CatalogViewer(List<ClothingItem> clothingItemList) {
        this.clothingItemList = clothingItemList;
    }

    public void startGUI() {
        frame = new JFrame("CTLG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel filterPanel = createFilterPanel();
        panel.add(filterPanel, BorderLayout.NORTH);

        imagePanel = new JPanel(new GridLayout(0, 5, 10, 10));
        updateImagePanel(clothingItemList);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    //add every filter button to panel
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridLayout(5, 1));

        String[] colors = {"Blue", "Grey", "Brown", "Green", "Beige", "Pink", "Red", "Orange", "Purple"};
        JPanel colorPanel = createFilterButtons("Color", colors);
        filterPanel.add(colorPanel);

        String[] categories = {"Shirts", "Shorts", "Pants", "Sweaters"};
        JPanel categoryPanel = createFilterButtons("Category", categories);
        filterPanel.add(categoryPanel);

        String[] materials = {"Cotton", "Polyester", "Nylon"};
        JPanel materialPanel = createFilterButtons("Material", materials);
        filterPanel.add(materialPanel);

        String[] styles = {"Formal", "Casual", "Sport", "Loungewear"};
        JPanel stylePanel = createFilterButtons("Style", styles);
        filterPanel.add(stylePanel);

        String[] fits = {"Standard", "Loose", "Slim", "Oversized"};
        JPanel fitPanel = createFilterButtons("Fit", fits);
        filterPanel.add(fitPanel);

        return filterPanel;
    }

    // create button for current filtertype
    private JPanel createFilterButtons(String filterType, String[] options) {
        JPanel panel = new JPanel();
        //for each option create a button
        for (String option : options) {
            JToggleButton button = new JToggleButton(option);
            //add a listener
            button.addActionListener(e -> {
                updateSelectedFilters(filterType, ((JToggleButton) e.getSource()).getText());
            });
            panel.add(button);
        }
        return panel;
    }
    //when the button is pressed change the filter
    private void updateSelectedFilters(String filterType, String filterValue) {
        //for the type of filter add or remove the value
        switch (filterType) {
            case "Color":
                if (selectedColors.contains(filterValue)) {
                    selectedColors.remove(filterValue);
                } else {
                    selectedColors.add(filterValue);
                }
                break;
            case "Category":
                if (selectedCategories.contains(filterValue)) {
                    selectedCategories.remove(filterValue);
                } else {
                    selectedCategories.add(filterValue);
                }
                break;
            case "Material":
                if (selectedMaterials.contains(filterValue)) {
                    selectedMaterials.remove(filterValue);
                } else {
                    selectedMaterials.add(filterValue);
                }
                break;
            case "Style":
                if (selectedStyles.contains(filterValue)) {
                    selectedStyles.remove(filterValue);
                } else {
                    selectedStyles.add(filterValue);
                }
                break;
            case "Fit":
                if (selectedFits.contains(filterValue)) {
                    selectedFits.remove(filterValue);
                } else {
                    selectedFits.add(filterValue);
                }
                break;
        }
        applyFilters();
    }

    private void applyFilters() {
        //new list for current filters
        List<ClothingItem> filteredItems = new ArrayList<>();

        //for each item in the clothing list
        for (ClothingItem item : clothingItemList) {
            boolean matches = true;
            //if a filter is active and the item tag doesn't match then dont add to list;
            if (!selectedColors.isEmpty() && !selectedColors.contains(item.getColor())) {
                matches = false;
            }
            if (!selectedCategories.isEmpty() && !selectedCategories.contains(item.getCategory())) {
                matches = false;
            }
            if (!selectedMaterials.isEmpty() && !selectedMaterials.contains(item.getMaterial())) {
                matches = false;
            }
            if (!selectedStyles.isEmpty() && !selectedStyles.contains(item.getStyle())) {
                matches = false;
            }
            if (!selectedFits.isEmpty() && !selectedFits.contains(item.getFit())) {
                matches = false;
            }
            //add to list
            if (matches) {
                filteredItems.add(item);
            }
        }
        //update the new list
        updateImagePanel(filteredItems);
    }

    //updates the panel of images
    private void updateImagePanel(List<ClothingItem> items) {
        //remove all current images
        imagePanel.removeAll();
        //for every clothing item in the current list
        for (ClothingItem item : items) {
            //if is in the no show list dont show
            if (noShowList.contains(item.getName())) continue;
            //get the image
            ImageIcon icon = item.getImage();

            if (!isValidImage(icon)) continue;

            Image img = icon.getImage();
            //scale it correctly
            Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImg);

            JLabel imageLabel = new JLabel(icon);
            //when item is hovered show the name and ability to click for more
            imageLabel.setToolTipText(item.getName() + " Click for more info");
            //when image is clicked show the information
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showItemDetails(item);
                }
            });
            imagePanel.add(imageLabel);
        }
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    //shows the information about the image they clicked on
    private void showItemDetails(ClothingItem item) {
        //create new box to show information
        JDialog dialog = new JDialog(frame, "Item Details", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(frame);

        //show most details can add more if want
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.add(new JLabel("Name: " + item.getName()));
        detailsPanel.add(new JLabel("Brand: " + item.getBrand()));
        detailsPanel.add(new JLabel("Price: $" + item.getPrice()));
        detailsPanel.add(new JLabel("Contains Material: " + item.getMaterial()));
        detailsPanel.add(new JLabel("Fit: " + item.getFit()));
        //delete button
        JButton deleteButton = new JButton("Hide Item");
        deleteButton.addActionListener(e -> {
            noShowList.add(item.getName());
            dialog.dispose();
            applyFilters();
        });
        detailsPanel.add(deleteButton);

        dialog.add(detailsPanel);
        dialog.setVisible(true);
    }

    //check if image exist
    private boolean isValidImage(ImageIcon icon) {
        Image img = icon.getImage();
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        return width > 1 && height > 1;
    }
}