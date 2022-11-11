import java.io.Serializable;

public class Boat implements Serializable {

    private static final int INITIAL_VALUE = 0;
    private static final double INITIAL_VALUE_DOUBLE = 0;

    protected enum BoatEnum {SAILING, POWER}
    BoatEnum boatStyle;
    protected String boatName;
    protected int yearMade;
    protected String makeModel;
    protected int boatLength;
    protected double purchasePrice;
    protected double maintenancePrice;

    public Boat() {
        boatStyle = null;
        boatName = null;
        yearMade = INITIAL_VALUE;
        makeModel = null;
        boatLength = INITIAL_VALUE;
        purchasePrice = INITIAL_VALUE_DOUBLE;
        maintenancePrice = INITIAL_VALUE_DOUBLE;
    }

    public Boat(BoatEnum boatEnum, String name, int year, String make, int length, double price, double expenses) {
        boatStyle = boatEnum;
        boatName = name;
        yearMade = year;
        makeModel = make;
        boatLength = length;
        purchasePrice = price;
        maintenancePrice = expenses;
    }

    public boolean setExpense(double expense) {
        if (this.maintenancePrice + expense > purchasePrice) {
            System.out.println("Expense not permitted, only $" + (purchasePrice - maintenancePrice) + " left to spend.");
            return(false);
        } else {
            maintenancePrice += expense;
            return(true);
        }
    }

    public double getExpense() {
        return(maintenancePrice);
    }

    public String getName() {
        return(boatName);
    }

    public double getPrice() {
        return(purchasePrice);
    }

    public String toString() {
        String print;
        print = String.format("\t%-8s%-21s%4d %-12s%2d\' : Paid $%9.2f : Spent $%9.2f", boatStyle.toString(),boatName,yearMade ,makeModel  , boatLength , purchasePrice , maintenancePrice);
        return(print);
    }

    }


