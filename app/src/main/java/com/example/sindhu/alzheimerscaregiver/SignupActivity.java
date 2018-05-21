package com.example.sindhu.alzheimerscaregiver;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.io.Serializable;
import java.io.Serializable;

public class SignupActivity extends AppCompatActivity implements Serializable{

    DatabaseHelper helper = new DatabaseHelper(this);
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    String role;
    private Context ctx;
    public static DynamoDBMapper dynamoDBMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ctx=getApplicationContext();
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
            }
        }).execute();


    }


    public void doSignup(View view)
    {
        switch(view.getId()) {
            case R.id.button_createAccount:
                EditText name = (EditText) findViewById(R.id.name);
                EditText username = (EditText) findViewById(R.id.username);
                EditText password = (EditText) findViewById(R.id.password);
                EditText emailId= (EditText) findViewById(R.id.emailid);
                EditText phoneNo= (EditText) findViewById(R.id.phone);
                radioGroup= (RadioGroup)  findViewById(R.id.radioRole);

                String namestr = name.getText().toString();
                String unamestr = username.getText().toString();
                String passstr = password.getText().toString();
                String emailstr = emailId.getText().toString();
                String phonestr = phoneNo.getText().toString();

                int SelectId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(SelectId);
                System.out.println("++++++ Button Slected " + radioButton.getText());
                if (radioButton.getText().equals("Patient"))
                {
                    role = "patient";
                }
                else
                {
                    role ="caretaker";
                }
                System.out.println("+++++ Role=" + role);
                Contact c = new Contact();
                c.setName(namestr);
                c.setUname(unamestr);
                c.setPass(passstr);
                c.setEmailid(emailstr);
                c.setPhone_no(phonestr);
                c.setRole(role);

                helper.insertContact(c);
                Intent i = new Intent(this,InsertContactsDynamo.class);
                i.setAction("com.example.sindhu.contacts.Insert");
                i.putExtra("userobject", (Serializable) c);
                ctx.sendBroadcast(i);
                Toast.makeText(SignupActivity.this, " Successfully Registered", Toast.LENGTH_LONG).show();

                name.setText("");
                username.setText("");
                password.setText("");
                emailId.setText("");
                phoneNo.setText("");
                break;


            case R.id.button_login:

                Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;


        }
    }


}
