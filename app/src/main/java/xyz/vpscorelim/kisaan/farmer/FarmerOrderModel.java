package xyz.vpscorelim.kisaan.farmer;

public class FarmerOrderModel {

    private String FarmerPhone;
    private String ProductID;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Unit;
    private String DealerID;
    private String FarmerID;
    private String ProductImage;
    private String ProductBrand;


    public FarmerOrderModel() {
    }


    public FarmerOrderModel(String farmerPhone, String productID, String productName, String quantity, String price, String unit, String dealerID, String farmerID, String productImage, String productBrand) {
        FarmerPhone = farmerPhone;
        ProductID = productID;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Unit = unit;
        DealerID = dealerID;
        FarmerID = farmerID;
        ProductImage = productImage;
        ProductBrand = productBrand;
    }


    public String getFarmerPhone() {
        return FarmerPhone;
    }

    public void setFarmerPhone(String farmerPhone) {
        FarmerPhone = farmerPhone;
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

    public String getDealerID() {
        return DealerID;
    }

    public void setDealerID(String dealerID) {
        DealerID = dealerID;
    }

    public String getFarmerID() {
        return FarmerID;
    }

    public void setFarmerID(String farmerID) {
        FarmerID = farmerID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductBrand() {
        return ProductBrand;
    }

    public void setProductBrand(String productBrand) {
        ProductBrand = productBrand;
    }
}
