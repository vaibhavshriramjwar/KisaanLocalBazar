package xyz.vpscorelim.kisaan.farmer.farmer_Model;

public class GetDealerModel {

    private String User_Role;
    private String uid;
    private String phoneNumber;
    private String storeName;
    private String dealerName;
    private String stateName;
    private String districtName;
    private String subArea;
    private String shopLocation;
    private String Home_Delivery;
    private String latitude;
    private String longitude;


    public GetDealerModel() {
    }

    public GetDealerModel(String user_Role, String uid, String phoneNumber, String storeName, String dealerName, String stateName, String districtName, String subArea, String shopLocation, String home_Delivery, String latitude, String longitude) {
        User_Role = user_Role;
        this.uid = uid;
        this.phoneNumber = phoneNumber;
        this.storeName = storeName;
        this.dealerName = dealerName;
        this.stateName = stateName;
        this.districtName = districtName;
        this.subArea = subArea;
        this.shopLocation = shopLocation;
        Home_Delivery = home_Delivery;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getUser_Role() {
        return User_Role;
    }

    public void setUser_Role(String user_Role) {
        User_Role = user_Role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getSubArea() {
        return subArea;
    }

    public void setSubArea(String subArea) {
        this.subArea = subArea;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getHome_Delivery() {
        return Home_Delivery;
    }

    public void setHome_Delivery(String home_Delivery) {
        Home_Delivery = home_Delivery;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
