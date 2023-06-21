package xyz.vpscorelim.kisaan.customer;

public class CustomerOrderModel {

    private String UserPhone;
    private String ProductID;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Unit;
    private String FarmerID;
    private String CustomerID;
    private String ProductImage;



    public CustomerOrderModel() {
    }

    public CustomerOrderModel(String userPhone, String productID, String productName, String quantity, String price, String unit, String farmerID, String customerID, String productImage) {
        UserPhone = userPhone;
        ProductID = productID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Unit = unit;
        FarmerID = farmerID;
        CustomerID = customerID;
        ProductImage = productImage;
    }


    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getFarmerID() {
        return FarmerID;
    }

    public void setFarmerID(String farmerID) {
        FarmerID = farmerID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }
}
