import catalog_helper
import argparse

def main ():
    # parser = argparse.ArgumentParser()
    # parser.add_argument("filename", nargs='1', metavar='file', type='str', help="File to be read")
    # args = parser.parse_args()
    
    catalog_helper.printcsv()
    catalog_helper.edit_csv_entry()
    input = 0
    while(true):
        if(input == 6):
            break
        else:
            print("1. View ID description")
            print("2. Add to file")
            print("3. Edit file")
            

    
