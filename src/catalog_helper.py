import csv


#called when need to print whole database
def printcsv():
  with open('data\catalog_database.csv', mode ='r') as csvfile:
    database = csv.reader(csvfile)
    for lines in database:
      print(lines)

