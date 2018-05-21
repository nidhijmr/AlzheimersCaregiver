package com.example.sindhu.alzheimerscaregiver;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TestLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    TextView txtLoc;
    Button ShowLocations;
    public GoogleApiClient ggleAPICli;

    private static String[] PERMS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_location);
        txtLoc = findViewById(R.id.txtLocation);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {

            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
            }
        }).execute();

        /*ShowLocations=findViewById(R.id.btnShowLocations);
        ShowLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TestLocation.this,MapsActivity.class);
                startActivity(i);
            }
        });*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMS_STORAGE, 1);
        }
        else  checkkGglePlyService();

        Intent intent=new Intent(TestLocation.this,LocationTracker.class);
        getApplicationContext().startService(intent);

    }

    private void stopLocUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(ggleAPICli,this);
    }

    private void showLocation() {

    }

    private boolean checkkGglePlyService() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, this, 7172).show();
            } else {
                Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        ggleAPICli.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("MainActivity", "MainActivity");
    }

    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(LocationTracker.SEND_LOCATION_INTENT)) {
                if(intent.getExtras().getString("location").equals(null) || intent.getExtras().getString("location").equals("")) {
                    // txtLoc.setText("3000 Mission College Blvd,\n  Santa Clara, CA 95054");
                }
                else
                    txtLoc.setText(intent.getExtras().getString("location"));
                Log.i("changed" , "changed");
            }
        }
    };
    private boolean rcvrsRgstrd = false;
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentToReceive = new IntentFilter();
        intentToReceive.addAction(LocationTracker.SEND_LOCATION_INTENT);
        this.registerReceiver(intentReceiver, intentToReceive, null, handler);
        rcvrsRgstrd = true;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(rcvrsRgstrd) {
            unregisterReceiver(intentReceiver);
            rcvrsRgstrd = false;
        }
    }
}