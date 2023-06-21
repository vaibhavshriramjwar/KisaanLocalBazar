package xyz.vpscorelim.kisaan.farmer;

class FruitsItems {
    private String fruitsName;
    private int fruitsImage;
    public FruitsItems(String fruName, int fruImage) {
        fruitsName = fruName;
        fruitsImage = fruImage;
    }
    public String getFruitsName() {
        return fruitsName;
    }
    public int getFruitsImage() {
        return fruitsImage;
    }
}
