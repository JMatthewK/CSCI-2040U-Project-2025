import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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
    private JPanel favoritesItemsPanel = new JPanel(new GridLayout(0, 4, 20, 20));
    private JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    private JLabel emptyFavoritesLabel = new JLabel();

    // Define buttons used in the class
    private JButton loginButton;
    private JButton logoutButton;
    private JButton deleteButton;
    private JButton openCatalogButton;
    private JButton favouritesButton;
    private JButton exitButton;
    private JButton homepageButton;
    private JButton toggleDeleteButton;


    // Define font used, Custom Font
    String fontPath = "src/resources/fonts/ArchivoBlack-Regular.ttf";
    Font archivoBlackFont = loadCustomFont(fontPath, 12f);

    Font textFont = archivoBlackFont.deriveFont(Font.PLAIN, 12);
    Font headerFont = archivoBlackFont.deriveFont(Font.PLAIN, 18);
    Font buttonFont = archivoBlackFont.deriveFont(Font.PLAIN, 12);

    Color mainColor = new Color(0xfaebd7);
    Color logoColor = new Color(0xe04c2c);
    Color searchButtonColor = new Color(0x61B5D9);
    Color accentColor = new Color(0xE4A08E);

    BufferedImage logoImage = ImageIO.read(new File("data/icon.png"));
    Image scaledLogo = logoImage.getScaledInstance(70, 40, Image.SCALE_SMOOTH);
    ImageIcon logoIcon = new ImageIcon(scaledLogo);
    JLabel headerLogoLabel = new JLabel(logoIcon);

    // Set of different filters to help search for specific clothes
    private Set<String> selectedColors = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private Set<String> selectedMaterials = new HashSet<>();
    private Set<String> selectedStyles = new HashSet<>();
    private Set<String> selectedFits = new HashSet<>();

    // Variable to track login details
    private boolean isLoggedIn = false;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    // 0 for guest, 1 for registered User, 2 for admin
    private int userStatus = 0;

    public int getUserStatus() {
        return userStatus;
    }

    private String currentuser;

    public String getCurrentUser() {
        return currentuser;
    }

    // account list
    private List<Account> accounts = null;

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

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
        initializeToggleDeleteButton();
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
        sidebar.setPreferredSize(new Dimension(150, frame.getHeight()));

        // Creates a cardLayout and mainCardPanel allowing me to switch panels for both sidebar and main section
        mainCardLayout = new CardLayout();
        mainCardPanel = new JPanel(mainCardLayout);

        sideCardLayout = new CardLayout();
        sideCardPanel = new JPanel(sideCardLayout);
        sideCardPanel.setPreferredSize(new Dimension(150, frame.getHeight()));

        // Create title and logo panel and put it on header
        JPanel titlePanel = new JPanel(new FlowLayout());
        setPanelColors(titlePanel, mainColor, mainColor);
        // Add logo and title to the title panel
        titlePanel.add(headerLogoLabel);

        // Make header and navigation buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        setPanelColors(headerPanel, mainColor, mainColor);
        navigationPanel = new JPanel();
        setPanelColors(navigationPanel, mainColor, mainColor);
        FlowLayout navigationLayout = new FlowLayout();
        navigationPanel.setSize(150, headerPanel.getHeight());
        navigationLayout.setHgap(10);
        navigationPanel.setLayout(navigationLayout);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(navigationPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // MainMenu asks us if we want to continue as guest, or login/create account
        JPanel mainMenu = new JPanel();
        setPanelColors(mainMenu, mainColor, mainColor);
        setPanelColors(mainMenuOptions, mainColor, mainColor);
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
        customizeButton(guestButton);

        // login button
        loginButton = new JButton("Login");
        customizeButton(loginButton);
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
        setPanelColors(mainMenuButtonsPanel, mainColor, mainColor);
        mainMenuOptions.add(mainMenuButtonsPanel);

        // Add these options to our mainMenu
        mainMenu.add(mainMenuOptions, BorderLayout.CENTER);


        // add homePage button
        homepageButton = new JButton("HomePage");
        customizeButton(homepageButton);
        homepageButton.addActionListener(e -> {
            mainCardLayout.show(mainCardPanel, "homepage");
            sideCardLayout.show(sideCardPanel, "sidebar");
        });

        // add homePage2 button for the other sidebar
        JButton homepageButton2 = new JButton("HomePage");
        customizeButton(homepageButton2);
        homepageButton2.addActionListener(e -> {
            mainCardLayout.show(mainCardPanel, "homepage");
            sideCardLayout.show(sideCardPanel, "sidebar");
        });

        //display catalog button
        openCatalogButton = new JButton("View Catalogue");
        customizeButton(openCatalogButton);
        openCatalogButton.addActionListener(e -> {
            try {
                clothingItemList = csvParser.parseCsv("data/CatalogData.csv");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            startGUI(clothingItemList);
        });

        // exit the application button
        exitButton = new JButton("Exit");
        customizeButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));

        // Favourites List
        // Favourite Page Panel
      // Favourites List
        // Replace the existing favoritesPanel initialization with this:
