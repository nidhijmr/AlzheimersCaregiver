package com.example.sindhu.alzheimerscaregiver;


import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FallDetection extends AppCompatActivity implements View.OnClickListener {

    String phoneNumber, username, address;
    DatabaseHelper db;
    MediaPlayer mp;
   // Boolean callFlag=true;
    Button yesButton;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detection);
        address = getIntent().getStringExtra("address");
        username = getIntent().getStringExtra("username");
        System.out.println("Username= "+ username+ " inside Fall Detection");
        yesButton = (Button) findViewById(R.id.yes);
        yesButton.setOnClickListener(this);

        db = new DatabaseHelper(this);

        mp = MediaPlayer.create(this, R.raw.alarm_sound);

        System.out.println("Inside fall detection: Alarm started !!!");
        mp.start();

        System.out.println("starting timer");
        countDownTimer= new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

                System.out.println("seconds remaining " + l / 1000);
            }

            @Override
            public void onFinish() {
                System.out.println("Time out: calling timeUP");
                TimeUp();

            }
        }.start();
    }


    public void TimeUp()
    {
        System.out.println("inside Timeup fn");
        mp.stop();
        countDownTimer.cancel();
    //   yesButton.setEnabled(false);
        Intent callIntent= new Intent(FallDetection.this, EmergencyCall.class);
        callIntent.putExtra("username", username);
        callIntent.putExtra("address", address);
        System.out.println("Calling emergency phone + SMS");
        startActivity(callIntent);
        System.out.println("returned from emergeny cal");
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("on stop of fall detection: packup and go");
        mp.stop();
        countDownTimer.cancel();
        finish();
    }


    @Override
    public void onClick(View view) {

        if(view.getId()== R.id.yes) {
            countDownTimer.cancel();
            System.out.println("on Click of fall detection: packup and go");
            mp.stop();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("on Backpress of fall detection: packup and go");
        mp.stop();
    }


}


