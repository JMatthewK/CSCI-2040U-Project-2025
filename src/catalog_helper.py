import pandas as pd

def edit_csv_entry():
    df = pd.read_csv('data/catalog_database.csv')
    
    id = input("What is the ID of the entry to edit: ")
    row_index = df[df['Id'] ==  (id)].index
    if not row_index.empty:
        new_name = input("What should the name be: ")
        new_description = input("What should the description be: ")
        df.loc[row_index, "Name"] = new_name
        df.loc[row_index, "Description"] = new_description
        
        df.to_csv('data/catalog_database.csv', index=False)
        print("Entry Updated")
    else:
        print("No entry with ID")
            
edit_csv_entry()