import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
import java.util.concurrent.Flow;

import com.opencsv.CSVWriter;
import com.formdev.flatlaf.FlatLightLaf;

public class CatalogViewer {
    // Parse the CSV file at beginning as opposed to parsing it multiple times (for quicker load times)
    CsvParser csvParser = new CsvParser();
    private List<ClothingItem> clothingItemList = csvParser.parseCsv("data/CatalogData.csv");
    private JFrame frame;
    private JPanel imagePanel;
    private CardLayout mainCardLayout;
    private CardLayout sideCardLayout;
    private JPanel mainCardPanel;
    private JPanel sideCardPanel;
    private JPanel mainMenu;
    private JPanel catalogPanel = new JPanel(new BorderLayout());
    private JPanel bottomPanel = new JPanel(new FlowLayout());
    private JPanel navigationPanel = new JPanel();
    private JPanel mainMenuOptions = new JPanel(new GridLayout(4,1));
    private JPanel menuButtonPanel = new JPanel(new FlowLayout());
    private JPanel mainMenuButtonsPanel = new JPanel(new FlowLayout());

    // Define buttons used in the class
    private JButton loginButton;
    private JButton logoutButton;
    private JButton deleteButton;
    private JButton openCatalogButton;
    private JButton favouritesButton;
    private JButton exitButton;
    private JButton homepageButton;

    // Set of different filters to help search for specific clothes
    private Set<String> selectedColors = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedMaterials = new HashSet<>();
    private Set<String> selectedStyles = new HashSet<>();
    private Set<String> selectedFits = new HashSet<>();

    // Variable to track login details
    private boolean isLoggedIn = false;
    // 0 for guest, 1 for registered User, 2 for admin
    private int userStatus = 0;


    // account list
    private List<Account> accounts = null;

