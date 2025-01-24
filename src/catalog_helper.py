import pandas as pd
import csv

def edit_csv_entry():
    df = pd.read_csv('data/catalog_database.csv')
    
    id = input("What is the ID of the entry to edit: ")
    print(id)
            
            
edit_csv_entry()


#called when need to print whole database
def printcsv():
  with open('data\catalog_database.csv', mode ='r') as csvfile:
    database = csv.reader(csvfile)
    for lines in database:
      print(lines)

