package com.example.sindhu.alzheimerscaregiver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProfilePage extends AppCompatActivity implements View.OnClickListener {

    TextView tv_name, tv_email, tv_username, tv_phone, tv_caretaker;
    ImageView imgUser;
    RadioGroup radio_group;
    DatabaseHelper db;
    String userName;
    List<String> caretakerList = new ArrayList<>();
    CharSequence charSequences[]={};
    String name, email, phone, caretaker;
    Cursor cursor;
    Button assignCaretaker, editImage;
    String selectedCaretaker;
    byte[] img;
    private static final int PICK_IMAGE = 100;
    private static final int RESULT_OK = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        userName = getIntent().getExtras().getString("username");
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_caretaker = (TextView) findViewById(R.id.tv_caretaker);
        imgUser = (ImageView) findViewById(R.id.userimg);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        assignCaretaker = (Button) findViewById(R.id.assigncaretaker);
        editImage = (Button) findViewById(R.id.edit);
        editImage.getBackground().setAlpha(0);
        assignCaretaker.setOnClickListener(this);
        editImage.setOnClickListener(this);
        imgUser.setOnClickListener(this);
        db = new DatabaseHelper(this);

        cursor = db.getData("select name,email_id,phone_number,caretaker_name,profile_image from contacts where username=" + "\"" + userName + "\"");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                email = cursor.getString(1);
                phone = cursor.getString(2);
                caretaker = cursor.getString(3);
                img = cursor.getBlob(4);
            }
        }
        tv_name.setText(name);
        tv_email.setText(email);
        tv_username.setText(userName);
        tv_phone.setText(phone);
       if (caretaker!=null)
      {
            tv_caretaker.setText(caretaker);
       } else {

            tv_caretaker.setText("No caretaker");
        }
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            imgUser.setImageBitmap(bitmap);
        }

        cursor.close();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.assigncaretaker:

                createAlertDialogWithRadioGroup();
                break;

            case R.id.edit:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE);
                break;

        }

    }

    public void createAlertDialogWithRadioGroup() {

        cursor = db.getData("select name from contacts where role='caretaker'");

        if (cursor != null) {

            while (cursor.moveToNext()) {
                caretakerList.add(cursor.getString(0));
            }
        }
        charSequences=caretakerList.toArray(new CharSequence[caretakerList.size()]);

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Select Caretaker");
        ad.setSingleChoiceItems(charSequences, -1,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Toast.makeText(getApplicationContext(),
                        "You Choose : " + charSequences[arg1],
                        Toast.LENGTH_LONG).show();
                selectedCaretaker = charSequences[arg1].toString();
                System.out.println("++++++ " + selectedCaretaker);
                tv_caretaker.setText(selectedCaretaker);
                boolean b = db.insertData(selectedCaretaker,userName);
                if (b) {
                    System.out.println("Updated successfully");
                    tv_caretaker.setText(selectedCaretaker);
                } else {
                    System.out.println("Updated not successfull");
                }
            }
        });
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ad.show();

            }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data!=null) {
            Uri selectedImage=data.getData();
            imgUser.setImageURI(selectedImage);
            String x = getPath(selectedImage);
            if(db.updateProfilePicture(x,userName)) {
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

