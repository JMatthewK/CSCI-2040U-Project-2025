import pandas as pd

def edit_csv_entry():
    df = pd.read_csv('data/catalog_database.csv')
    
    id = input("What is the ID of the entry to edit: ")
    print(id)
            
            
edit_csv_entry()