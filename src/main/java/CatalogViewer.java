import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CatalogViewer {
    //parse csv at beginning instead of parsing it multiple times (takes longer to load)
    CsvParser csvParser = new CsvParser();
    private List<ClothingItem> clothingItemList = csvParser.parseCsv("data/CatalogData.csv");
    private JFrame frame;
    private JPanel imagePanel;
    private Set<String> selectedColors = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedMaterials = new HashSet<>();
    private Set<String> selectedStyles = new HashSet<>();
    private Set<String> selectedFits = new HashSet<>();

    public CatalogViewer() throws IOException {
        frame = new JFrame("CTLG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
    }
    public CatalogViewer(List<ClothingItem> clothingItemList) throws IOException {
        this.clothingItemList = clothingItemList;
    }
    // main menu
    public void startMainMenu() {
        // Create a main menu panel
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BorderLayout());

        // Label for title
        JLabel titleLabel = new JLabel("CLTG.", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        mainMenuPanel.add(titleLabel, BorderLayout.NORTH);
        //Menu is a simple button panel for now
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1)); // 2 buttons, 1 column

        // add item button
        JButton addItemButton = new JButton("Add New Item");
        addItemButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            addItem();
        });

        // login button
        JButton loginButton = new JButton("login");
        loginButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            login();
        });
        
        //display catalog button
        JButton openCatalogButton = new JButton("View Catalogue");
        openCatalogButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            startGUI(clothingItemList);
        });

        // exit the application button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(openCatalogButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(addItemButton);
        buttonPanel.add(exitButton);

        // add the button panel to the main menu
        mainMenuPanel.add(buttonPanel, BorderLayout.CENTER);
        frame.getContentPane().add(mainMenuPanel);
        frame.setVisible(true);
    }

    //Login screen
    public void login() {
        // Login window
        JFrame frame = new JFrame("Login System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,200);
        frame.setLayout(new GridLayout(3, 2, 10, 10));

        // GUI Components
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JLabel resultLabel = new JLabel("");

        // User creds (hashed)
        String adminUsername = "admin";
        String adminPassword = hashPassword("adminpass");
        System.out.println(adminPassword);

        String userUsername = "user";
        String userPassword = hashPassword("userpass");
        System.out.println(userPassword);

        // Button repsonse and override for action of button
        loginButton.addActionListener(new ActionListener() {
            // User input
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                String input = hashPassword(password);

                // Comparing creds
                if (username.equals(adminUsername) && input.equals(adminPassword)) {
                    resultLabel.setText("Admin access granted!");
                    resultLabel.setForeground(Color.BLUE);
                    frame.setVisible(false);
                    frame.dispose();
                    startGUI(clothingItemList);
                    // openAdminCatalog();
                } else if (username.equals(userUsername) && input.equals(userPassword)) {
                    resultLabel.setText("User access granted!");
                    resultLabel.setForeground(Color.GREEN);
                    frame.setVisible(false);
                    frame.dispose();
                    startGUI(clothingItemList);
                    // openUserCatalog();
                } else {
                    resultLabel.setText("Invalid credentials.");
                    resultLabel.setForeground(Color.RED);
                }
            }
        });

        // Adding the components to the frame and display
        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(loginButton);
        frame.add(resultLabel);

        frame.setVisible(true);

    }

    // Super cool security system I learned
    public static String hashPassword(String password) {
        // Exception handling if SHA-256 algo isn't accessable
        try {
            // Gets and instance of the SHA-256 hashing algo
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // hashes password into unique byte array
            byte[] hash = md.digest(password.getBytes());

            // converting hash to readable hexString
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: add catalogs for user and admin to seperately login to

    private static void openAdminCatalog() {
        JOptionPane.showMessageDialog(null, "Welcome, Admin! You have full access.");
    }

    private static void openUserCatalog() {
        JOptionPane.showMessageDialog(null, "Welcome, User! You have limited access.");
    }
    
    //Add item screen
    public void addItem() {
        JPanel addPanel = new JPanel(new GridLayout(10, 2));

        JTextField name = new JTextField(20);
        JTextField brand = new JTextField(20);
        JTextField color = new JTextField(20);
        //JCombobox used for limited selections
        String[] categories = {"Shirt", "Sweater", "Pants", "Skirts"};
        JComboBox<String> categoriesList = new JComboBox<>(categories);

        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setGroupingUsed(true);
        JFormattedTextField price = new JFormattedTextField(new NumberFormatter(currencyFormat));
        price.setColumns(10);

        JTextField material = new JTextField(20);
        String[] styles = {"Casual", "Formal", "Loungewear", "Sport"};
        JComboBox<String> styleList = new JComboBox<>(styles);

        String[] fits = {"Standard", "Loose", "Slim", "Oversized"};
        JComboBox<String> fitList = new JComboBox<>(fits);
        //Upload image using jfilechooser
        JButton uploadImageButton = new JButton("Upload Image");
        JLabel imagePathLabel = new JLabel("No Image Selected");
        final File[] selectedFile = {null};
        uploadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = fileChooser.getSelectedFile();
                imagePathLabel.setText(selectedFile[0].getName());
            }
        });
        //Add button converts all entries into strings (double for price), and adds it to clothingitem list
        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(e -> {
            double itemPrice;
            try {
                itemPrice = Double.parseDouble(price.getText().replaceAll(",", ""));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid price format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ClothingItem clothing = new ClothingItem(
                    clothingItemList.size() + 1,
                    name.getText(),
                    brand.getText(),
                    color.getText(),
                    categoriesList.getSelectedItem().toString(),
                    itemPrice,
                    material.getText(),
                    styleList.getSelectedItem().toString(),
                    fitList.getSelectedItem().toString()
            );
            //save image to data/img
            clothingItemList.add(clothing);
            int newId = clothingItemList.size();
            String imagePath = "data/img/" + newId + ".png";
            //save image immediately (initially would save after closing application)
            try {
                File destination = new File(imagePath);
                Files.copy(selectedFile[0].toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                destination.setReadable(true, false);
                destination.setWritable(true, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //display new catalog with added item
            startGUI(clothingItemList);
        });
        addPanel.add(new JLabel("Name:"));
        addPanel.add(name);
        addPanel.add(new JLabel("Brand:"));
        addPanel.add(brand);
        addPanel.add(new JLabel("Color:"));
        addPanel.add(color);
        addPanel.add(new JLabel("Category:"));
        addPanel.add(categoriesList);
        addPanel.add(new JLabel("Price:"));
        addPanel.add(price);
        addPanel.add(new JLabel("Material:"));
        addPanel.add(material);
        addPanel.add(new JLabel("Style:"));
        addPanel.add(styleList);
        addPanel.add(new JLabel("Fit:"));
        addPanel.add(fitList);
        addPanel.add(uploadImageButton); addPanel.add(imagePathLabel);
        addPanel.add(addButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(addPanel);
        frame.revalidate();
        frame.repaint();
    }
    //Delete item
    private void deleteItemById() {
        String inputId = JOptionPane.showInputDialog(frame,
                "Enter the ID of the item to delete:");

        if (inputId != null && !inputId.isEmpty()) {
            int itemId = Integer.parseInt(inputId); // Convert input to integer
            ClothingItem itemToDelete = findItemById(itemId);

            if (itemToDelete != null) {
                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete " + itemToDelete.getName() + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    clothingItemList.remove(itemToDelete); // Remove from the list

                    // delete the associated image file
                    File imageFile = new File("data/img/" + itemToDelete.getId() + ".jpg");
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }

                    updateImagePanel(clothingItemList); // Refresh the UI
                }
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Item with ID " + itemId + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //Quick helper method
    private ClothingItem findItemById(int id) {
        for (ClothingItem item : clothingItemList) {
            if (item.getId() == id) {
                return item; // Return the matching item
            }
        }
        return null; // Return null if not found
    }

    // method to display the catalog in the same window
    public void startGUI(List<ClothingItem> clothingItemList) {
        JPanel catalogPanel = new JPanel();
        catalogPanel.setLayout(new BorderLayout());

        JPanel filterPanel = createFilterPanel();
        catalogPanel.add(filterPanel, BorderLayout.NORTH);

        imagePanel = new JPanel(new GridLayout(0, 5, 10, 10));
        updateImagePanel(clothingItemList);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        catalogPanel.add(scrollPane, BorderLayout.CENTER);
        JButton deleteButton = new JButton("Delete Item by ID");
        deleteButton.addActionListener(e -> deleteItemById());
        frame.add(deleteButton, BorderLayout.SOUTH); // Add to the frame or wherever you prefer


        frame.add(catalogPanel);
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
            //get the image
            ImageIcon icon = item.getImage();
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
        dialog.add(detailsPanel);
        dialog.setVisible(true);
    }
}
