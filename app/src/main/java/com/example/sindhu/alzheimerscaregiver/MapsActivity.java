package com.example.sindhu.alzheimerscaregiver;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    public DynamoDBMapper dynamoDBMapper;
    List<UserLocationDO> userlatLong;
    String[] todaydateArray;
    String userName, emailId;
    private Geocoder geocoder;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
        @Override
        public void onComplete(AWSStartupResult awsStartupResult) {
            AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
            dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(
                            AWSMobileClient.getInstance().getConfiguration())
                    .build();
            Log.i("dynamo", String.valueOf(dynamoDBMapper));
            FetchLocations();
        }
         }).execute();

        setContentView(R.layout.activity_maps);
        userName=getIntent().getExtras().getString("username");
        emailId="nidhi.jmr@gmail.com";

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
        googleMap.moveCamera(zoom);
        googleMap.animateCamera(zoom);
        try {
            googleMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {

        }
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);


        for(int i = 0; i< userlatLong.size(); i++) {
            LatLng tempLoc = new LatLng(Double.parseDouble(userlatLong.get(i).getLatitude()), Double.parseDouble(userlatLong.get(i).getLongitude()));
            geocoder = new Geocoder(this, Locale.getDefault());
            String finalAddress = "";
            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(userlatLong.get(i).getLatitude()), Double.parseDouble(userlatLong.get(i).getLongitude()), 1);
                if(addresses.size() > 0) {
                    for (int x = 0; x < addresses.get(0).getMaxAddressLineIndex(); x++) {
                        finalAddress += addresses.get(0).getAddressLine(x) + " ";
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();

            }
            if(Double.parseDouble(userlatLong.get(i).getAnomalyScore())<1.0)
                googleMap.addMarker(new MarkerOptions().position(tempLoc));
            else
                googleMap.addMarker(new MarkerOptions().position(tempLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(tempLoc));
        }
        googleMap.setOnMarkerClickListener(this);
    }

    public void FetchLocations() {
        String currentDate= String.valueOf(Calendar.getInstance().getTime());
        todaydateArray=currentDate.split(" ");

        Thread thrFetchLoc =  new Thread(new Runnable() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void run() {
                Map<String, AttributeValue> userVal = new HashMap<>();
                userVal.put(":Patient_UserName", new AttributeValue().withS(emailId));
                //userValues.put(":Datetym", new AttributeValue().withS("Mon Apr 09 20:03:16 PDT 2018"));
                userVal.put(":Datetym", new AttributeValue().withS(todaydateArray[0]+ " "+ todaydateArray[1] + " " + todaydateArray[2]));
               // userVal.put(":Datetym", new AttributeValue().withS("Mon Apr 09 "));

                Map<String, String> userVal2 = new HashMap<>();
                userVal2.put("#Dt", "DateTime");
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                        .withFilterExpression("Patient_UserName = :Patient_UserName and contains(#Dt, :Datetym)").withExpressionAttributeValues(userVal).withExpressionAttributeNames(userVal2);

                try {
                    userlatLong = dynamoDBMapper.scan(UserLocationDO.class, scanExpression);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (userlatLong.isEmpty()) {
                }
                Log.i("****Data Fetched****", String.valueOf(userlatLong.size()));
            }
        });
        thrFetchLoc.start();

        try {
            thrFetchLoc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("Inside onMarkerClick");
        geocoder = new Geocoder(this, Locale.getDefault());
            String finalAddress = "test address";
            try {
                addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 2);
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                finalAddress=address + city + state +country + postalCode;

            } catch (IOException e) {
                e.printStackTrace();

            }
            System.out.println("Final address= "+finalAddress);

        marker.setTitle(finalAddress);
        marker.showInfoWindow();
        return true;
    }


}
