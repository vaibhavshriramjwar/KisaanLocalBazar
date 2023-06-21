package xyz.vpscorelim.kisaan.dealer.DealerModel;

public class DealerProductListModel {

    private String uid;
    private String Product_Category;
    private String Product_Name;
    private String Product_Brand;
    private String Product_Quantity;
    private String Product_Unit;
    private String Product_rate;
    private String Product_image;
    private String Product_Id;


    public DealerProductListModel() {
    }


    public DealerProductListModel(String uid, String product_Category, String product_Name, String product_Brand, String product_Quantity, String product_Unit, String product_rate, String product_image, String product_Id) {
        this.uid = uid;
        Product_Category = product_Category;
        Product_Name = product_Name;
        Product_Brand = product_Brand;
        Product_Quantity = product_Quantity;
        Product_Unit = product_Unit;
        Product_rate = product_rate;
        Product_image = product_image;
        Product_Id = product_Id;
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

    public String getProduct_Brand() {
        return Product_Brand;
    }

    public void setProduct_Brand(String product_Brand) {
        Product_Brand = product_Brand;
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
}
