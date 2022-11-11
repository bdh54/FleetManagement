import java.io.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class mainBoat implements Serializable {
    public static final Scanner keyboard = new Scanner(System.in);

    private enum BoatEnum {SAILING, POWER}
    private static final String DB_FILE_NAME = "FleetData.db";
    private static final int ARGS_TEST = 1;

    public static void main(String[] args) {

        ArrayList<Boat> localBoatStorage = new ArrayList<>();

        if (args.length == ARGS_TEST) {
            BufferedReader fromBufferedReader;
            String oneLine;
            String[] boatData;

            try {
                fromBufferedReader = new BufferedReader(new FileReader(args[0]));
                oneLine = fromBufferedReader.readLine();
                while(oneLine != null) {
                    boatData = oneLine.split(",");
                    Boat newBoat = new Boat(Boat.BoatEnum.valueOf(boatData[0]), boatData[1], Integer.parseInt(boatData[2]), boatData[3], Integer.parseInt(boatData[4]), Double.parseDouble(boatData[5]), 0);
                    localBoatStorage.add(newBoat);
                    oneLine = fromBufferedReader.readLine();
                }
                fromBufferedReader.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }
        } else {
            loadCarFile(localBoatStorage);
        }

        menu(localBoatStorage);
    }

    public static void menu(ArrayList<Boat> boatStorage) {
        Character choice = 'a';

        System.out.println("Welcome to the Fleet Management System\n--------------------------------------");

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

    public static void print(ArrayList<Boat> boatStorage) {
        System.out.println("\n Fleet Report");
        double sumPrice = 0;
        double sumExpense = 0;
        int index;

        for (index=0; index<boatStorage.size(); ++index) {
            System.out.println(boatStorage.get(index));
            sumPrice += boatStorage.get(index).getPrice();
            sumExpense += boatStorage.get(index).getExpense();
        }
        System.out.printf("\tTotal                                             : Paid $%9.2f : Spent $%9.2f" , sumPrice, sumExpense);
        System.out.println();
    }

    public static ArrayList<Boat> addBoat(ArrayList<Boat> boatStorage) {
        Boat addBoat;
        String[] boatData;
        String thisLine;

        System.out.print("Please enter the new boat CSV data          : ");
        thisLine = keyboard.nextLine();
        boatData = thisLine.split(",");
        addBoat = new Boat(Boat.BoatEnum.valueOf(boatData[0]), boatData[1], Integer.parseInt(boatData[2]), boatData[3], Integer.parseInt(boatData[4]), Double.parseDouble(boatData[5]), 0);
        boatStorage.add(addBoat);
        return(boatStorage);
    }

    public static ArrayList<Boat> removeBoat(ArrayList<Boat> boatStorage) {
        String removeBoat;
        int index;
        boolean found = false;
        System.out.print("Which boat do you want to remove?           : ");
        removeBoat = keyboard.nextLine();
        for (index = 0; index < boatStorage.size(); ++index) {
            if (boatStorage.get(index).getName().equalsIgnoreCase(removeBoat)) {
                boatStorage.remove(index);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.print("Cannot find boat " + removeBoat);
            System.out.println();
        }
        return(boatStorage);
    }

    public static ArrayList<Boat> changeExpense(ArrayList<Boat> boatStorage) {
        String thisBoat;
        int index;
        double spendAmount;
        int boatIndex=0;
        double remainingFunds;
        double newSpendAmount;

        boolean found = false;
        System.out.print("Which boat do you want to spend on?         : ");
        thisBoat = keyboard.nextLine();
        for (index = 0; index < boatStorage.size(); ++index) {
            if (boatStorage.get(index).getName().equalsIgnoreCase(thisBoat)) {
                boatIndex = index;
                found = true;
                break;
            }
        }
        if (found) {
            System.out.print("How much do you want to spend?              : ");
            spendAmount = keyboard.nextDouble();
            newSpendAmount = boatStorage.get(boatIndex).getExpense() + spendAmount;
            if (newSpendAmount < boatStorage.get(boatIndex).getPrice()) {
                System.out.printf("Expense authorized, $%.2f spent." , newSpendAmount);
                boatStorage.get(boatIndex).setExpense(spendAmount);
            } else {
                remainingFunds = boatStorage.get(boatIndex).getPrice() - boatStorage.get(boatIndex).getExpense();
                System.out.printf("Expense not permitted, only $%.2f left to spend." , remainingFunds);

            }
            keyboard.nextLine();
        }
         else {
            System.out.print("Cannot find boat " + thisBoat);
        }
            System.out.println();
            return (boatStorage);
        }

    public static void exit(ArrayList<Boat> boatStorage) {
        saveBoatFile(boatStorage);
        System.out.println("\nExiting the Fleet Management System");
        System.exit(0);
    }

    public static boolean saveBoatFile(ArrayList<Boat> boatStorage) {

        ObjectOutputStream toStream = null;
        int index;

        try {
            toStream = new ObjectOutputStream(new FileOutputStream(DB_FILE_NAME));
            for (index = 0; index < boatStorage.size();  index++) {
                    toStream.writeObject(boatStorage.get(index));
            }
            return(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return(false);
        } finally {
            if (toStream != null) {
                try {
                    toStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return (false);
                }
            }
        }
    }
    public static boolean loadCarFile(ArrayList<Boat> boatStorage) {

        ObjectInputStream fromStream = null;
        Boat newBoat;

        try {
            fromStream = new ObjectInputStream(new FileInputStream(DB_FILE_NAME));
            newBoat = (Boat)fromStream.readObject();
            while (newBoat != null) {
                boatStorage.add(newBoat);
                newBoat = (Boat)fromStream.readObject();
            }
            return(true);
        } catch (EOFException e) {
            return(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return (false);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return(false);
        }
         finally {
            if (fromStream != null) {
                try {
                    fromStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    return(false);
                }
            }
        }
    }
}
