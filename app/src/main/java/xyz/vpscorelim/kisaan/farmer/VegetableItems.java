package xyz.vpscorelim.kisaan.farmer;

public class VegetableItems {
    private String vegetableName;
    private int vegetableImage;
    public VegetableItems(String vegName, int vegImage) {
        vegetableName = vegName;
        vegetableImage = vegImage;
    }
    public String getVegetableName() {
        return vegetableName;
    }
    public int getVegetableImage() {
        return vegetableImage;
    }

}