// Replace the favorites panel creation with:
        JPanel favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
        setPanelColors(favoritesPanel, mainColor, mainColor);

// Create header
        JLabel favoritesLabel = createHeaderLabel("Your Favorites");
        favoritesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        favoritesLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        favoritesPanel.add(favoritesLabel);

// Add search panel inside a wrapper for spacing
        JPanel searchWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchWrapper.setMaximumSize(new Dimension(600, 50)); // Restrict width
        searchWrapper.add(createSearchPanel(true));
        searchWrapper.setBackground(mainColor);
        favoritesPanel.add(searchWrapper);


// Initialize favorites items panel with proper alignment
        favoritesItemsPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        favoritesItemsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JScrollPane favoritesScrollPane = new JScrollPane(favoritesItemsPanel);
        favoritesScrollPane.setBorder(BorderFactory.createEmptyBorder());
        favoritesScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        favoritesScrollPane.setPreferredSize(new Dimension(600, 300));
        favoritesScrollPane.setBackground(mainColor);

// Add a wrapper panel for the scroll pane to expand properly
        JPanel scrollPaneWrapper = new JPanel(new BorderLayout());
        scrollPaneWrapper.add(favoritesScrollPane, BorderLayout.CENTER);
        scrollPaneWrapper.setBackground(mainColor);
        favoritesPanel.add(scrollPaneWrapper);

// Empty state message
        emptyFavoritesLabel = new JLabel("You haven't added any favorites yet!");
        emptyFavoritesLabel.setFont(headerFont);
        emptyFavoritesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emptyFavoritesLabel.setForeground(Color.GRAY);
        emptyFavoritesLabel.setVisible(false);
        favoritesPanel.add(emptyFavoritesLabel);

// Add toggle remove button with better spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        buttonPanel.add(createToggleRemoveButton());
        favoritesPanel.add(buttonPanel);

        setPanelColors(favoritesPanel, mainColor, mainColor);
        setPanelColors(searchWrapper, mainColor, mainColor);
        setPanelColors(buttonPanel, mainColor, mainColor);
        setPanelColors(scrollPaneWrapper, mainColor, mainColor);
        setPanelColors(favoritesItemsPanel, mainColor, mainColor);
        favoritesScrollPane.getViewport().setBackground(mainColor);

