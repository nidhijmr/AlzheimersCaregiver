package com.example.sindhu.alzheimerscaregiver;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.text.DecimalFormat;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private CardView addressCard, memoryCard, quizCard,locationCard, tasksCard, pillCard;
    private Button emergencydial;
    private String username,address;;
    DatabaseHelper db;
    String phoneNumber;
    private Sensor accelerometer;
    private SensorManager accelerometerManager;
    Context context;
    long t1 = 0;
    long t2 = 0;
    boolean lessThan = false;
    boolean greaterThan = false;
    boolean callFlag=true;
    GPSLocation gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = new DatabaseHelper(this);
        addressCard = (CardView) findViewById(R.id.address_list);
        memoryCard = (CardView) findViewById(R.id.memory_boost);
        quizCard = (CardView) findViewById(R.id.quiz);
        tasksCard= (CardView) findViewById(R.id.task);
        locationCard=(CardView) findViewById(R.id.showmylocation);
        pillCard= (CardView) findViewById(R.id.pillschedule);
        emergencydial = (Button) findViewById(R.id.emergencydial);
        addressCard.setOnClickListener(this);
        memoryCard.setOnClickListener(this);
        quizCard.setOnClickListener(this);
        tasksCard.setOnClickListener(this);
        locationCard.setOnClickListener(this);
        emergencydial.setOnClickListener(this);
        pillCard.setOnClickListener(this);
        username = getIntent().getExtras().getString("username");
        System.out.println("Username= "+ username + "Inside Dashboard");

        accelerometerManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = accelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.myprofile, menu);
        inflater.inflate(R.menu.logout, menu);
        inflater.inflate(R.menu.deleteaccount, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        if(id==R.id.logout)
        {
            finish();
            Intent patientDashboardIntent = new Intent(Dashboard.this, LoginActivity.class);
            patientDashboardIntent.putExtra("username", username);
            startActivity(patientDashboardIntent);
            return true;
        }

        if(id==R.id.myprofile)
        {
            Intent profileintent = new Intent(this,ProfilePage.class);
            profileintent.putExtra("username", username);
            this.startActivity(profileintent);
            return true;
        }

        if(id== R.id.deleteaccount)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Delete Account");
            alertDialogBuilder
                    .setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            db.deleteAccount(username);
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.address_list:
                Intent addressIntent = new Intent(Dashboard.this, AddressList.class);
                addressIntent.putExtra("username", username);
                startActivity(addressIntent);
                break;

            case R.id.memory_boost:
                Intent albumIntent = new Intent(Dashboard.this, PhotoAlbum.class);
                albumIntent.putExtra("username", username);
                startActivity(albumIntent);
                break;

            case R.id.quiz:
                Intent quizIntent = new Intent(Dashboard.this, QuizMainActivity.class);
                quizIntent.putExtra("username", username);
                startActivity(quizIntent);
                break;

            case R.id.emergencydial:

                Cursor cursor = db.getData("select U.name,C.phone_number from contacts as U INNER JOIN contacts as C on U.caretaker_name=C.name where U.username=" + "\"" + username + "\"");
                        //"Select phone_number from contacts where username=" + "\"" + username + "\"");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        phoneNumber = cursor.getString(1);
                    }
                    String concatTel = "tel:" + phoneNumber;
                    AudioManager audioManager = (AudioManager)this. getSystemService(Context.AUDIO_SERVICE);

                    if(!audioManager.isSpeakerphoneOn()) {
                        audioManager.setMode(AudioManager.MODE_IN_CALL);
                        audioManager.setSpeakerphoneOn(true);
                    }

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(concatTel));

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);

                }
                break;

            case R.id.task:
                Intent taskIntent= new Intent (Dashboard.this, DailyActivity.class);
                startActivity(taskIntent);
                break;

            case R.id.showmylocation:
                Intent currentLocationIntent= new Intent(Dashboard.this, CurrentLocationActivity.class);
                startActivity(currentLocationIntent);
                break;

            case R.id.pillschedule:
                Intent pillScheduleIntent =  new Intent(Dashboard.this, PillSchedule.class );
                startActivity(pillScheduleIntent);
                break;
        }
    }

   @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            double acVector = Math.sqrt(x * x + y * y + z * z);
            //System.out.println("acVector= " + acVector + " at " + System.currentTimeMillis());

            if (acVector > 20) {
                greaterThan = true;
                t1 = System.currentTimeMillis();
            } else if (acVector < 3) {
                lessThan = true;
                t2 = System.currentTimeMillis();
            }

            if (greaterThan && lessThan && callFlag) {
                if (t2 - t1 <= 1000 && t2 - t1 >100) {

                    callFlag=false;
                    greaterThan = false;
                    lessThan = false;
                    t1 = 0;
                    t2 = 0;
                    acVector=0;

                    gps = new GPSLocation(Dashboard.this);
                    Intent intent = new Intent();
                    if (gps.canGetLocation()) {
                        address = gps.getAddress();
                        intent.putExtra("address", address);
                        intent.putExtra("username",username );
                    }
                    System.out.println("callFlag= "+ callFlag);
                    intent.setClass(this, FallDetection.class);
                    startActivity(intent);
                    System.out.println("returned to dashboard/ onsensor change from fall detection");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
