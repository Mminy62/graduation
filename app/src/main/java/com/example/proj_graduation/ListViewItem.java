package com.example.proj_graduation;

import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;

public class ListViewItem {
    private String imageURL;
    private String titleStr;
    private String descStr;
    private Spot[] spotLst;

    public ListViewItem(String image, String title, String desc, Spot[] spots) {
        imageURL = image;
        titleStr = title;
        descStr = desc;
        spotLst = spots;
    }

    public ListViewItem() {

    }

    public void setImage(String url) {
        imageURL = url;
    }
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setDesc(String desc) {
        descStr = desc;
    }

    public String getImage() {
        return this.imageURL;
    }
    public String getTitle() {
        return this.titleStr;
    }
    public String getDesc() {
        return this.descStr;
    }
    public Spot[] getSpots() {
        return this.spotLst;
    }
}
