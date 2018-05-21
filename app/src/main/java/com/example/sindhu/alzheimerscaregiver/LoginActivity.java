package com.example.sindhu.alzheimerscaregiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);
    private Button forgotpassword, signup;
    String unamestr;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotpassword=(Button) findViewById(R.id.button_forgotpassword);
        signup= (Button) findViewById(R.id.button_signup);
        image=(ImageView) findViewById(R.id.image);
        forgotpassword.getBackground().setAlpha(0);
        signup.getBackground().setAlpha(0);
    }

    public void doSignup(View view) {

        if (view.getId() == R.id.button_signup) {

            Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(signupIntent);
        }
    }

    public void login(View view)
    {
        if(view.getId()== R.id.button_login)
        {
            EditText uname = (EditText) findViewById(R.id.username);
            unamestr = uname.getText().toString();
            EditText pass = (EditText) findViewById(R.id.password);
            String passstr = pass.getText().toString();
            String password = helper.searchPass(unamestr);
            
            if (passstr.equals(password))
            {
                String roleType= helper.getRole("select role from contacts where username=" + "\"" + unamestr + "\"" );
                String emailId=helper.getEmail("select email_id from contacts where username=" + "\"" + unamestr + "\"" );;
                if(roleType.equals("patient"))
                {
                    Intent patientDashboardIntent = new Intent(LoginActivity.this, Dashboard.class);
                    patientDashboardIntent.putExtra("username", unamestr);
                    startActivity(patientDashboardIntent);
                }
                else
                {
                    Intent caretakerDashboardIntent = new Intent(LoginActivity.this, CaretakerDashboard.class);
                    caretakerDashboardIntent.putExtra("username", unamestr);
                    caretakerDashboardIntent.putExtra("role",roleType);
                    caretakerDashboardIntent.putExtra("email",emailId);
                    startActivity(caretakerDashboardIntent);
                }

            } else {
                Toast.makeText(LoginActivity.this, "Username and/or Password doesnt match", Toast.LENGTH_SHORT).show();

            }

            uname.setText("");
            pass.setText("");
        }

    }

    public void forgotPassword(View view)
    {

        if(view.getId()== R.id.button_forgotpassword)
        {
            Intent resetPasswordIntent = new Intent(this, ResetPasswordActivity.class);
            resetPasswordIntent.putExtra("username", unamestr);
            startActivity(resetPasswordIntent);
        }
    }
}
