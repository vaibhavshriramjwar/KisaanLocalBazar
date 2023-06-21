package xyz.vpscorelim.kisaan.adapter;

public class ModelFarmerData {

    String Farmer_Name;
    String phoneNumber;
    String Bulk_avail;
    String movable;
    String District_Name;
    String Tehsil_Name;
    String State_Name;
    String uid;

    public ModelFarmerData() {
    }

    public ModelFarmerData(String farmer_Name, String phoneNumber, String bulk_avail, String movable, String district_Name, String tehsil_Name, String state_Name, String uid) {
        Farmer_Name = farmer_Name;
        this.phoneNumber = phoneNumber;
        Bulk_avail = bulk_avail;
        this.movable = movable;
        District_Name = district_Name;
        Tehsil_Name = tehsil_Name;
        State_Name = state_Name;
        this.uid = uid;
    }

    public String getFarmer_Name() {
        return Farmer_Name;
    }

    public void setFarmer_Name(String farmer_Name) {
        Farmer_Name = farmer_Name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBulk_avail() {
        return Bulk_avail;
    }

    public void setBulk_avail(String bulk_avail) {
        Bulk_avail = bulk_avail;
    }

    public String getMovable() {
        return movable;
    }

    public void setMovable(String movable) {
        this.movable = movable;
    }

    public String getDistrict_Name() {
        return District_Name;
    }

    public void setDistrict_Name(String district_Name) {
        District_Name = district_Name;
    }

    public String getTehsil_Name() {
        return Tehsil_Name;
    }

    public void setTehsil_Name(String tehsil_Name) {
        Tehsil_Name = tehsil_Name;
    }

    public String getState_Name() {
        return State_Name;
    }

    public void setState_Name(String state_Name) {
        State_Name = state_Name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
