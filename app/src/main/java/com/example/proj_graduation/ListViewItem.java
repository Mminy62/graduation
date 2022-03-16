package com.example.proj_graduation;

import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;

    public ListViewItem(Drawable drawable, String title, String desc) {
        iconDrawable = drawable;
        titleStr = title;
        descStr = desc;
    }

    public ListViewItem() {

    }

    public void setIcon(Drawable icon) {
        iconDrawable = icon;
    }
    public void setTitle(String title) {
        titleStr = title;
    }
    public void setDesc(String desc) {
        descStr = desc;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