// Update the favoritesButton action listener
        favouritesButton = new JButton("Favorites");
        customizeButton(favouritesButton);
        favouritesButton.addActionListener(e -> {
            List<ClothingItem> favorites = getFavourites();
            updateFavoritesDisplay(favorites);
            mainCardLayout.show(mainCardPanel, "favoritesPanel");
            sideCardLayout.show(sideCardPanel, "sidebar"); // Show the default sidebar
        });
         mainCardPanel.add(favoritesPanel, "favoritesPanel");

        // Add navigation buttons
        navigationPanel.add(homepageButton);
        navigationPanel.add(exitButton);

        // Logout button will logout
        logoutButton = new JButton("Logout");
        customizeButton(logoutButton);
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

        setPanelColors(bottomPanel,mainColor,mainColor);

        // add item button
        JButton addItemButton = new JButton("Add New Item");
        customizeButton(addItemButton);
        addItemButton.addActionListener(e -> {
            addItem();
        });
        bottomPanel.add(addItemButton);


        // CREATE PANEL WHEN FILTERING IMAGES
        // Create filter panel
        JPanel sideFilterPanel = new JPanel();
        setPanelColors(sideFilterPanel, mainColor, mainColor);
        sideFilterPanel.setLayout(new BoxLayout(sideFilterPanel, BoxLayout.Y_AXIS));

        // Add the menu buttons to the filter panel at the top
        menuButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        menuButtonPanel.setBackground(mainColor);
        sideFilterPanel.add(menuButtonPanel);

        // Add the actual filtering buttons
        JLabel filterHeaderLabel = createHeaderLabel("Filter");
        filterHeaderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        filterHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sideFilterPanel.add(filterHeaderLabel);
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

    /**
     * Customize buttons and JLABELS
     */

    private static Font loadCustomFont(String fontPath, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
            return font.deriveFont(size); // Resize font
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Segoe UI", Font.PLAIN, 12); // Fallback font
        }
    }

    public void customizeButton(JButton button) {
        button.setBackground(logoColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(buttonFont);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private JLabel createHeaderLabel(String title){
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(logoColor);
        return headerLabel;
    }

    private JLabel createTextLabel(String text){
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(textFont);
        return textLabel;
    }

    private void setPanelColors(JPanel panel, Color backgroundColor, Color foregroundColor) {
        panel.setBackground(backgroundColor);
        panel.setForeground(foregroundColor);
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
        setPanelColors(searchPanel, mainColor, mainColor);
        JTextField searchField = new JTextField(25);
        JButton searchButton = new JButton("Search"); // Add an icon for style
        customizeButton(searchButton);
        searchButton.setBackground(searchButtonColor);

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

        JLabel searchLabel = createHeaderLabel("Search: ");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        catalogPanel.add(searchPanel, BorderLayout.NORTH);

        // 📌 Image Panel - Spacious layout
        imagePanel = new JPanel(new GridLayout(0, 4, 20, 20)); // 4 columns, 20px padding
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around items
        setPanelColors(imagePanel, mainColor, mainColor);
        updateImagePanel(clothingItemList);

        // Scroll Pane to handle overflow
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        setPanelColors(catalogPanel, mainColor, mainColor);
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
            JPanel loginPanel = new JPanel(new GridLayout(6,1, 0, 0));
            setPanelColors(loginPanel, mainColor, mainColor);
            JPanel usernamePanel = new JPanel(new GridLayout(2,1));
            setPanelColors(usernamePanel, mainColor, mainColor);
            JPanel passwordPanel = new JPanel(new GridLayout(2,1));
            setPanelColors(passwordPanel, mainColor, mainColor);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400,600);
            frame.setLocationRelativeTo(null);
            frame.add(loginPanel);
            frame.setResizable(false);
            // check accounts
            try {
                accounts = csvParser.parseAccount("data/accounts.csv");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    
            // GUI Components
            JLabel userLabel = createTextLabel("Username:");
            JTextField userField = new JTextField(15);
            Rectangle r = new Rectangle(75,50);
            userField.setBounds(r);
    
            JLabel passLabel = createTextLabel("Password:");
            JPasswordField passField = new JPasswordField(15);
            passField.setBounds(r);
    
            JButton loginButton = new JButton("Login");
            customizeButton(loginButton);
            JLabel resultLabel = new JLabel("");
            JButton registerButton = new JButton("Register Account");
            customizeButton(registerButton);
            registerButton.addActionListener(e -> {
                frame.dispose();
                register();
            });
    
            // Button response and override for action of button
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = userField.getText();
                    String password = new String(passField.getPassword());
            
                    if (authenticate(username, password)) {
                        resultLabel.setText("Access granted!");
                        resultLabel.setForeground(Color.BLUE);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        frame.setVisible(false);
                        frame.dispose();
                    } else {
                        resultLabel.setText("Invalid credentials.");
                        resultLabel.setForeground(logoColor);
                    }
            
                    // Run update UI to check for changes
                    updateUI();
                }
            });
            
    
            // Adding the components to the frame and display
            usernamePanel.add(userLabel);
            usernamePanel.add(userField);
            passwordPanel.add(passLabel);
            passwordPanel.add(passField);
    
            JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            setPanelColors(loginButtonPanel, mainColor, mainColor);
            loginButtonPanel.add(loginButton);
            loginButtonPanel.add(resultLabel);
    
            JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            setPanelColors(signupPanel, mainColor, mainColor);
            signupPanel.add(registerButton);
    
            loginPanel.add(headerLogoLabel);
            loginPanel.add(usernamePanel);
            loginPanel.add(passwordPanel);
            loginPanel.add(loginButtonPanel);
            loginPanel.add(createTextLabel("Don't have an account? Sign up here:"));
            loginPanel.add(signupPanel);
    
    
            frame.setVisible(true);
    
        }

    // Register a new account (new account gets added to a CSV account database)
    public void register() {
        JFrame frame = new JFrame("Register Account");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setSize(400,600);
        frame.setResizable(false);

        // GUI Components
        JLabel newUserLabel = createTextLabel("Username:");
        JTextField newUserField = new JTextField(15);
        Rectangle r = new Rectangle(75,50);
        newUserField.setBounds(r);

        JLabel newPassLabel = createTextLabel("Password:");
        JPasswordField newPassField = new JPasswordField(15);
        newPassField.setBounds(r);

        JButton registerButton = new JButton("Register Account");
        customizeButton(registerButton);
        JLabel resultLabel = new JLabel("");

        JPanel registerPanel = new JPanel(new GridLayout(5,1));
        JPanel usernamePanel = new JPanel(new GridLayout(2,1));
        JPanel passwordPanel = new JPanel(new GridLayout(2,1));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setPanelColors(registerPanel, mainColor, mainColor);
        setPanelColors(usernamePanel, mainColor, mainColor);
        setPanelColors(passwordPanel, mainColor, mainColor);
        setPanelColors(buttonPanel, mainColor, mainColor);

        usernamePanel.add(newUserLabel);
        usernamePanel.add(newUserField);
        passwordPanel.add(newPassLabel);
        passwordPanel.add(newPassField);
        buttonPanel.add(registerButton);
        buttonPanel.add(resultLabel);

        registerPanel.add(headerLogoLabel);
        registerPanel.add(usernamePanel);
        registerPanel.add(passwordPanel);
        registerPanel.add(buttonPanel);

        frame.add(registerPanel);
        frame.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = newUserField.getText();
                String password = new String(newPassField.getPassword());
                boolean userTaken = false;

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

                try {
                    accounts = csvParser.parseAccount("data/accounts.csv");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                for(int i = 0; i < accounts.size(); i++) {
                    if (username.equals(accounts.get(i).getUsername())) {
                        resultLabel.setText("Username taken.");
                        resultLabel.setForeground(logoColor);
                        userTaken = true;
                    }
                }

                if (userTaken != true) {
                    String[] accString = {username,password,"false"};
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
            }
        });

    }

    public boolean authenticate(String username, String password) {
        String hashedInput = hashPassword(password);
        for (Account acc : accounts) {
            if (username.equals(acc.getUsername()) && hashedInput.equals(hashPassword(acc.getPassword()))) {
                userStatus = acc.isAdmin() ? 2 : 1;
                currentuser = acc.getUsername();
                isLoggedIn = true;
                return true;
            }
        }
        return false;
    }

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

        String[] fits = {"All", "Standard", "Loose", "Slim", "Oversized"};
        JComboBox<String> fitList = new JComboBox<>(fits);
        //Upload image using jfilechooser
        JButton uploadImageButton = new JButton("Upload Image");
        customizeButton(uploadImageButton);
        JLabel imagePathLabel = createTextLabel("No Image Selected");
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
        customizeButton(addButton);
        addButton.addActionListener(e -> {
            double itemPrice;
            try {
                itemPrice = Double.parseDouble(price.getText().replaceAll(",", ""));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Try again with a price", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Try Again With a name ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int size = clothingItemList.size()+1;
            JOptionPane.showMessageDialog(frame, "Adding Item with ID Number: "+size, "Success", JOptionPane.INFORMATION_MESSAGE);

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
            SwingWorker<Void, Void> imageSaver = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    if (selectedFile[0] != null) {
                        String imagePath = "data/img/" + clothing.getId() + ".png";
                        File destination = new File(imagePath);

                        // Ensure directory exists
                        if (!destination.getParentFile().exists()) {
                            destination.getParentFile().mkdirs();
                        }

                        // Copy the file
                        Files.copy(selectedFile[0].toPath(), destination.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);

                        // Set permissions
                        destination.setReadable(true, false);
                        destination.setWritable(true, false);

                        // Force the image to load
                        ImageIcon icon = new ImageIcon(imagePath);
                        icon.getImage().flush(); // Ensure image is loaded
                        clothing.setImage(icon);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    // Update UI after image is saved
                    updateImagePanel(clothingItemList);
                }
            };
            imageSaver.execute();

            // Show catalog immediately (will show placeholder first)
            updateImagePanel(clothingItemList);
            mainCardLayout.show(mainCardPanel, "catalogPanel");

        });

        addPanel.add(createTextLabel("*Name:"));
        addPanel.add(name);
        addPanel.add(createTextLabel("Brand:"));
        addPanel.add(brand);
        addPanel.add(createTextLabel("Color:"));
        addPanel.add(color);
        addPanel.add(createTextLabel("Category:"));
        addPanel.add(categoriesList);
        addPanel.add(createTextLabel("*Price:"));
        addPanel.add(price);
        addPanel.add(createTextLabel("Material:"));
        addPanel.add(material);
        addPanel.add(createTextLabel("Style:"));
        addPanel.add(styleList);
        addPanel.add(createTextLabel("Fit:"));
        addPanel.add(fitList);
        addPanel.add(createTextLabel("Link:"));
        addPanel.add(link);

        addPanel.add(uploadImageButton); addPanel.add(imagePathLabel);
        addPanel.add(addButton);


        mainCardPanel.add(addPanel, "addPanel");
        mainCardLayout.show(mainCardPanel, "addPanel");

        frame.revalidate();
        frame.repaint();
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
        setPanelColors(filterPanel,mainColor,mainColor);
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));

        // Color selection panel (using colored buttons instead of text)
        JPanel colorPanel = new JPanel();
        setPanelColors(colorPanel, mainColor, mainColor);
        TitledBorder border = BorderFactory.createTitledBorder("Color");
        border.setTitleFont(textFont);
        border.setTitleColor(logoColor);
        String[] colors = {"Blue", "Grey", "Brown", "Green", "Beige", "Pink", "Red", "Orange", "Purple"};

        for (String color : colors) {
            JButton colorButton = new JButton();
            customizeButton(colorButton);
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
        setPanelColors(categoryPanel, mainColor, mainColor);
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
        String[] fits = {"All", "Standard", "Loose", "Slim", "Oversized"};
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
            case "red": return logoColor;
            case "orange": return Color.ORANGE;
            case "purple": return new Color(128, 0, 128); // Purple
            default: return Color.BLACK;
        }
    }

    /** Creates a panel with checkboxes for multiple selection */
    private JPanel createCheckboxPanel(String filterType, String[] options) {
        JPanel panel = new JPanel();
        setPanelColors(panel, mainColor, mainColor);
        TitledBorder border = BorderFactory.createTitledBorder(filterType);
        border.setTitleFont(textFont);
        border.setTitleColor(logoColor);
        panel.setBorder(border);

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
        setPanelColors(panel, mainColor, mainColor);
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleFont(textFont);
        border.setTitleColor(logoColor);
        panel.setBorder(border);
        panel.add(component);
        return panel;
    }


    // create button for current filtertype
    private JPanel createFilterButtons(String filterType, String[] options) {
        JPanel panel = new JPanel();
        setPanelColors(panel, mainColor, mainColor);
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
                }
                else if(filterValue.equals("All")){
                    selectedCategories.add(filterValue);
                    // ADD ALL CATEGORIES TO THE FILTER IF ALL
                    String[] categories = {"All", "Shirts", "Shorts", "Pants", "Sweaters", "Skirts"};
                    for(int i = 1; i < categories.length; i++){
                        selectedCategories.add(categories[i]);
                    }
                }
                else {
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
                }
                else if(filterValue.equals("All")){
                    String[] fits = {"All", "Standard", "Loose", "Slim", "Oversized"};
                    for(int i = 1; i < fits.length; i++){
                        selectedFits.add(fits[i]);
                    }
                }
                else {
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
    private boolean showDeleteButtons = false; // Track delete button visibility
    private void initializeToggleDeleteButton() {
        toggleDeleteButton = new JButton("Delete Multiple");
        toggleDeleteButton.addActionListener(e -> {
            showDeleteButtons = !showDeleteButtons;
            toggleDeleteButton.setSelected(showDeleteButtons); // Maintain button state
            updateImagePanel(clothingItemList); // Refresh UI
        });
        customizeButton(toggleDeleteButton);
    }

    //updates the panel of images
    private void updateImagePanel(List<ClothingItem> items) {
        imagePanel.removeAll();
        // Add toggle delete button only if admin
        if (userStatus == 2) {
            buttonPanel.add(toggleDeleteButton);
            bottomPanel.add(buttonPanel);
        }

        for (ClothingItem item : items) {
            ImageIcon icon = item.getImage();
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImg);

            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());
            Color defaultColor = itemPanel.getBackground();
            Color hoverColor = new Color(173, 216, 230); // Light Blue

            // Image label
            JLabel imageLabel = new JLabel(icon, JLabel.CENTER);
            imageLabel.setToolTipText("Click for more details on " + item.getName());
            imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Panel for each clothing item (card-like structure)
            itemPanel = new JPanel();
            itemPanel.setLayout(new BorderLayout());
            itemPanel.setPreferredSize(new Dimension(180, 220)); // Adjust for spacing
            itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2)); // Light gray border

            // Item name label
            JLabel nameLabel = createTextLabel(item.getName());
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            nameLabel.setForeground(Color.DARK_GRAY);

            // Mouse effect (border highlight)
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        ImageIcon icon = new ImageIcon(scaledImg);
                        showItemDetails(item, icon);
                    }
                }
            });

            // Add delete button if in delete mode and user is admin
            if (showDeleteButtons && userStatus == 2) {
                JButton deleteButton = new JButton("Delete");
                customizeButton(deleteButton);
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to delete " + item.getName() + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteItem(item);
                    }
                });
                itemPanel.add(deleteButton, BorderLayout.NORTH);
            }

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
    private void showItemDetails(ClothingItem item, ImageIcon icon) {
        //create new box to show information
        JDialog dialog = new JDialog(frame, "Item Details", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(frame);

        //show most details can add more if want
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JPanel informationPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        JLabel imageLabel = new JLabel(icon, JLabel.CENTER);
        detailsPanel.add(imageLabel, BorderLayout.NORTH);
        setPanelColors(detailsPanel, mainColor, mainColor);
        informationPanel.add(createTextLabel("ID: " + item.getId()));
        informationPanel.add(createTextLabel("Name: " + item.getName()));
        informationPanel.add(createTextLabel("Brand: " + item.getBrand()));
        informationPanel.add(createTextLabel("Price: $" + item.getPrice()));
        informationPanel.add(createTextLabel("Contains Material: " + item.getMaterial()));
        informationPanel.add(createTextLabel("Fit: " + item.getFit()));
        setPanelColors(informationPanel, mainColor, mainColor);
        detailsPanel.add(informationPanel, BorderLayout.CENTER);
        dialog.add(detailsPanel);

        //create button to allow user to edit
        JButton editButton = new JButton("Edit");
        customizeButton(editButton);
        //favourite button
        JButton addfavouriteButton = new JButton("Add to Favourite");
        customizeButton(addfavouriteButton);

        addfavouriteButton.addActionListener(new ActionListener() {
            //call favourite function
            @Override
            public void actionPerformed(ActionEvent e) {
                writeFavourite(item);
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
        customizeButton(deleteButton);
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
        setPanelColors(buttonPanel, mainColor, mainColor);

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
        String[] fits = {"All", "Standard", "Loose", "Slim", "Oversized"};
        JComboBox<String> fitList = new JComboBox<>(fits);
        fitList.setSelectedItem(selectedItem.getFit());

        // image upload
        JButton uploadImageButton = new JButton("Upload Image");
        customizeButton(uploadImageButton);
        JLabel imagePathLabel = createTextLabel("No Image Selected");
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
        editDialog.add(createTextLabel("Name:"));
        editDialog.add(nameField);
        editDialog.add(createTextLabel("Brand:"));
        editDialog.add(brandField);
        editDialog.add(createTextLabel("Color:"));
        editDialog.add(colorField);
        editDialog.add(createTextLabel("Category:"));
        editDialog.add(categoriesList);
        editDialog.add(createTextLabel("Price (CAD):"));
        editDialog.add(priceField);
        editDialog.add(createTextLabel("Material:"));
        editDialog.add(materialField);
        editDialog.add(createTextLabel("Style:"));
        editDialog.add(styleList);
        editDialog.add(createTextLabel("Fit:"));
        editDialog.add(fitList);
        editDialog.add(createTextLabel("Link:"));
        editDialog.add(linkField);
        editDialog.add(createTextLabel("Image:"));
        editDialog.add(uploadImageButton);
        editDialog.add(imagePathLabel);

        // save button
        JButton saveButton = new JButton("Save");
        customizeButton(saveButton);
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
    public List<ClothingItem> getFavourites() {
        List<ClothingItem> favoriteItems = new ArrayList<>();
        //empty if no user
        if (currentuser == null || currentuser.isEmpty()) {
            return favoriteItems;
        }
        File favoritesFile = new File("data/favorites/" + currentuser + "_favorites.csv");
        //if doesnt exist empty
        if (!favoritesFile.exists()) {
            return favoriteItems;
        }
        try {
            //get all favourites of the user
            List<String> favoriteIds = Files.readAllLines(favoritesFile.toPath());
            //get each line
            for (String idStr : favoriteIds) {
                //get the current id
                int id = Integer.parseInt(idStr.trim());
                ClothingItem item = findItemById(id);
                //if item doesn't exist already add it
                if (item != null) {
                    favoriteItems.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return favoriteItems;
    }

    public void writeFavourite(ClothingItem item) {
        // favourites just in case
        File favoritesDir = new File("data/favorites");
        if (!favoritesDir.exists()) {
            favoritesDir.mkdirs();
        }
        //file for user
        File favoritesFile = new File("data/favorites/" + currentuser + "_favorites.csv");

        try {
            // Check if already exist in the file
            if (favoritesFile.exists()) {
                List<String> existingFavorites = Files.readAllLines(favoritesFile.toPath());
                if (existingFavorites.contains(String.valueOf(item.getId()))) {
                    JOptionPane.showMessageDialog(frame, "item is already in your favorites");
                    return;
                }
            }

            // write the new item to csv if doesn't exist
            FileWriter writer = new FileWriter(favoritesFile, true);
            writer.append(String.valueOf(item.getId())).append("\n");
            writer.close();

            JOptionPane.showMessageDialog(frame, "item added to favorites");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error favoriting");
        }
    }
    private JPanel createSearchPanel(boolean isFavorites) {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(mainColor);

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setPreferredSize(new Dimension(200, 30));

        JButton searchButton = new JButton("Search");
        customizeButton(searchButton);
        searchButton.setBackground(new Color(50, 150, 250));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            List<ClothingItem> itemsToSearch = isFavorites ? getFavourites() : clothingItemList;
            List<ClothingItem> filteredList = new ArrayList<>();

            for (ClothingItem item : itemsToSearch) {
                if (item.getName().toLowerCase().contains(query) ||
                        item.getBrand().toLowerCase().contains(query) ||
                        item.getCategory().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }

            if (isFavorites) {
                updateFavoritesDisplay(filteredList);
            } else {
                updateImagePanel(filteredList);
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        return searchPanel;
    }

    private void updateFavoritesDisplay(List<ClothingItem> favorites) {
        favoritesItemsPanel.removeAll(); // Clear previous items

        if (favorites == null || favorites.isEmpty()) {
            emptyFavoritesLabel.setVisible(true);
            favoritesItemsPanel.setVisible(false);
        } else {
            emptyFavoritesLabel.setVisible(false);
            favoritesItemsPanel.setVisible(true);

            for (ClothingItem item : favorites) {
                if (item == null) continue;

                ImageIcon icon = item.getImage();
                if (icon.getIconWidth() <= 0) {
                    icon = new ImageIcon("data/img/placeholder.png");
                }

                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);

                JPanel itemPanel = getjPanel(item, icon, showRemoveButtons); // Pass remove button visibility
                favoritesItemsPanel.add(itemPanel);
            }
        }

        favoritesItemsPanel.revalidate();
        favoritesItemsPanel.repaint();
    }
    private boolean showRemoveButtons = false; // Track remove button visibility

    private JButton createToggleRemoveButton() {
        JButton toggleRemoveButton = new JButton("Remove from Favorites");
        toggleRemoveButton.addActionListener(e -> {
            showRemoveButtons = !showRemoveButtons;
            updateFavoritesDisplay(getFavourites()); // Refresh the UI to show/hide buttons
        });
        customizeButton(toggleRemoveButton);
        return toggleRemoveButton;
    }


    private JPanel getjPanel(ClothingItem item, ImageIcon icon, boolean showRemove) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setPreferredSize(new Dimension(180, 220));
        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        JLabel imageLabel = new JLabel(icon, JLabel.CENTER);
        imageLabel.setToolTipText("Click for more details on " + item.getName());
        imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showItemDetails(item, icon);
            }
        });
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(new Color(173, 216, 230)); // Light Blue
            }

            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(new Color(240, 240, 240)); // Back to normal
            }
        });


        JLabel nameLabel = new JLabel(item.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(Color.DARK_GRAY);

        JButton removeButton = new JButton("Remove");
        customizeButton(removeButton);
        removeButton.setVisible(showRemove); // Control visibility
        removeButton.addActionListener(e -> removeFromFavorites(item));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(removeButton);

        itemPanel.add(imageLabel, BorderLayout.CENTER);
        itemPanel.add(nameLabel, BorderLayout.SOUTH);
        itemPanel.add(buttonPanel, BorderLayout.NORTH);
        return itemPanel;
    }


    private void removeFromFavorites(ClothingItem item) {
        File favoritesFile = new File("data/favorites/" + currentuser + "_favorites.csv");
        if (favoritesFile.exists()) {
            try {
                List<String> lines = Files.readAllLines(favoritesFile.toPath());
                List<String> updatedLines = new ArrayList<>();

                for (String line : lines) {
                    if (!line.trim().equals(String.valueOf(item.getId()))) {
                        updatedLines.add(line);
                    }
                }

                Files.write(favoritesFile.toPath(), updatedLines);
                updateFavoritesDisplay(getFavourites()); // Refresh the display

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                        "Error removing from favorites",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //looks cool
    public void deleteFavourite(ClothingItem item) {
        List<ClothingItem> currentfavourites = getFavourites();
        currentfavourites.remove(findItemById(item.getId()));
        for (ClothingItem currentitem: currentfavourites) {
            writeFavourite(currentitem);
        }
    }


}
