package com.example.sindhu.alzheimerscaregiver;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class GetImageActivity extends AppCompatActivity {

    private ArrayList<Bitmap> images;
    private ArrayList<ImageDetails> imageDetails;
    DatabaseHelper db;
    private int columnWidth=500;
    Button get;
    ImageGridViewAdapter adapter;
    private GridView imageGridview;
    String username;

    //private String name, relationship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        db = new DatabaseHelper(this);
        imageGridview =(GridView) findViewById(R.id.my_grid_view);
        username=getIntent().getExtras().getString("username");
        imageDetails = new ArrayList<>();
        adapter = new ImageGridViewAdapter(this,R.layout.image_items, imageDetails);
        imageGridview.setAdapter(adapter);
        Bitmap bitmap = null;

        System.out.println("Select img,name,relationship from imageList where username=" + "\"" + username + "");

        Cursor icursor = db.getData("Select img,name,relationship from imageList where username=" + "\"" + username + "\"");

        System.out.println("Select img,name,relationship from imageList where username=" + "\"" + username + "");
        imageDetails.clear();
        if(icursor!=null) {
            while (icursor.moveToNext()) {
                // int id = cursor.getInt(0);
                byte[] img = icursor.getBlob(0);
                String name = icursor.getString(1);
                String relationship = icursor.getString(2);
                imageDetails.add(new ImageDetails(name, relationship, img));
            }
        }
        adapter.notifyDataSetChanged();

    }
}
