package Models;

public class Divisions {

    private int divisionID;
    private String division;
    private int countryID;

    //Constructor to get Division parameters when modifying a customer
    public Divisions(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }


    public Divisions(String division, int divisionID) {
        this.division = division;
        this.divisionID = divisionID;
    }

    public Divisions(int CountryID) {
        this.countryID = countryID;
    }

    //Getters
    public int getDivisionID() {
        return divisionID;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryID() {
        return countryID;
    }

    //Setters
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String toString () {
        return this.division;
    }
}
