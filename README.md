# CSCI-2040U-Project-2025
![icon2](https://github.com/user-attachments/assets/79d3a456-1a4e-4ddb-8728-03fbb7b6e0e6)


# CTLG.
## Project Overview
### Team Members:
Matthew Kassapian, Russell Manalo, Adrian Fudge, Mavrick Egan, Isabella Smith

## Table of Contents
- [Project Overview](#project-overview)
  - [Team Members](#team-members)
  - [Summary](#summary)
  - [High-level goals](#high-level-goals)
  - [Team Agreements and Elicitation Documents](#team-agreements-and-elicitation-documents)
- [Setup Instructions](#setup-instructions)
  - [1. Clone the repository](#1-clone-the-repository)
  - [2. Compile and run](#2-compile-and-run)
  - [3. Usage](#3-usage)
    - [Features](#features)
- [User Documentation](#user-documentation)
  - [Features](#features-1)
  - [How to Use](#how-to-use)
    - [Launching the app](#launching-the-app)
    - [Logging In](#logging-in)
    - [Creating an Account](#creating-an-account)
    - [Browsing](#browsing)
    - [Filter](#filter)
    - [Searching](#searching)
    - [Favouriting Items](#favouriting-items)
    - [Admin Features](#admin-features)
      - [Adding/Editing/Deleting Items](#addingeditingdeleting-items)
- [Developer Documentation](#developer-documentation)
  - [Project Structure](#project-structure)
  - [Testing](#testing)
  - [Technologies Used](#technologies-used)
  - [Future Improvements](#future-improvements)



## Summary

**“CTLG.”** is a clothing catalogue application with access to a database of clothing from different brands. A user will be able to add or delete items from the catalogue, search for items, and apply various filters. This functionality ensures a tailored and efficient shopping experience, helping users discover clothing that matches their preferences and needs

Fashion and clothing has a big presence in culture, social media and in the market meaning that it will possess a large amount of users. Some key characteristics of our domain include:
1. Brands: A curated selection of both well-known and emerging clothing brands.
2. Colours: A versatile palette, from classic neutrals to bold, trendy hues.
3. Costs: A range of price points, from affordable everyday wear to premium designer pieces.
4. Materials: High-quality fabrics like cotton, polyester, linen, and sustainable options for comfort and durability.
5. Styles: Sporty, casual, formal, loungewear, and relaxed fits to suit every occasion and lifestyle.
6. Types of Clothing: A wide variety of garments, including shirts, sweaters, shorts, pants, dresses, jackets, and more.

### High-level goals:
1. High-Quality Product: The product functions and exceeds all requirements.
2. Friendly UI Design: The user interface is intuitive for all users.
3. Fast Access to Catalog: Reduce user waiting time.
4. Clean Data Files: CSV files are easily read with proper categorization.
5. Efficient Collaboration: Proper use of VCS, branching and communication.

### Team Agreements and Elicitation Documents
**Project Roles:**
- Project Manager: Isabella Smith
- Technical Manager: Adrian Fudge
- Front-End Lead: Matthew Kassapian
- Back-End Lead: Mavrick Egan
- Software Quality: Russell Manalo
- Developers: All

## Setup Instructions

### 1. Clone the repository

To get the source code, run the following command:

```bash
git clone https://github.com/JMatthewK/CSCI-2040U-Project-2025
cd CSCI-2040U-Project-2025
```

### 2. Compile and run

Compile the program:

```bash
mvn clean install
```

Finally, run the program:

```bash
java -jar target/FinalProject-1.0.jar
```

### 3. Usage

Once the program launches, the UI will display:
![homepage](https://github.com/user-attachments/assets/e70f8244-02ce-4097-a593-f95e1721122d)
![catalogPage](https://github.com/user-attachments/assets/0ea3d5bf-160b-472f-b1ef-9b1d61f5e78b)

## User Documentation

#### Features

- View the full catalogue
- Add, delete and edit clothing items from the catalogue
- Filter items by various tags, and a search bar to find items by their name
- Make an account and save your favourite items

### How to Use
1. **Launching the app.**
  a. Run the commands in the [Setup Instructions](#setup-instructions).
2. **Logging In:** How to log into accounts. <br>
  a. After launching the app, select the log in button. <br>
![Login1](https://github.com/user-attachments/assets/db5b28d9-f5ae-4a00-a51f-ee2d37a73e03)
  b. Put in your username and password, and click "Log In" <br>
![Login2](https://github.com/user-attachments/assets/9f9383c0-25ae-4286-b768-daa7ee823911)
  c. If you would like to just view with limited features, select "Continue as Guest" <br>
3. **Creating an Account:** Creating an user account. <br>
  a. Select the "Register Account" button in the login screen. <br>
![Register1](https://github.com/user-attachments/assets/bca16574-0c1a-4fbf-9db2-6307b8c318c9)
  b. Fill in the information with a username and password to create an account. <br>
4. **Browsing:** Scroll through the catalog. <br>
  a. Click on the "View Catalog" button on the top right of the homepage **after logging in.** <br>
  b. Scroll through the catalog of items to view. <br>
  c. Click on any item to view the item details.
5. **Filter:** Filter catalog using the menu. <br>
  a. Use the left sidebar to select and deselect attributes to filter the catalog with. <br>
![FilterImage](https://github.com/user-attachments/assets/d27784cb-68e7-41bb-a638-e16f0cc452e8)
6. **Searching:** Search through the catalog using search bar. <br>
  a. Use the search bar on the top of the catalog to type and search for items. <br>
![Search1](https://github.com/user-attachments/assets/fe4d3da4-805b-440b-9a77-adea58e39211)
7. **Favouriting Items:** How to favourite items to your account. <br>
  a. Click on any item to open up the item details. <br>
  b. If logged in as a user (guests not included) there will be a button to "Add to Favourite". <br>
  c. View your favourites list by clicking the "Favorites" button on the top right. <br>
ADMIN FEATURES <br>
9. **Adding/Editing/Deleting Items:** How to modify items in the catalog. <br>
  a. If logged in as admin, the catalog will show 2 extra buttons. The first button "Add Item" will allow you to fill out the details of an item, for it to be added to the catalog, The second button "Delete Multiple" will toggle and allow the admin to delete multiple items at once. <br>
![AdminFeature1](https://github.com/user-attachments/assets/f23422ce-038d-41c9-b32c-a4fe30b13175)
  b. If logged in as an admin, when viewing item details you will see 2 extra buttons. The "Edit" button will allow you to change data about that item. The "Delete" button will allow you to remove the selected item from the catalog. <br>
  ![AdminFeature2](https://github.com/user-attachments/assets/57dd94c9-e52e-450b-87c0-adf0dc9fea45)

### Video Tutorial

## Developer Documentation

### Project Structure
```
CSCI-2040U-Project-2025/
├── .idea/                         # IntelliJ project settings
│   └── shelf/                     # Changes
│
├── data/                          # CSV files and UI image assets
│   ├── favorites/                 # Stores user-selected favorites
│   ├── img/                       # App image assets
│   ├── img_backup/                # Backup of image files
│   ├── accounts.csv               # User accounts data
│   ├── CatalogData.csv            # Primary catalog dataset
│   ├── CatalogDataBackup.csv      # Backup of catalog dataset
│   ├── CatalogDataForTesting.csv  # Test dataset for dev purposes
│   └── credentials/               # Stores login credentials in CSVs
│       ├── icon.png, icon2.png, newIcon.png # LOGO Icons
│
├── lib/
│   └── opencsv-4.0.jar            # External library for CSV parsing
│
├── out/                           # Compiled bytecode (auto-generated)
│   └── Account                    # Bytecode output of compiled classes
│
├── src/                           # Java source files
│   └── main/
│       └── java/
│           ├── Account.java              # Handles user account info
│           ├── CatalogApp.java           # Main class, starts the application
│           ├── CatalogController.java    # Manages logic between model and view
│           ├── CatalogViewer.java        # Java Swing UI code
│           ├── ClothingItem.java         # Data object for a clothing item
│           └── CsvParser.java            # Reads/writes CSV data
│
├── resources/                    # Fonts and UI assets
│   └── fonts/
│       └── ArchivoBlack-Regular.ttf     # Custom font for UI
│
├── catalog.png                   # UI screenshot (for README)
├── homepage.png                  # UI screenshot (for README)
├── dependency-reduced-pom.xml   
├── makefile                      # Optional Makefile (if used)
├── pom.xml                       # Maven build file
├── README.md                     
└── .gitignore                    # Git exclusions
```

### Testing
To run the tests for the application, use the following command
```bash
mvn test
```

### Data Information
1. Catalog Data
```csv
ID,Product Name,Brand,Color,Category,Price (CAD),Material,Style,Fit,Link
1,Nike Strike Men's Dri-FIT Short-Sleeve Football,Nike,Black,Shirt,55.0,Polyester,Sport,Slim,https://www.nike.com/ca/t/strike-dri-fit-short-sleeve-football-top-DFnTwg/FN2399-010
2,Nike Primary Men's Dri-FIT Short-Sleeve Versatile Top,Nike,Black,Shirts,68.0,Polyester,Sport,Standard,https://www.nike.com/ca/t/primary-dri-fit-short-sleeve-versatile-top-2wrctV/DV9831-010
...
```
| **Column**     | **Description**                           |
|----------------|-------------------------------------------|
| ID             | Unique identifier for the product         |
| Product Name   | The name of the product                   |
| Brand          | The brand of the product                  |
| Color          | The color of the product                  |
| Category       | The category the product belongs to       |
| Price (CAD)    | The price of the product in CAD           |
| Material       | The material used in the product          |
| Style          | The style or design of the product        |
| Fit            | The fit type (e.g., Slim, Standard)       |
| Link           | URL to the product's page online          |
2. Account Data <br>
Stores user login information, admin status has to be edited in this file. <br>
```csv
Username,Password,isAdmin
admin,adminpass,true
user,userpass,false
...
```
| **Column** | **Description**                     |
|------------|-------------------------------------|
| Username   | User’s login name                   |
| Password   | User’s login password (plaintext)   |
| isAdmin    | true if the user is an admin        |

3. Favorites Data <br>
The "favorites" folder contains csv files that containt he ID number of each clothing item a user has favourited.
4. Images <br>
In the "img" folder there are images that share the same ID number as the respective clothing item they have a picture of.


### Technologies Used
- Java 17
- Java Swing (UI)
- Maven (Build Tool)
- CSV for data storage
- Git & GitHub (Version Control)

### Future Improvements
- Clothing recommendation algorithm
- User profile personalization
- Using remote database instead of local CSV files
