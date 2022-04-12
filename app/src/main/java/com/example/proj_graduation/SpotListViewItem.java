package com.example.proj_graduation;

public class SpotListViewItem extends ListViewItem {
    private String imageURL;
    private String titleStr;
    private String descStr;
    private Double longitude;
    private Double latitude;

    public SpotListViewItem(String image, String title, String desc, Double lon, Double lat) {
        imageURL = image;
        titleStr = title;
        descStr = desc;
        longitude = lon;
        latitude = lat;
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
    public void setLatitude(Double lat) {
        latitude = lat;
    }
    public void setLongitude(Double lon) {
        longitude = lon;
    }

    public String getImage() {
        return this.imageURL ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}
