package com.example.sindhu.alzheimerscaregiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.io.Serializable;
import java.util.UUID;

public class InsertContactsDynamo extends BroadcastReceiver implements Serializable {

    public Contact c;
    public ContactsDynamoDO c1 = new ContactsDynamoDO();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.sindhu.contacts.Insert")) {
            c = (Contact) intent.getSerializableExtra("userobject");

            Thread ContactThread =  new Thread(new Runnable() {
                @Override
                public int hashCode() {
                    return super.hashCode();
                }

                @Override
                public void run() {

                    c1.setUsername(c.getUname());
                    c1.setName(c.getName());
                    c1.setCaretaker_name("");
                    c1.setEmail_id(c.getEmailid());
                    c1.setPassword(c.getPass());
                    c1.setPhone_number(c.getPhone_no());
                    c1.setUserid(UUID.randomUUID().toString());
                    c1.setRole(c.getRole());
                    SignupActivity.dynamoDBMapper.save(c1);

                }
            });
            ContactThread.start();
    }

    }

}




