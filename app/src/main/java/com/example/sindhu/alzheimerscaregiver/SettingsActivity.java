package com.example.sindhu.alzheimerscaregiver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class SettingsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CheckBox notificationCheckBox;
    private EditText notificationInterval;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        final SharedPreferences myPreferences = getSharedPreferences("tasks", MODE_PRIVATE);

        notificationCheckBox = (CheckBox) findViewById(R.id.notification_checkbox);
        notificationInterval = (EditText) findViewById(R.id.notification_duration_edittext);

        notificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putBoolean("isNotificationEnabled", isChecked);
                editor.apply();
            }
        });

        notificationInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putInt("notificationInterval", Integer.parseInt(s.toString()));
                editor.apply();
            }
        });

        // Setting up list
        ArrayList<String> tasks = new ArrayList<>();
        ArrayList<Long> times = new ArrayList<>();

        tasks.add("Cooking");
        times.add(myPreferences.getLong("Cooking", (long) (25 * 60 * 1000)));

        tasks.add("Washing Clothes");
        times.add(myPreferences.getLong("Washing Clothes", (long) (35 * 60 * 1000)));

        tasks.add("Drying Clothes");
        times.add(myPreferences.getLong("Drying Clothes", (long) (60 * 60 * 1000)));

        StringTokenizer tokenizer = new StringTokenizer(myPreferences.getString("tasks", ""), "|", false);

        while (tokenizer.hasMoreTokens()) {
            String task = tokenizer.nextToken();

            tasks.add(task);
            times.add(myPreferences.getLong(task, 0));
        }

        recyclerView = (RecyclerView) findViewById(R.id.settings_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NewTaskAdapter(this, tasks, times));

        updateData();
    }

    public void updateData() {
        updateList();
        updateNotifData();
    }

    private void updateNotifData() {
        final SharedPreferences myPreferences = getSharedPreferences("tasks", MODE_PRIVATE);
        boolean notify = myPreferences.getBoolean("isNotificationEnabled", true);
        int notifDuration = myPreferences.getInt("notificationInterval", 15);

        notificationCheckBox.setChecked(notify);
        notificationInterval.setText(String.valueOf(notifDuration));
    }

    private void updateList() {
        final SharedPreferences myPreferences = getSharedPreferences("tasks", MODE_PRIVATE);

        // Setting up list
        ArrayList<String> tasks = new ArrayList<>();
        ArrayList<Long> times = new ArrayList<>();

        tasks.add("Cooking");
        times.add(myPreferences.getLong("Cooking", (long) (25 * 60 * 1000)));

        tasks.add("Washing Clothes");
        times.add(myPreferences.getLong("Washing Clothes", (long) (35 * 60 * 1000)));

        tasks.add("Drying Clothes");
        times.add(myPreferences.getLong("Drying Clothes", (long) (60 * 60 * 1000)));

        StringTokenizer tokenizer = new StringTokenizer(myPreferences.getString("tasks", ""), "|", false);

        while (tokenizer.hasMoreTokens()) {
            String task = tokenizer.nextToken();

            tasks.add(task);
            times.add(myPreferences.getLong(task, 0));
        }
    }
}
