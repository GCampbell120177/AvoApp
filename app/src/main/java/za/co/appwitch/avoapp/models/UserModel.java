package za.co.appwitch.avoapp.models;

public class UserModel {

    String userFullName, userMobile, userEmail, userStreet, userSuburb, userCity, currentOrder;

    public UserModel() {
    }

    public UserModel(String userFullName, String userMobile, String userEmail, String userStreet, String userSuburb, String userCity, String currentOrder) {
        this.userFullName = userFullName;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.userStreet = userStreet;
        this.userSuburb = userSuburb;
        this.userCity = userCity;
        this.currentOrder = currentOrder;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserStreet() {
        return userStreet;
    }

    public void setUserStreet(String userStreet) {
        this.userStreet = userStreet;
    }

    public String getUserSuburb() {
        return userSuburb;
    }

    public void setUserSuburb(String userSuburb) {
        this.userSuburb = userSuburb;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(String currentOrder) {
        this.currentOrder = currentOrder;
    }
}
