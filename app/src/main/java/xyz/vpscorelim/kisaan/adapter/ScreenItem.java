package xyz.vpscorelim.kisaan.adapter;
public class ScreenItem {

    String Title,Description;


    public ScreenItem(String title, String description) {
        Title = title;
        Description = description;

    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }



    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }


}




