package com.example.sindhu.alzheimerscaregiver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CaretakerDashboard extends AppCompatActivity implements View.OnClickListener{

    Button logoutButton;
    CardView myprofileButton, anamolyDetectorButton, testButton;
    String username, role,emailId;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_dashboard);
        username=getIntent().getExtras().getString("username");
        role=getIntent().getExtras().getString("role");
        emailId=getIntent().getExtras().getString("email");
        myprofileButton=(CardView)findViewById(R.id.myprofile);
        anamolyDetectorButton=(CardView) findViewById(R.id.anamolydetector);
       // testButton=(CardView) findViewById(R.id.test);
        myprofileButton.setOnClickListener(this);
        anamolyDetectorButton.setOnClickListener(this);
      //  testButton.setOnClickListener(this);
        db= new DatabaseHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        inflater.inflate(R.menu.deleteaccount, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            finish();
            Intent patientDashboardIntent = new Intent(this, LoginActivity.class);
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

        if(id==R.id.deleteaccount)
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

        switch(view.getId())
        {
            case R.id.logout:
                finish();
                break;

            case R.id.myprofile:
                Intent profileintent = new Intent(this,CaretakerProfilePage.class);
                profileintent.putExtra("username", username);
                this.startActivity(profileintent);
                break;

            case R.id.anamolydetector:
                Intent anamolyintent = new Intent(this, MapsActivity.class);
                anamolyintent.putExtra("username", username);
                anamolyintent.putExtra("emailId", emailId);
                this.startActivity(anamolyintent);
                break;

            case R.id.test:
                Intent testintent = new Intent(this,TestLocation.class);
                startActivity(testintent);
                break;
        }
    }
}
