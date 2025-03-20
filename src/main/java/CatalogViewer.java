import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
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
import com.opencsv.CSVWriter;

public class CatalogViewer {
    // Parse the CSV file at beginning as opposed to parsing it multiple times (for quicker load times)
    CsvParser csvParser = new CsvParser();
    private List<ClothingItem> clothingItemList = csvParser.parseCsv("data/CatalogData.csv");
    private JFrame frame;
    private JPanel imagePanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Set<String> selectedColors = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedMaterials = new HashSet<>();
    private Set<String> selectedStyles = new HashSet<>();
    private Set<String> selectedFits = new HashSet<>();

    public CatalogViewer() throws IOException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("CTLG.");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenSize.width, screenSize.height); // Set size of application to size of screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Automaticaly maximize screen
    }
    public CatalogViewer(List<ClothingItem> clothingItemList) throws IOException {
        this.clothingItemList = clothingItemList;
    }

    // main menu
    public void startMainMenu() {
        // Create a main menu panel
        JPanel mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new BorderLayout());

        // Create Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(150, frame.getHeight()));

        // Creates a cardLayout and cardPanel allowing me to switch panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        frame.getContentPane().add(cardPanel);

        // Label for title
        JLabel titleLabel = new JLabel("CTLG.", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        mainMenuPanel.add(titleLabel, BorderLayout.NORTH);

        //Menu is a simple button panel for now
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1)); // 2 buttons, 1 column

        // add item button
        JButton addItemButton = new JButton("Add New Item");
        addItemButton.addActionListener(e -> {
            addItem();
        });

        // login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            login();
        });

        // add back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu")); // Back Button

        //display catalog button
        JButton openCatalogButton = new JButton("View Catalogue");
        openCatalogButton.addActionListener(e -> {
            startGUI(clothingItemList);
        });

        // exit the application button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(openCatalogButton);
        sidebar.add(loginButton);
        sidebar.add(addItemButton);
        sidebar.add(backButton);
        buttonPanel.add(exitButton);

        // add the mainMenu panel to the cardPanels
        cardPanel.add(buttonPanel, "mainMenu");

        // add the sidebar and catalog panel to the mainmenupanel
        mainMenuPanel.add(cardPanel, BorderLayout.CENTER);
        mainMenuPanel.add(sidebar, BorderLayout.WEST);

        frame.getContentPane().add(mainMenuPanel);
        frame.setVisible(true);


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

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton deleteButton = new JButton("Delete Item by ID");
        deleteButton.addActionListener(e -> deleteItemById());

        bottomPanel.add(deleteButton);

        catalogPanel.add(bottomPanel, BorderLayout.SOUTH);
        cardPanel.add(catalogPanel, "catalogPanel");
        cardLayout.show(cardPanel, "catalogPanel");

        frame.revalidate();
        frame.repaint();
    }

    // Login screen
    public void login() {
        // Login window
        JFrame frame = new JFrame("Login System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300,200);
        frame.setLayout(new GridLayout(3, 2, 10, 10));

        // GUI Components
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        Rectangle r = new Rectangle(75,50);
        userField.setBounds(r);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        passField.setBounds(r);

        JButton loginButton = new JButton("Login");
        JLabel resultLabel = new JLabel("");
        JButton registerButton = new JButton("Register Account");
        registerButton.addActionListener(e -> {
            frame.dispose();
            register();
        });

        // Button response and override for action of button
        loginButton.addActionListener(new ActionListener() {
            // User input
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                String input = hashPassword(password);

                List<Account> accounts = null;
                
                try {
                    accounts = csvParser.parseAccount("data/accounts.csv");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                for(int i = 0; i <= accounts.size(); i++) {
                    if (username.equals(accounts.get(i).getUsername()) && input.equals(hashPassword(accounts.get(i).getPassword()))) {
                        resultLabel.setText("Access granted!");
                        resultLabel.setForeground(Color.BLUE);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        frame.setVisible(false);
                        frame.dispose();
                        startGUI(clothingItemList);
                    }
                    else {
                        resultLabel.setText("Invalid credentials.");
                        resultLabel.setForeground(Color.RED);
                    }
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
        frame.add(registerButton);
        frame.setVisible(true);

    }

    // Register a new account (new account gets added to a CSV account database)
    public void register() {
        JFrame frame = new JFrame("Register Account");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLayout(new GridLayout(4, 3, 10, 10));
        frame.setResizable(false);

        // GUI Components
        JLabel newUserLabel = new JLabel("Username:");
        JTextField newUserField = new JTextField(15);
        Rectangle r = new Rectangle(75,50);
        newUserField.setBounds(r);

        JLabel newPassLabel = new JLabel("Password:");
        JPasswordField newPassField = new JPasswordField(15);
        newPassField.setBounds(r);

        JButton registerButton = new JButton("Register Account");
        JLabel resultLabel = new JLabel("");

        frame.add(newUserLabel);
        frame.add(newUserField);
        frame.add(newPassLabel);
        frame.add(newPassField);
        frame.add(registerButton);
        frame.add(resultLabel);
        frame.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = newUserField.getText();
                String password = new String(newPassField.getPassword());
                
                CSVWriter writer = null;
                try {
                     writer = new CSVWriter(new FileWriter("data/accounts.csv", true),
                                                                        CSVWriter.DEFAULT_SEPARATOR,
                                                                        CSVWriter.NO_QUOTE_CHARACTER,
                                                                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                                                        CSVWriter.DEFAULT_LINE_END);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                String[] accString = {username,password};
                writer.writeNext(accString);

                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                resultLabel.setText("Account created successfully!");
                resultLabel.setForeground(Color.GREEN);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                frame.setVisible(false);
                frame.dispose();
                login();
            }
        });

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

    // TODO: Add catalogs for users vs. admins

    private static void openAdminCatalog() {
        JOptionPane.showMessageDialog(null, "Welcome, Admin! You have full access.");
    }

    private static void openUserCatalog() {
        JOptionPane.showMessageDialog(null, "Welcome, User! You have limited access.");
    }
    private void deleteItem(ClothingItem item) {
        clothingItemList.remove(item); // Remove from the list

        // Delete the image file
        File imageFile = new File("data/img/" + item.getId() + ".png");
        if (imageFile.exists()) {
            imageFile.delete();
        }

        // Refresh the UI
        updateImagePanel(clothingItemList);
    }
    //Add item screen
    public void addItem() {
        JPanel addPanel = new JPanel(new GridLayout(11, 2));

        JTextField name = new JTextField(20);
        JTextField brand = new JTextField(20);
        JTextField color = new JTextField(20);
        JTextField link = new JTextField(20);
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
                    fitList.getSelectedItem().toString(),
                    link.getText().toString()
            );
            //save image to data/img
            clothingItemList.add(clothing);
            //call to write new csv
            writecsv(clothingItemList);
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
        addPanel.add(new JLabel("Link:"));
        addPanel.add(link);

        addPanel.add(uploadImageButton); addPanel.add(imagePathLabel);
        addPanel.add(addButton);


        cardPanel.add(addPanel, "addPanel");
        cardLayout.show(cardPanel, "addPanel");

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
                    File imageFile = new File("data/img/" + itemToDelete.getId() + ".png");
                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                    writecsv(clothingItemList);


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
    //add every filter button to panel
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridLayout(5, 1));

        String[] colors = {"Blue", "Grey", "Brown", "Green", "Beige", "Pink", "Red", "Orange", "Purple"};
        JPanel colorPanel = createFilterButtons("Color", colors);
        filterPanel.add(colorPanel);

        String[] categories = {"Shirts", "Shorts", "Pants", "Sweaters", "Skirts"};
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
        imagePanel.removeAll();

        for (ClothingItem item : items) {
            ImageIcon icon = item.getImage();
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImg);

            JLabel imageLabel = new JLabel(icon);
            imageLabel.setToolTipText(item.getName() + " - Click for more info");

            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        // Left-click: Show item details
                        showItemDetails(item);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        // Right-click: Ask for delete confirmation
                        int confirm = JOptionPane.showConfirmDialog(
                                frame,
                                "Are you sure you want to delete " + item.getName() + "?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteItem(item);
                        }
                    }
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
        dialog.setSize(350, 325);
        dialog.setLocationRelativeTo(frame);

        //show most details can add more if want
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.add(new JLabel("ID: " + item.getId()));
        detailsPanel.add(new JLabel("Name: " + item.getName()));
        detailsPanel.add(new JLabel("ID: " + item.getId()));
        detailsPanel.add(new JLabel("Brand: " + item.getBrand()));
        detailsPanel.add(new JLabel("Price: $" + item.getPrice()));
        detailsPanel.add(new JLabel("Contains Material: " + item.getMaterial()));
        detailsPanel.add(new JLabel("Fit: " + item.getFit()));
        dialog.add(detailsPanel);
        //create button to allow user to edit
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            //call the edit function and send the current item
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedItem(item);
            }
        });


        // add to bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    //when clicked, creates a prompt that allows user to edit current item
    private void editSelectedItem(ClothingItem selectedItem) {
        JDialog editDialog = new JDialog(frame, "Edit Clothing Item", true);
        editDialog.setLayout(new GridLayout(0, 2));

        //fields to write with current values in already
        JTextField nameField = new JTextField(selectedItem.getName(), 20);
        JTextField brandField = new JTextField(selectedItem.getBrand(), 20);
        JTextField colorField = new JTextField(selectedItem.getColor(), 20);
        JTextField linkField = new JTextField(selectedItem.getlink(), 20);

        // categories
        String[] categories = {"Shirt", "Sweater", "Pants", "Skirts"};
        JComboBox<String> categoriesList = new JComboBox<>(categories);
        categoriesList.setSelectedItem(selectedItem.getCategory());

        // price
        NumberFormat currencyFormat = NumberFormat.getNumberInstance();
        currencyFormat.setGroupingUsed(true);
        JFormattedTextField priceField = new JFormattedTextField(new NumberFormatter(currencyFormat));
        priceField.setColumns(10);
        priceField.setValue(selectedItem.getPrice());

        JTextField materialField = new JTextField(selectedItem.getMaterial(), 20);

        // styles
        String[] styles = {"Casual", "Formal", "Loungewear", "Sport"};
        JComboBox<String> styleList = new JComboBox<>(styles);
        styleList.setSelectedItem(selectedItem.getStyle());

        // fits
        String[] fits = {"Standard", "Loose", "Slim", "Oversized"};
        JComboBox<String> fitList = new JComboBox<>(fits);
        fitList.setSelectedItem(selectedItem.getFit());

        // image upload
        JButton uploadImageButton = new JButton("Upload Image");
        JLabel imagePathLabel = new JLabel("No Image Selected");
        final File[] selectedFile = {null};
        uploadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(editDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = fileChooser.getSelectedFile();
                imagePathLabel.setText(selectedFile[0].getName());
            }
        });

        // labels
        editDialog.add(new JLabel("Name:"));
        editDialog.add(nameField);
        editDialog.add(new JLabel("Brand:"));
        editDialog.add(brandField);
        editDialog.add(new JLabel("Color:"));
        editDialog.add(colorField);
        editDialog.add(new JLabel("Category:"));
        editDialog.add(categoriesList);
        editDialog.add(new JLabel("Price (CAD):"));
        editDialog.add(priceField);
        editDialog.add(new JLabel("Material:"));
        editDialog.add(materialField);
        editDialog.add(new JLabel("Style:"));
        editDialog.add(styleList);
        editDialog.add(new JLabel("Fit:"));
        editDialog.add(fitList);
        editDialog.add(new JLabel("Link:"));
        editDialog.add(linkField);
        editDialog.add(new JLabel("Image:"));
        editDialog.add(uploadImageButton);
        editDialog.add(imagePathLabel);

        // save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new values
                selectedItem.setName(nameField.getText());
                selectedItem.setBrand(brandField.getText());
                selectedItem.setColor(colorField.getText());
                selectedItem.setCategory((String) categoriesList.getSelectedItem());
                selectedItem.setPrice(Double.parseDouble(priceField.getText().replace(",", "")));
                selectedItem.setMaterial(materialField.getText());
                selectedItem.setStyle((String) styleList.getSelectedItem());
                selectedItem.setFit((String) fitList.getSelectedItem());
                selectedItem.setLink(linkField.getText());

                //replace image
                if (selectedFile[0] != null) {
                    try {
                        String imagePath = "data/img/" + selectedItem.getId() + ".png";
                        File destination = new File(imagePath);
                        Files.copy(selectedFile[0].toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        destination.setReadable(true, false);
                        destination.setWritable(true, false);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }}

                //rewrite csv with changes
                writecsv(clothingItemList);
                //close dialog
                editDialog.dispose();
            }
        });

        editDialog.add(saveButton);
        editDialog.pack();
        editDialog.setLocationRelativeTo(frame);
        editDialog.setVisible(true);
    }

//given a list of clothing items rewrite the csv at data/CatalogData.csv
    private void writecsv(List<ClothingItem> items) {
        String filePath = "data/CatalogData.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            // header
            writer.write("ID,Product Name,Brand,Color,Category,Price (CAD),Material,Style,Fit,Link\n");

            // write each clothing item
            for (ClothingItem item : items) {
                writer.write(item.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