    public CatalogViewer() throws IOException {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Create main window for the program
        frame = new JFrame("CTLG.");

        // Give the frame a logo
        // Load logo image
        ImageIcon icon = new ImageIcon("data/newIcon.png"); // Replace with your image path
        frame.setIconImage(icon.getImage());

        // Set the screensize, and exit condition
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenSize.width, screenSize.height); // Set size of application to size of screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Automaticaly maximize screen
    }

    public CatalogViewer(List<ClothingItem> clothingItemList) throws IOException {
        this.clothingItemList = clothingItemList;
    }

    // main menu
    public void startMainMenu() throws IOException {
        // Create a main menu panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create mainMenu that displays "view catalog" "login" "exit"
        mainMenu = new JPanel();

        // Create Sidebar panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new FlowLayout());
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(150, frame.getHeight()));

        // Creates a cardLayout and mainCardPanel allowing me to switch panels for both sidebar and main section
        mainCardLayout = new CardLayout();
        mainCardPanel = new JPanel(mainCardLayout);

        sideCardLayout = new CardLayout();
        sideCardPanel = new JPanel(sideCardLayout);
        sideCardPanel.setPreferredSize(new Dimension(150, frame.getHeight()));

        // Create title and logo panel and put it on header
        JPanel titlePanel = new JPanel(new FlowLayout());
        BufferedImage logoImage = ImageIO.read(new File("data/icon.png"));
        Image scaledLogo = logoImage.getScaledInstance(70, 40, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(scaledLogo);
        JLabel headerLogoLabel = new JLabel(logoIcon);
        // Add logo and title to the title panel
        titlePanel.add(headerLogoLabel);

        // Make header and navigation buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        navigationPanel = new JPanel();
        FlowLayout navigationLayout = new FlowLayout();
        navigationPanel.setSize(150, headerPanel.getHeight());
        navigationLayout.setHgap(10);
        navigationPanel.setLayout(navigationLayout);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(navigationPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // MainMenu asks us if we want to continue as guest, or login/create account
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new BorderLayout()); // 2 buttons, 1 column

        // Craete the MainMenu
        // Create an empty label to create a gap in the first row
        JLabel emptyLabel = new JLabel("");
        mainMenuOptions.add(emptyLabel);

        // Make a new logo and add it to the mainMenuOptions
        Image mainMenuScaledLogo = logoImage.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        ImageIcon mainMenuLogoIcon = new ImageIcon(mainMenuScaledLogo);
        JLabel mainMenuLogoLabel = new JLabel(mainMenuLogoIcon);

        mainMenuOptions.add(mainMenuLogoLabel);

        // Make the buttons to control the users login status and add it to our mainmenu options
        JButton guestButton = new JButton("Continue as Guest");

        // login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            login();
        });

        mainMenuButtonsPanel.add(loginButton);

        guestButton.addActionListener(e -> {
            userStatus = 0;
            isLoggedIn = true;
            updateUI();
        });

        mainMenuButtonsPanel.add(guestButton);

        mainMenuOptions.add(mainMenuButtonsPanel);

        // Add these options to our mainMenu
        mainMenu.add(mainMenuOptions, BorderLayout.CENTER);


        // add homePage button
        homepageButton = new JButton("HomePage");
        homepageButton.addActionListener(e -> {
            mainCardLayout.show(mainCardPanel, "homepage");
            sideCardLayout.show(sideCardPanel, "sidebar");
        });

        // add homePage2 button for the other sidebar
        JButton homepageButton2 = new JButton("HomePage");
        homepageButton2.addActionListener(e -> {
            mainCardLayout.show(mainCardPanel, "homepage");
            sideCardLayout.show(sideCardPanel, "sidebar");
        });

        //display catalog button
        openCatalogButton = new JButton("View Catalogue");
        openCatalogButton.addActionListener(e -> {
            startGUI(clothingItemList);
        });

        // exit the application button
        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // Favourites List
        // Favourite Page Panel
        JPanel favouritesPanel = new JPanel(new BorderLayout());
        JLabel favouritesLabel = new JLabel("Favourites");
        favouritesPanel.add(favouritesLabel);
        mainCardPanel.add(favouritesPanel, "favouritesPanel");

        favouritesButton = new JButton("Favourites List");
        favouritesButton.addActionListener(e -> {mainCardLayout.show(mainCardPanel, "favouritesPanel");});

        // Add navigation buttons
        navigationPanel.add(homepageButton);
        navigationPanel.add(exitButton);

        // Logout button will logout
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            isLoggedIn = false;
            userStatus = 0;
            mainCardLayout.show(mainCardPanel, "homepage");
            updateUI();
        });

        // add the homepage panel to the mainCardPanels
        mainCardPanel.add(mainMenu, "homepage");

        sideCardPanel.add(sidebar, "sidebar");

        mainPanel.add(mainCardPanel, BorderLayout.CENTER);

        // BUTTONS FOR ADMINS TO USE TO EDIT CATALOG
        // Bottom panel with delete button
        deleteButton = new JButton("🗑 Delete Item by ID");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        deleteButton.addActionListener(e -> deleteItemById());

        bottomPanel.add(deleteButton);

        // add item button
        JButton addItemButton = new JButton("Add New Item");
        addItemButton.addActionListener(e -> {
            addItem();
        });
        bottomPanel.add(addItemButton);


        // CREATE PANEL WHEN FILTERING IMAGES
        // Create filter panel
        JPanel sideFilterPanel = new JPanel();
        sideFilterPanel.setLayout(new BoxLayout(sideFilterPanel, BoxLayout.Y_AXIS));

        // Add the menu buttons to the filter panel at the top
        menuButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        sideFilterPanel.add(menuButtonPanel);

        // Add the actual filtering buttons
        sideFilterPanel.add(new JLabel("Filter"));
        sideFilterPanel.add(createFilterPanel());

        // add the filter panel to the sidebar
        sideCardPanel.add(sideFilterPanel, "sideFilterPanel");


        // The sidebar should first be the one for the mainmenu
        // Show the main sidebar at first
        sideCardLayout.show(sideCardPanel, "sidebar");

        // Show homepage first
        mainCardLayout.show(mainCardPanel, "homepage");

        // Add the mainPanel to the frame
        frame.getContentPane().add(mainPanel);

        // Show Frame
        frame.setVisible(true);


    }

    public void updateUI(){
        if(isLoggedIn){
            // Edit top right
            // add catalog if they're logged in someway
            navigationPanel.removeAll();
            navigationPanel.add(openCatalogButton);
            // only add favourites button if they're a registered user
            if(userStatus >= 1){
                navigationPanel.add(favouritesButton);
            }
            navigationPanel.add(homepageButton);
            navigationPanel.add(exitButton);
            navigationPanel.add(logoutButton);
            navigationPanel.revalidate();
            navigationPanel.repaint();

            // edit the buttons in the mainmenu
            mainMenuOptions.remove(mainMenuButtonsPanel);
            mainMenuOptions.revalidate();
            mainMenuOptions.repaint();
            System.out.println("LOGIN DONE");



            if(userStatus == 2){
                // Only let user see bottom panel if they're an admin
                catalogPanel.add(bottomPanel, BorderLayout.SOUTH);
                catalogPanel.revalidate();
                catalogPanel.repaint();
            }
        }
        else{
            // Edit top right
            // add catalog if they're logged in someway
            navigationPanel.removeAll();
            navigationPanel.add(homepageButton);
            navigationPanel.add(exitButton);
            navigationPanel.revalidate();
            navigationPanel.repaint();

            mainMenuOptions.add(mainMenuButtonsPanel);
            mainMenuOptions.revalidate();
            mainMenuOptions.repaint();

            // Only let user see bottom panel if they're an admin
            catalogPanel.remove(bottomPanel);
            catalogPanel.revalidate();
            catalogPanel.repaint();
        }
    }

    // method to display the catalog in the same window
    public void startGUI(List<ClothingItem> clothingItemList) {
        catalogPanel.add(sideCardPanel, BorderLayout.WEST);

        // Sidebar filter buttons
        sideCardLayout.show(sideCardPanel, "sideFilterPanel");

        // 💡 Search Bar - More modern look
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField searchField = new JTextField(25);
        JButton searchButton = new JButton("🔍 Search"); // Add an icon for style

        // 🔹 Modern UI tweaks for search field & button
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchButton.setBackground(new Color(50, 150, 250));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            List<ClothingItem> filteredList = new ArrayList<>();
            for (ClothingItem item : clothingItemList) {
                if (item.getName().toLowerCase().contains(query) ||
                        item.getBrand().toLowerCase().contains(query) ||
                        item.getCategory().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
            updateImagePanel(filteredList);
        });

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        catalogPanel.add(searchPanel, BorderLayout.NORTH);

        // 📌 Image Panel - Spacious layout
        imagePanel = new JPanel(new GridLayout(0, 4, 20, 20)); // 4 columns, 20px padding
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around items
        updateImagePanel(clothingItemList);

        // Scroll Pane to handle overflow
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        catalogPanel.add(scrollPane, BorderLayout.CENTER);

        mainCardPanel.add(catalogPanel, "catalogPanel");

        updateUI();
        mainCardLayout.show(mainCardPanel, "catalogPanel");

        frame.revalidate();
        frame.repaint();
    }


    // Login screen
    public void login() {
        // Login window
        JFrame frame = new JFrame("Login System");
        JPanel loginPanel = new JPanel(new GridLayout(4,2, 10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300,200);
        frame.add(loginPanel);
        // check accounts
        try {
            accounts = csvParser.parseAccount("data/accounts.csv");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

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

                for(int i = 0; i < accounts.size(); i++) {
                    if (username.equals(accounts.get(i).getUsername()) && input.equals(hashPassword(accounts.get(i).getPassword()))) {
                        if(accounts.get(i).isAdmin()){
                            // Set isAdmin to true
                            userStatus = 2;
                            System.out.println("User is Admin");
                        }
                        else{
                            userStatus = 1;
                            System.out.println("User is not Admin");
                        }
                        isLoggedIn = true;
                        resultLabel.setText("Access granted!");
                        resultLabel.setForeground(Color.BLUE);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        frame.setVisible(false);
                        frame.dispose();
                    }
                    else {
                        resultLabel.setText("Invalid credentials.");
                        resultLabel.setForeground(Color.RED);
                    }
                }
                // Run update UI to check for changes
                updateUI();
            }
        });

        // Adding the components to the frame and display
        loginPanel.add(userLabel);
        loginPanel.add(userField);
        loginPanel.add(passLabel);
        loginPanel.add(passField);
        loginPanel.add(loginButton);
        loginPanel.add(resultLabel);
        loginPanel.add(registerButton);
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


        mainCardPanel.add(addPanel, "addPanel");
        mainCardLayout.show(mainCardPanel, "addPanel");

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
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        // Color selection panel (using colored buttons instead of text)
        JPanel colorPanel = new JPanel();
        colorPanel.setBorder(BorderFactory.createTitledBorder("Color"));
        String[] colors = {"Blue", "Grey", "Brown", "Green", "Beige", "Pink", "Red", "Orange", "Purple"};

        for (String color : colors) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.setBackground(getColorFromName(color)); // Converts text to actual color
            colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            colorButton.addActionListener(e -> {
                updateSelectedFilters("Color", color);
                colorButton.setBorder(selectedColors.contains(color) ?
                        BorderFactory.createLineBorder(Color.WHITE, 3) :
                        BorderFactory.createLineBorder(Color.BLACK));
            });

            colorPanel.add(colorButton);
        }
        filterPanel.add(colorPanel);

        // Category dropdown
        String[] categories = {"All", "Shirts", "Shorts", "Pants", "Sweaters", "Skirts"};
        JComboBox<String> categoryDropdown = new JComboBox<>(categories);
        categoryDropdown.addActionListener(e -> updateSelectedFilters("Category", (String) categoryDropdown.getSelectedItem()));
        JPanel categoryPanel = wrapInPanel("Category", categoryDropdown);
        filterPanel.add(categoryPanel);

        // Material checkboxes
        String[] materials = {"Cotton", "Polyester", "Nylon"};
        JPanel materialPanel = createCheckboxPanel("Material", materials);
        filterPanel.add(materialPanel);

        // Style checkboxes
        String[] styles = {"Formal", "Casual", "Sport", "Loungewear"};
        JPanel stylePanel = createCheckboxPanel("Style", styles);
        filterPanel.add(stylePanel);

        // Fit dropdown
        String[] fits = {"Standard", "Loose", "Slim", "Oversized"};
        JComboBox<String> fitDropdown = new JComboBox<>(fits);
        fitDropdown.addActionListener(e -> updateSelectedFilters("Fit", (String) fitDropdown.getSelectedItem()));
        JPanel fitPanel = wrapInPanel("Fit", fitDropdown);
        filterPanel.add(fitPanel);

        return filterPanel;
    }

    /** Converts color names to actual Color objects */
    private Color getColorFromName(String color) {
        switch (color.toLowerCase()) {
            case "blue": return Color.BLUE;
            case "grey": return Color.GRAY;
            case "brown": return new Color(139, 69, 19); // Brown
            case "green": return Color.GREEN;
            case "beige": return new Color(245, 245, 220); // Beige
            case "pink": return Color.PINK;
            case "red": return Color.RED;
            case "orange": return Color.ORANGE;
            case "purple": return new Color(128, 0, 128); // Purple
            default: return Color.BLACK;
        }
    }

    /** Creates a panel with checkboxes for multiple selection */
    private JPanel createCheckboxPanel(String filterType, String[] options) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(filterType));

        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            checkBox.addItemListener(e -> {
                if (checkBox.isSelected()) {
                    updateSelectedFilters(filterType, option);
                } else {
                    selectedMaterials.remove(option);
                    applyFilters();
                }
            });
            panel.add(checkBox);
        }
        return panel;
    }

    /** Wraps a dropdown in a titled panel */
    private JPanel wrapInPanel(String title, JComponent component) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component);
        return panel;
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
                selectedCategories.clear();
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
                selectedFits.clear();
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

            // Image label
            JLabel imageLabel = new JLabel(icon, JLabel.CENTER);
            imageLabel.setToolTipText("Click for more details on " + item.getName());
            imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Panel for each clothing item (card-like structure)
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());
            itemPanel.setPreferredSize(new Dimension(180, 220)); // Adjust for spacing
            itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2)); // Light gray border
            itemPanel.setBackground(Color.WHITE);

            // Item name label
            JLabel nameLabel = new JLabel(item.getName(), JLabel.CENTER);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            nameLabel.setForeground(Color.DARK_GRAY);

            // Mouse effect (border highlight)
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        showItemDetails(item);
                    }
                }
            });

            // Add components to the item panel
            itemPanel.add(imageLabel, BorderLayout.CENTER);
            itemPanel.add(nameLabel, BorderLayout.SOUTH);

            // Add the item panel to the main image panel
            imagePanel.add(itemPanel);
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
        detailsPanel.add(new JLabel("Brand: " + item.getBrand()));
        detailsPanel.add(new JLabel("Price: $" + item.getPrice()));
        detailsPanel.add(new JLabel("Contains Material: " + item.getMaterial()));
        detailsPanel.add(new JLabel("Fit: " + item.getFit()));
        dialog.add(detailsPanel);

        //create button to allow user to edit
        JButton editButton = new JButton("Edit");
        //favourite button
        JButton addfavouriteButton = new JButton("Add to Favourite");

        addfavouriteButton.addActionListener(new ActionListener() {
            //call favourite function
            @Override
            public void actionPerformed(ActionEvent e) {
                addfavourite();
            }
        });

        editButton.addActionListener(new ActionListener() {
            //call the edit function and send the current item
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedItem(item);
            }
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            dialog,
                            "Are you sure you want to delete " + item.getName() + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteItem(item);
                        dialog.dispose(); // Close the details window
                    }
                });


        // add to bottom
        mainMenu = new JPanel();
        mainMenu.add(editButton);
        dialog.add(mainMenu, BorderLayout.SOUTH);
        JPanel buttonPanel = new JPanel();

        if(userStatus >= 1){
            buttonPanel.add(addfavouriteButton);
        }
        if(userStatus == 2){
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
        }
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public void addfavourite() {
        JDialog dialog = new JDialog(frame, "Favourite popup");
        JLabel l = new JLabel("Current function doesn't work");
        dialog.add(l);
        dialog.setSize(200, 100);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        return;
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
