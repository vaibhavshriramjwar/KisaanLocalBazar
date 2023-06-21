package xyz.vpscorelim.kisaan.dealer.DealerModel;

public class SeedModel {

    private String seedName;
    private int seedImage;

    public SeedModel(String seedName, int seedImage) {
        this.seedName = seedName;
        this.seedImage = seedImage;
    }


    public String getSeedName() {
        return seedName;
    }

    public void setSeedName(String seedName) {
        this.seedName = seedName;
    }

    public int getSeedImage() {
        return seedImage;
    }

    public void setSeedImage(int seedImage) {
        this.seedImage = seedImage;
    }
}
