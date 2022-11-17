import java.io.Serializable;

public class Boat implements Serializable {
//---- Final variables for the Boat class
    private static final int INITIAL_VALUE = 0;
    private static final double INITIAL_VALUE_DOUBLE = 0.0;
//---- Enum class for Boat object
    protected enum BoatEnum {SAILING, POWER}
    BoatEnum boatStyle;
//---- Data variables for the boat object
    private String boatName;
    private int yearMade;
    private String makeModel;
    private int boatLength;
    private double purchasePrice;
    private double maintenancePrice;
//---- Default constructor for Boat object
    public Boat() {
        boatStyle = null;
        boatName = null;
        yearMade = INITIAL_VALUE;
        makeModel = null;
        boatLength = INITIAL_VALUE;
        purchasePrice = INITIAL_VALUE_DOUBLE;
        maintenancePrice = INITIAL_VALUE_DOUBLE;
    }
//---- Constructor for Boat object
    public Boat(BoatEnum boatEnum, String name, int year, String make, int length, double price, double expenses) {
        boatStyle = boatEnum;
        boatName = name;
        yearMade = year;
        makeModel = make;
        boatLength = length;
        purchasePrice = price;
        maintenancePrice = expenses;
    }
//---- Method for setting a new maintenancePrice for the Boat object
    public void setExpense(double expense) {
            maintenancePrice += expense;
        }

//---- Gets expense double for Boat object
    public double getExpense() {
        return(maintenancePrice);
    }
//---- Gets name string for Boat object
    public String getName() {
        return(boatName);
    }
//---- Gets price double for Boat object
    public double getPrice() {
        return(purchasePrice);
    }
//---- toString method for Boat object that returns a formatted version of the data
    public String toString() {
        String print;
        print = String.format("\t%-8s%-21s%4d %-12s%2d\' : Paid $%9.2f : Spent $%9.2f", boatStyle.toString(),boatName,yearMade ,makeModel  , boatLength , purchasePrice , maintenancePrice);
        return(print);
    }

    }


