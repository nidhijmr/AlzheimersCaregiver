package com.example.sindhu.alzheimerscaregiver;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsertImageActivity extends AppCompatActivity implements View.OnClickListener {

    Button choosePic;
    TextView name, relation;
    EditText ename, erelation;
    private static final int PICK_IMAGE=100;
    private static final int RESULT_OK= -1;
    DatabaseHelper db;
    String sname, srelation;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_image);
        db = new DatabaseHelper(this);
        choosePic = (Button) findViewById(R.id.button_choosePic);
        ename = (EditText) findViewById(R.id.editText3);
        erelation = (EditText) findViewById(R.id.editText4);
        choosePic.setOnClickListener(this);
        username=getIntent().getExtras().getString("username");

    }

    @Override
    public void onClick(View view) {
        Intent intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_IMAGE );

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            String x = getPath(uri);
            sname = ename.getText().toString();
            srelation = erelation.getText().toString();
            System.out.println("####### " + sname);
            System.out.println("####### " + srelation);
            if(db.insertImage(username,x,sname,srelation)) {
                Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Not inserted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getPath(Uri uri)
    {
        if(uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,projection,null,null,null);
        if(cursor!=null)
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();

    }

}
