package com.example.sindhu.alzheimerscaregiver;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;



public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double newLatitude = 0.0;
    double newLongitude = 0.0;
    double dblatitude=0.00, dblongitude=0.00;
    DatabaseHelper databaseHelper;
    String username, place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        username = getIntent().getExtras().getString("username");
        place = getIntent().getExtras().getString("place");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void updateLocation()
    {
        Cursor cursor = databaseHelper.getData("select latitude,longitude from locationList where username=" + "\"" + username + "\"" + " and place=" + "\"" + place + "\"");
             if (cursor != null) {
            while (cursor.moveToNext()) {
                System.out.println("inside while");
                dblatitude = cursor.getDouble(0);
                dblongitude = cursor.getDouble(1);
                System.out.println("FRom db Latitude= " + dblatitude);
                System.out.println("From db Longitude= " + dblongitude);
            }
                if(dblatitude==0.00 && dblongitude==0.00)
                {
                    databaseHelper.insertLatLng(username,place,newLatitude,newLongitude);
                }
                else
                {
                    databaseHelper.updateLatLng(username,place,newLatitude,newLongitude);
                }

        }

    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
               /* mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point).title("Custom location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13)); */
               MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                markerOptions.title(point.latitude + " : " + point.longitude);
                System.out.println("Inside Map Click");
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
                mMap.addMarker(markerOptions);
                newLatitude = point.latitude;
                newLongitude = point.longitude;
                System.out.println("Latitude= " + newLatitude);
                System.out.println("Longitude= " + newLatitude);
                updateLocation();
            }
        });
    }
}
