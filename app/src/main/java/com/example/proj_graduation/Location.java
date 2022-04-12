package com.example.proj_graduation;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Location {
    private Drama[] drama;

    public Drama[] getDrama() { return drama; }
    public void setDrama(Drama[] value) { this.drama = value; }
}

class Drama {
    private String name;
    private String desc;
    private String poster;
    private Spot[] spots;

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getDesc() { return desc; }
    public void setDesc(String value) { this.desc = value; }

    public String getImageURL() { return poster; }
    public void setImageURL(String value) { this.poster = value; }

    public Spot[] getSpots() { return spots; }
    public void setSpots(JSONArray value) throws JSONException {

        List<Spot> spotList = new ArrayList<Spot>();
        for (int j=0; j<value.length(); j++)
        {
            JSONObject spotObject = value.getJSONObject(j);

            Spot spot = new Spot();

            spot.setName(spotObject.getString("name"));
            spot.setDesc(spotObject.getString("desc"));
            spot.setImageURL(spotObject.getString("image"));
            spot.setLongitude(spotObject.getDouble("longitude"));
            spot.setLatitude(spotObject.getDouble("latitude"));

            spotList.add(spot);
        }
        this.spots = spotList.toArray(new Spot[0]);
    }
}

class Spot implements Parcelable {
    private String name;
    private String desc;
    private String image;
    private double latitude;
    private double longitude;

    protected Spot(Parcel in) {
        name = in.readString();
        desc = in.readString();
        image = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Spot> CREATOR = new Creator<Spot>() {
        @Override
        public Spot createFromParcel(Parcel in) {
            return new Spot(in);
        }

        @Override
        public Spot[] newArray(int size) {
            return new Spot[size];
        }
    };

    public Spot() {

    }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getDesc() { return desc; }
    public void setDesc(String value) { this.desc = value; }

    public String getImageURL() { return image; }
    public void setImageURL(String value) { this.image = value; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double value) { this.latitude = value; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double value) { this.longitude = value; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(image);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
