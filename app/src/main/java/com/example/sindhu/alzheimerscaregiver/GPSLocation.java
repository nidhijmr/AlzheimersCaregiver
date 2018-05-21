package com.example.sindhu.alzheimerscaregiver;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSLocation extends Service implements LocationListener {

    private final Context mContext;

    boolean GPSEnabled = false;
    boolean NetworkEnabled = false;
    boolean canGetLocation = false;

    private static final String TAG = GPSLocation.class.getSimpleName();
    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 12;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 2;

    protected LocationManager locationManager;

    public GPSLocation(Context context) {
        this.mContext = context;
        System.out.println("inside dps location");
        getLocation();
    }

    public Location getLocation() {
        try {

            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            GPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            NetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (GPSEnabled && NetworkEnabled)  {
                this.canGetLocation = true;
                if (NetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (GPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (SecurityException se){
            se.printStackTrace();
        }

        return location;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public String getAddress() {
        String fullAddress="";
        try {
            if(location != null) {
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(this.mContext, Locale.getDefault());
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);
                fullAddress = address;
            }else{
                Log.d(TAG,"location is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  fullAddress;
    }

}