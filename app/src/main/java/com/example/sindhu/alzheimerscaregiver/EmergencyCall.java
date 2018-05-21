package com.example.sindhu.alzheimerscaregiver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.widget.Toast;


public class EmergencyCall extends AppCompatActivity {
    String phoneNumber = "", address, username;
    String message;
    DatabaseHelper db;
    TelephonyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        address = getIntent().getExtras().getString("address");
        username = getIntent().getExtras().getString("username");
        System.out.println("Username " + username + " inside Emergency call");
        db = new DatabaseHelper(this);
        message = "Emergency! I may need help!!. My location: " + address;


        System.out.println("inside emergency call: on create");


        Cursor cursor = db.getData("select U.name,C.phone_number from contacts as U INNER JOIN contacts as C on U.caretaker_name=C.name where U.username=" + "\"" + username + "\"");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                phoneNumber = cursor.getString(1);
            }
        }


        if (!phoneNumber.equals("")) {

            Log.i("TAG", phoneNumber);
            System.out.println("Phone number " + phoneNumber);
            System.out.println("dialing emergency contact started");
            DialEmergency();
            System.out.println("dialing emergency contact ended");

        }
    }

    public void sendMessage(String message, String phoneNumber) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public void DialEmergency() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String concatTel = "tel:" + phoneNumber;

        Intent callIntent1 = new Intent(Intent.ACTION_CALL);
        callIntent1.setData(Uri.parse(concatTel));
        AudioManager audioManager = (AudioManager)this. getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(true);
        System.out.println("CAll activity – Start");
        startActivity(callIntent1);

        audioManager = (AudioManager)this. getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(true);


        System.out.println("CAll activity – End");
        System.out.println("SMS activity – Start");

        sendMessage(message, phoneNumber);
        System.out.println("SMS activity – end");
        finish();

        /*Intent patientDashboardIntent = new Intent(EmergencyCall.this, Dashboard.class);
        patientDashboardIntent.putExtra("username", username);
        startActivity(patientDashboardIntent);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("emergency call on stop : packup and go");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("emergency call backpressed : packup and go");
        finish();
    }

}











