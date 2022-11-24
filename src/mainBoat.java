import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class mainBoat implements Serializable {

//---- Initializes keyboard for mainBoat driver class
    public static final Scanner keyboard = new Scanner(System.in);
//---- Creates Enum for mainBoat driver class
    private enum BoatEnum {SAILING, POWER}

//---- Initializes file name for the database that data will get written into or read out from
    private static final String DB_FILE_NAME = "FleetData.db";
//---- Variable to test if there is an argument file provided
    private static final int ARGS_TEST = 1;

    public static void main(String[] args) {

//---- Creates new ArrayList to store Boat objects in
        ArrayList<Boat> localBoatStorage = new ArrayList<>();

//---- Test for if there is a file argument provided, if not then the previously saved database file is loaded
        if (args.length == ARGS_TEST) {
            BufferedReader fromBufferedReader;
            String oneLine;
            String[] boatData;

//---- Attempts to read in data from the csv file provided
            try {
//---- Creates a new BufferedReader to read in individual data values from file
                fromBufferedReader = new BufferedReader(new FileReader(args[0]));
                oneLine = fromBufferedReader.readLine();
//---- Reads in data values until the end of the file is reached
                while(oneLine != null) {
                    boatData = oneLine.split(",");
                    Boat newBoat = new Boat(Boat.BoatEnum.valueOf(boatData[0]), boatData[1], Integer.parseInt(boatData[2]), boatData[3], Integer.parseInt(boatData[4]), Double.parseDouble(boatData[5]), 0);
                    localBoatStorage.add(newBoat);
                    oneLine = fromBufferedReader.readLine();
                }
//---- Closes csv file
                fromBufferedReader.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }
        } else {
//---- If no argument file is provided, run method to load in database values
            loadBoatFile(localBoatStorage);
        }
//---- Calls menu method
        menu(localBoatStorage);
    }

    public static void menu(ArrayList<Boat> boatStorage) {
//---- Constant value so while loop keeps running until X is entered
        Character choice = 'a';

        System.out.println("Welcome to the Fleet Management System\n--------------------------------------");
//---- Menu while loop that allows user to choose multiple menu options
        while(choice != 'X') {
            System.out.print("\n(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = keyboard.nextLine().charAt(0);
            choice = Character.toUpperCase(choice);
            if (choice == 'P') {
                print(boatStorage);
            } else if (choice == 'A') {
                addBoat(boatStorage);
            } else if (choice == 'R') {
                removeBoat(boatStorage);
            } else if (choice == 'E') {
                changeExpense(boatStorage);
            } else if (choice == 'X'){
                exit(boatStorage);
            } else {
                System.out.print("Invalid menu option, try again");
            }
        }
    }

//---- Method to print out the Boat data
    public static void print(ArrayList<Boat> boatStorage) {
        System.out.println("\n Fleet Report");
        double sumPrice = 0;
        double sumExpense = 0;
        int index;
//---- For loop that runs through and prints each Boats data, expense and price totals are added up to print out total expense
        for (index=0; index<boatStorage.size(); ++index) {
            System.out.println(boatStorage.get(index));
            sumPrice += boatStorage.get(index).getPrice();
            sumExpense += boatStorage.get(index).getExpense();
        }
        System.out.printf("\tTotal                                             : Paid $%9.2f : Spent $%9.2f" , sumPrice, sumExpense);
        System.out.println();
    }

//---- Method to add boat to the ArrayList
    public static ArrayList<Boat> addBoat(ArrayList<Boat> boatStorage) {
        Boat addBoat;
        String[] boatData;
        String thisLine;

//---- Takes in new Boat data by creating an array by splitting the data up by commas, then creates a new Boat object with the data
        System.out.print("Please enter the new boat CSV data          : ");
        thisLine = keyboard.nextLine();
        boatData = thisLine.split(",");
        addBoat = new Boat(Boat.BoatEnum.valueOf(boatData[0]), boatData[1], Integer.parseInt(boatData[2]), boatData[3], Integer.parseInt(boatData[4]), Double.parseDouble(boatData[5]), 0);
        boatStorage.add(addBoat);
        return(boatStorage);
    }

//---- Method to remove a Boat object from the ArrayList
    public static ArrayList<Boat> removeBoat(ArrayList<Boat> boatStorage) {
        String removeBoat;
        int index;
        boolean found = false;
//---- Gets name of Boat user wants to remove
        System.out.print("Which boat do you want to remove?           : ");
        removeBoat = keyboard.nextLine();
//---- For loop that checks if the name the user provided is in the ArrayList, if the Boat is found the loop breaks
        for (index = 0; index < boatStorage.size(); ++index) {
            if (boatStorage.get(index).getName().equalsIgnoreCase(removeBoat)) {
                boatStorage.remove(index);
                found = true;
                break;
            }
        }
//---- If the name is not found, prints message
        if (!found) {
            System.out.print("Cannot find boat " + removeBoat);
            System.out.println();
        }
        return(boatStorage);
    }

//---- Method to change the expense data of the Boat object
    public static ArrayList<Boat> changeExpense(ArrayList<Boat> boatStorage) {
        String thisBoat;
        int index;
        double spendAmount;
        int boatIndex=0;
        double remainingFunds;
        double newSpendAmount;

        boolean found = false;
//---- Same for loop to check for Boat name from user input as the loop in remove boat
        System.out.print("Which boat do you want to spend on?         : ");
        thisBoat = keyboard.nextLine();
        for (index = 0; index < boatStorage.size(); ++index) {
            if (boatStorage.get(index).getName().equalsIgnoreCase(thisBoat)) {
                boatIndex = index;
                found = true;
                break;
            }
        }
//---- If boat is found, get the amount user wants to spend
        if (found) {
            System.out.print("How much do you want to spend?              : ");
            spendAmount = keyboard.nextDouble();
//---- Adds the user amount to the expense total already on the Boat
            newSpendAmount = boatStorage.get(boatIndex).getExpense() + spendAmount;
//---- If the spend amount is less than the price the Boat was purchased for, then print out message and set new expense
            if (newSpendAmount < boatStorage.get(boatIndex).getPrice()) {
                System.out.printf("Expense authorized, $%.2f spent." , newSpendAmount);
                boatStorage.get(boatIndex).setExpense(spendAmount);
//---- If the spend amount is greater than the price the Boat was purchased for, print out denial message
            } else {
                remainingFunds = boatStorage.get(boatIndex).getPrice() - boatStorage.get(boatIndex).getExpense();
                System.out.printf("Expense not permitted, only $%.2f left to spend." , remainingFunds);

            }
            keyboard.nextLine();
        }
//---- Prints out message if Boat is not found
         else {
            System.out.print("Cannot find boat " + thisBoat);
        }
            System.out.println();
            return (boatStorage);
        }

//---- Method to exit the program, before exiting another method is called to save the data to the database file
    public static void exit(ArrayList<Boat> boatStorage) {

        saveBoatFile(boatStorage);

        System.out.println("\nExiting the Fleet Management System");
        System.exit(0);
    }

//---- Method  to save the data into the database file
    public static void saveBoatFile(ArrayList<Boat> boatStorage) {

        ObjectOutputStream toStream = null;
        int index;

        try {
//---- Creates new ObjectOutputStream to write out the Boat objects into the database file
            toStream = new ObjectOutputStream(new FileOutputStream(DB_FILE_NAME));
//---- For loop to write out Boat objects into the file
            for (index = 0; index < boatStorage.size();  index++) {
                    toStream.writeObject(boatStorage.get(index));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        } finally {
            if (toStream != null) {
                try {
//---- Closes database file
                    toStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

//---- Method to load in database file if a csv file is not provided as an argument
    public static void loadBoatFile(ArrayList<Boat> boatStorage) {

        ObjectInputStream fromStream = null;
        Boat newBoat;

        try {
//---- Creates an ObjectInputStream to read in the data from the database file
            fromStream = new ObjectInputStream(new FileInputStream(DB_FILE_NAME));
            newBoat = (Boat)fromStream.readObject();
//---- Loop to read in Boat object and add it into the ArrayList
            while (newBoat != null) {
                boatStorage.add(newBoat);
                newBoat = (Boat)fromStream.readObject();
            }
        } catch (EOFException e) {
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            }

         finally {
            if (fromStream != null) {
                try {
//---- Closes database file
                    fromStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
