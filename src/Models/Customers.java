package Models;

public class Customers {

    private int customerID;
    private String customerName;
    private String customerAddress;
    private String postalCode;
    private String phoneNumber;
    private int Division;
    String divisionName;
    String countryName;

    //Constructor for modifying a Customer
    public Customers(int customerID, String customerName, String customerAddress, String postalCode, String phoneNumber, int division) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.Division = division;
    }

    //Constructor for getting all Customers
    public Customers(int customerID, String customerName, String customerAddress, String postalCode, String phoneNumber, int division, String divisionName, String countryName) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.Division = division;
        this.divisionName = divisionName;
        this.countryName = countryName;
    }

    //Constructor for saving new Customers
    public Customers(String customerName, String customerAddress, String postalCode, String phoneNumber, int division) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.Division = division;
    }

    //Constructor for getting Customer name and ID
    public Customers(int customerID, String customerName) {
        this.customerID = customerID;
        this.customerName = customerName;
    }

    //Constructor for getting Customer ID
    public Customers(int customerID) {
        this.customerID = customerID;
    }

    //Getters
    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getDivision() {
        return Division;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public String getCountryName() {
        return countryName;
    }


    //Setters
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDivision(int division) {
        Division = division;
    }

    public void setCountryName(String countryName) {this.countryName = countryName; }

    public void setDivisionName(String divisionName) {
        divisionName = divisionName;
    }

    @Override
    public String toString() {
        String str = String.valueOf(getCustomerID()) + " - " + getCustomerName();
        return str;
    }
}

