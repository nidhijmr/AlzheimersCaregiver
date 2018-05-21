package com.example.sindhu.alzheimerscaregiver;

/**
 * Created by Sindhu on 4/28/18.
 */

public class LocationInfo {

    private String username;
    private String place;
    private double latitude;
    private double longitude;

    public LocationInfo(String uname, String placename, double lat, double longi)
    {
        username= uname;
        place=placename;
        latitude=lat;
        longitude=longi;
    }

    public LocationInfo()
    {
        username="";
        place="";
        latitude=0;
        longitude=0;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPlace()
    {
        return place;

    }

    public double getLatitude()
    {
        return latitude;

    }
    public double getLongitude()
    {
        return longitude;

    }


    public void setUsername(String name)
    {

        username=name;
    }

    public void setPlace(String q)
    {
        place=q;
    }

    public void setLatitude(double lat)
    {

        latitude=lat;
    }

    public void setLongitude(double lng)
    {

        longitude=lng;
    }

}
