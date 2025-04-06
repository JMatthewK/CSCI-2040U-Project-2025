# CSCI-2040U-Project-2025

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


**Summary**: “CTLG.” is a clothing catalogue application with access to a database of clothing from different brands. A user will be able to add or delete items from the catalogue, search for items, and apply various filters. This functionality ensures a tailored and efficient shopping experience, helping users discover clothing that matches their preferences and needs

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
![screenshot](homepage.png)
![screenshot](catalog.png)

#### Features

- View the full catalogue
- Add, delete and edit clothing items from the catalogue
- Filter items by various tags, and a search bar to find items by their name
- Make an account and save your favourite items
