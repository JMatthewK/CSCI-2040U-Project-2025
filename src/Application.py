from catalog_helper import *
import argparse
import pandas as pd
# parser = argparse.ArgumentParser()
# parser.add_argument("filename", nargs='1', metavar='file', type='str', help="File to be read")
# args = parser.parse_args()
running = True

while(running):
    print("What would you like to do?")
    print("1. Add to Catalog")
    print("2. Edit Item")
    print("3. View Description of Item")
    user_input = input("")

    if(user_input == "1"):
        add()
    elif(user_input == "2"):
        edit_csv_entry()
    elif(user_input == "3"): 
        view_description()
    elif(user_input == -1):
        running = False;
    else:
        print("invalid option")

    