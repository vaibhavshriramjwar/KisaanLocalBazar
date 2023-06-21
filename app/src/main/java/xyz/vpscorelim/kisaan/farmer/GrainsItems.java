package xyz.vpscorelim.kisaan.farmer;

class GrainsItems {
    private String grainsName;
    private int grainsImage;


    public GrainsItems(String graName, int graImage) {
        grainsName = graName;
        grainsImage = graImage;
    }


    public String getGrainsName() {
        return grainsName;
    }
    public int getGrainsImage() {
        return grainsImage;
    }
}
