package Models;


public class Contacts {

    private int contactID;
    private String contactName;
    private String contactEmail;

    //Constructor for getting Contacts
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    //Constructor for getting Contacts
    public Contacts(int contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
    }

    public Contacts(int contactId) {
        this.contactID = contactID;
    }

    //Getters
    public int getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    //Setters
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setContactName(String name) {
        this.contactName = contactName;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        String str = String.valueOf(getContactID()) + " - " + getContactName();
        return str;
    }
}
