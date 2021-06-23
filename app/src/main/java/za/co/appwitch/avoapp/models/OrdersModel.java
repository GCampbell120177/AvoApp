package za.co.appwitch.avoapp.models;

public class OrdersModel {

    String cod, eft, orderID, order, orderStatus, paymentStatus, userEmail, userFullName, userID, userMobile, userStreet, userSuburb, userCity, deliveryConfirmed, deliveryDate,paidDelivery, deliveryFee;

    public OrdersModel() {
    }

    public OrdersModel(String cod, String eft, String orderID, String order, String orderStatus, String paymentStatus, String userEmail, String userFullName, String userID, String userMobile, String userStreet, String userSuburb, String userCity, String deliveryConfirmed, String deliveryDate, String paidDelivery, String deliveryFee) {
        this.cod = cod;
        this.eft = eft;
        this.orderID = orderID;
        this.order = order;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.userID = userID;
        this.userMobile = userMobile;
        this.userStreet = userStreet;
        this.userSuburb = userSuburb;
        this.userCity = userCity;
        this.deliveryConfirmed = deliveryConfirmed;
        this.deliveryDate = deliveryDate;
        this.paidDelivery = paidDelivery;
        this.deliveryFee = deliveryFee;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getEft() {
        return eft;
    }

    public void setEft(String eft) {
        this.eft = eft;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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

    public String getDeliveryConfirmed() {
        return deliveryConfirmed;
    }

    public void setDeliveryConfirmed(String deliveryConfirmed) {
        this.deliveryConfirmed = deliveryConfirmed;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaidDelivery() {
        return paidDelivery;
    }

    public void setPaidDelivery(String paidDelivery) {
        this.paidDelivery = paidDelivery;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
