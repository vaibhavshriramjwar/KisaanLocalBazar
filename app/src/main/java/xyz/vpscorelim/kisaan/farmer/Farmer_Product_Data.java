package xyz.vpscorelim.kisaan.farmer;

import com.google.firebase.database.ServerValue;

public class Farmer_Product_Data {

    String uid,Product_Category,Product_Name,Product_Quantity,Product_Unit,Product_rate,Product_image,Product_Id,FarmerName,phoneNumber,FarmerState,FarmerCity,FarmerSubArea;


    public Farmer_Product_Data() {
    }


    public Farmer_Product_Data(String uid, String product_Category, String product_Name, String product_Quantity, String product_Unit, String product_rate, String product_image, String product_Id, String farmerName, String phoneNumber, String farmerState, String farmerCity, String farmerSubArea) {
        this.uid = uid;
        Product_Category = product_Category;
        Product_Name = product_Name;
        Product_Quantity = product_Quantity;
        Product_Unit = product_Unit;
        Product_rate = product_rate;
        Product_image = product_image;
        Product_Id = product_Id;
        FarmerName = farmerName;
        this.phoneNumber = phoneNumber;
        FarmerState = farmerState;
        FarmerCity = farmerCity;
        FarmerSubArea = farmerSubArea;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProduct_Category() {
        return Product_Category;
    }

    public void setProduct_Category(String product_Category) {
        Product_Category = product_Category;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getProduct_Quantity() {
        return Product_Quantity;
    }

    public void setProduct_Quantity(String product_Quantity) {
        Product_Quantity = product_Quantity;
    }

    public String getProduct_Unit() {
        return Product_Unit;
    }

    public void setProduct_Unit(String product_Unit) {
        Product_Unit = product_Unit;
    }

    public String getProduct_rate() {
        return Product_rate;
    }

    public void setProduct_rate(String product_rate) {
        Product_rate = product_rate;
    }

    public String getProduct_image() {
        return Product_image;
    }

    public void setProduct_image(String product_image) {
        Product_image = product_image;
    }

    public String getProduct_Id() {
        return Product_Id;
    }

    public void setProduct_Id(String product_Id) {
        Product_Id = product_Id;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        FarmerName = farmerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFarmerState() {
        return FarmerState;
    }

    public void setFarmerState(String farmerState) {
        FarmerState = farmerState;
    }

    public String getFarmerCity() {
        return FarmerCity;
    }

    public void setFarmerCity(String farmerCity) {
        FarmerCity = farmerCity;
    }

    public String getFarmerSubArea() {
        return FarmerSubArea;
    }

    public void setFarmerSubArea(String farmerSubArea) {
        FarmerSubArea = farmerSubArea;
    }
}
