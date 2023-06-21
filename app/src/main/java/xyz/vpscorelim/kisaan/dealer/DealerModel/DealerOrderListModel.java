package xyz.vpscorelim.kisaan.dealer.DealerModel;

public class DealerOrderListModel  {

    private String UserPhone;
    private String ProductID;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Unit;
    private String FarmerID;
    private String DealerID;
    private String ProductImage;


    public DealerOrderListModel() {
    }


    public DealerOrderListModel(String userPhone, String productID, String productName, String quantity, String price, String unit, String farmerID, String dealerID, String productImage) {
        UserPhone = userPhone;
        ProductID = productID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Unit = unit;
        FarmerID = farmerID;
        DealerID = dealerID;
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

    public String getDealerID() {
        return DealerID;
    }

    public void setDealerID(String dealerID) {
        DealerID = dealerID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }
}
