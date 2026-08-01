package Models;

public class Countries {

    private int countryID;
    private String country;

    //Constructor for modifying the Customer
    public Countries(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    //Constructor for getting the country name
    public Countries(String country) {
        this.country = country;
    }

    //Getters
    public int getCountryID() {
        return countryID;
    }

    public String getCountry() {
        return country;
    }

    //Setters
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String toString () {
        return this.country;
    }
}
