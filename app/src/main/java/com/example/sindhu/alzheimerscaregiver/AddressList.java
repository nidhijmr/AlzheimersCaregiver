package com.example.sindhu.alzheimerscaregiver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class AddressList extends AppCompatActivity implements LocationListener, View.OnClickListener {

    private CardView homeAddress, officeAddress, marketAddress,bankAddress;
    private String provider;
    private LocationManager lm;
    private Location location;
    private double longitude=0.00,dblongitude=0.00;
    private double latitude=0.00, dblatitude=0.00;
    private static final int REQUEST_LOCATION_CODE = 99;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String destination = "";
    private String username, place;
    Criteria criteria;
    DatabaseHelper databaseHelper;
    Button editHome, editOffice,editMarket, editBank;
    String fullAddress="";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        if(databaseHelper.getAllLocationCount().size()==0)
        {
           // databaseHelper.locationList();
        }

        username=getIntent().getExtras().getString("username");

        homeAddress = (CardView) findViewById(R.id.home_address);
        officeAddress = (CardView) findViewById(R.id.office_address);
        marketAddress = (CardView) findViewById(R.id.market_address);
        bankAddress = (CardView) findViewById(R.id.bank_address);
        editHome =(Button) findViewById(R.id.editHome);
        editMarket =(Button) findViewById(R.id.editMarket);
        editOffice =(Button) findViewById(R.id.editOffice);
        editBank =(Button) findViewById(R.id.editBank);
        editHome.getBackground().setAlpha(0);
        editMarket.getBackground().setAlpha(0);
        editOffice.getBackground().setAlpha(0);
        editBank.getBackground().setAlpha(0);
        homeAddress.setOnClickListener(this);
        officeAddress.setOnClickListener(this);
        marketAddress.setOnClickListener(this);
        bankAddress.setOnClickListener(this);
        editHome.setOnClickListener(this);
        editMarket.setOnClickListener(this);
        editBank.setOnClickListener(this);
        editOffice.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.home_address:
                dblatitude=0.00;
                dblongitude=0.00;

                Cursor cursorHome= databaseHelper.getData("select latitude,longitude from locationlist where username=" + "\"" + username + "\"" + " and place=\"Home\"");
                if(cursorHome!=null) {
                    while (cursorHome.moveToNext()) {
                        dblatitude = cursorHome.getDouble(0);
                        dblongitude = cursorHome.getDouble(1);
                        System.out.println("dblatitude= " + dblatitude);
                        System.out.println("dblongitude= " + dblongitude);
                    }
                }

                getLocation();
                fullAddress=buildAddress(latitude,longitude);
                destination = username +" is travelling towards "+ fullAddress ;
                // sendMessage(destination);

                Intent homeIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + dblatitude + "," + dblongitude));
                startActivity(homeIntent);
                break;

            case R.id.office_address:
                dblatitude=0.00;
                dblongitude=0.00;

                Cursor cursorOffice= databaseHelper.getData("select latitude,longitude from locationlist where username=" + "\"" + username + "\"" + " and place=\"Office\"");
                while(cursorOffice.moveToNext()) {
                    dblatitude = cursorOffice.getDouble(0);
                    dblongitude = cursorOffice.getDouble(1);
                }

                getLocation();
                fullAddress=buildAddress(latitude,longitude);
                destination = username +" is travelling towards "+ fullAddress ;
               // sendMessage(destination);

                Intent officeIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + dblatitude + "," + dblongitude));
                startActivity(officeIntent);
                break;

            case R.id.market_address:
                dblatitude=0.00;
                dblongitude=0.00;
                Cursor cursorMarket= databaseHelper.getData("select latitude,longitude from locationlist where username=" + "\"" + username + "\"" + " and place=\"Market\"");
                while(cursorMarket.moveToNext()) {
                    dblatitude = cursorMarket.getDouble(0);
                    dblongitude = cursorMarket.getDouble(1);

                    if(dblatitude==0.00 && dblongitude==0.00) {
                        Intent editMarketIntent =new Intent(AddressList.this,GoogleMapsActivity.class);
                        editMarketIntent.putExtra("username", username);
                        editMarketIntent.putExtra("place", "Market");
                        startActivity(editMarketIntent);
                    }
                }

                getLocation();
                fullAddress=buildAddress(latitude,longitude);
                destination = username +" is travelling towards "+ fullAddress ;
               // sendMessage(destination);

                Intent marketIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + dblatitude + "," + dblongitude));
                startActivity(marketIntent);
                break;

            case R.id.bank_address:
                dblatitude=0.00;
                dblongitude=0.00;
                Cursor cursorBank= databaseHelper.getData("select latitude,longitude from locationlist where username=" + "\"" + username + "\"" + " and place=\"Bank\"");
                while(cursorBank.moveToNext()) {
                    dblatitude = cursorBank.getDouble(0);
                    dblongitude = cursorBank.getDouble(1);
                }

                getLocation();
                fullAddress=buildAddress(latitude,longitude);
                destination = username +" is travelling towards "+ fullAddress ;
               // sendMessage(destination);

                Intent bankIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + dblatitude + "," + dblongitude));
                startActivity(bankIntent);
                break;


            case R.id.editHome:
                Intent editHomeIntent =new Intent(AddressList.this,GoogleMapsActivity.class);
                editHomeIntent.putExtra("username", username);
                editHomeIntent.putExtra("place", "Home");
                startActivity(editHomeIntent);
                break;

            case R.id.editMarket:
                Intent editMarketIntent =new Intent(AddressList.this,GoogleMapsActivity.class);
                editMarketIntent.putExtra("username", username);
                editMarketIntent.putExtra("place", "Market");
                startActivity(editMarketIntent);
                break;

            case R.id.editOffice:
                Intent editOfficeIntent =new Intent(AddressList.this,GoogleMapsActivity.class);
                editOfficeIntent.putExtra("username", username);
                editOfficeIntent.putExtra("place","Office");
                startActivity(editOfficeIntent);
                break;

            case R.id.editBank:
                Intent editBankIntent =new Intent(AddressList.this,GoogleMapsActivity.class);
                editBankIntent.putExtra("username", username);
                editBankIntent.putExtra("place", "Bank");
                startActivity(editBankIntent);
                break;

        }

    }

    public void getLocation()
    {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        provider = lm.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = lm.getLastKnownLocation(provider);

        if(location!=null)
        {
            onLocationChanged(location);
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println("Latitude= "+ latitude);


    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else

            return true;


    }

    public void sendMessage(String destination) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("4087669197", null, destination, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public String buildAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        String fullAddr="";
        try {
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            fullAddr=address + city + state +country + postalCode+knownName;
        } catch(Exception e)
        {
            e.printStackTrace();
            Log.w("My Current address", "Cannot get Address!");
        }

        return fullAddr;


    }


}
