package com.example.sindhu.alzheimerscaregiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotoAlbum extends AppCompatActivity implements View.OnClickListener {

    private CardView insert, get;
    ImageView imageView;
    private static final int PICK_IMAGE=100;
    private static final int RESULT_OK= -1;
    private ArrayList<ImageDetails> images;
    private GridView imageGridview;
    DatabaseHelper db;
    private int columnWidth=500;
    ImageGridViewAdapter adapter;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        db = new DatabaseHelper(this);
        insert = (CardView) findViewById(R.id.add_picture);
        get = (CardView) findViewById(R.id.display_pictures);
        username= getIntent().getExtras().getString("username");
        insert.setOnClickListener(this);
        get.setOnClickListener((View.OnClickListener) this);


        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getImageIntent = new Intent(PhotoAlbum.this, GetImageActivity.class);
                getImageIntent.putExtra("username", username);
                startActivity(getImageIntent);

            }
        });

    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.add_picture:
                Intent insertIntent = new Intent(PhotoAlbum.this, InsertImageActivity.class);
                insertIntent.putExtra("username", username);
                startActivity(insertIntent);
                break;
        }
    }

}
