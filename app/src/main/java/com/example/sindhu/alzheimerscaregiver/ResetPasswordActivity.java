package com.example.sindhu.alzheimerscaregiver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private Button resetPassword;
    private EditText newPassword, confirmPassword;
    String username;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        username= getIntent().getExtras().getString("username");
        resetPassword =(Button) findViewById(R.id.button_reset);
        newPassword = (EditText)findViewById(R.id.newpassword);
        confirmPassword = (EditText) findViewById(R.id.confrimpassword);
        resetPassword.setOnClickListener(this);
        db= new DatabaseHelper(this);

    }

    @Override
    public void onClick(View view) {

        if(newPassword.getText().toString().equals(confirmPassword.getText().toString()))
        {
          if( db.resetPassword(username,newPassword.getText().toString()))
          {
              AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
              alertDialog.setTitle("Success");
              alertDialog.setMessage("Password updated successfully");
              alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                      new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                          }
                      });
              alertDialog.show();
          }

        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(ResetPasswordActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Passwords do not match. Enter again");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }



    }
}
