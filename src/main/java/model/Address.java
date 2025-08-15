package model;

public class Address {
    private int id;
    private int customerId;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private boolean isDefault;
    
    public Address() {
    }
    
    public Address(int customerId, String street, String city, String state, String zipCode, String country) {
        this.customerId = customerId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.isDefault = false;
    }
    
    public Address(int id, int customerId, String street, String city, String state, 
                   String zipCode, String country, boolean isDefault) {
        this.id = id;
        this.customerId = customerId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.isDefault = isDefault;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public String getFullAddress() {
        return street + ", " + city + ", " + state + " " + zipCode + ", " + country;
    }
    
    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}