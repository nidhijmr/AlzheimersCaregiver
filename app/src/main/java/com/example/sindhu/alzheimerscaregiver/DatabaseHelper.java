package com.example.sindhu.alzheimerscaregiver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=17;
    private static final String DATABASE_NAME="alzheimer.db";
    private static final String TABLE_NAME="contacts";
    private static final String TABLE_NAME2="imageList";
    private static final String TABLE_NAME3="locationList";
    private static final String TABLE_NAME4="quiz";
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID= "userid";
    private static final String COLUMN_NAME ="name";
    private static final String COLUMN_UNAME= "username";
    private static final String COLUMN_PASS= "password";
    private static final String COLUMN_ROLE= "role";
    private static final String COLUMN_EMAIL= "email_id";
    private static final String COLUMN_PHONE= "phone_number";
    private static final String QUESTION="question";
    private static final String OPTIONA = "optionA";
    private static final String OPTIONB = "optionB";
    private static final String OPTIONC = "optionC";
    private static final String OPTIOND = "optionD";
    private static final String ANSWER = "answer";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_TASK_TIME = "task_time";


    private static final String TABLE_CREATE_CONTACT = "create table contacts (userid integer primary key autoincrement , name text not null, username text not null, password text not null, email_id text not null, phone_number text not null, role text not null, caretaker_name text, profile_image blob);";
    private static final String TABLE_CREATE_IMAGE= "create table imageList (id integer primary key autoincrement, username text not null, img blob not null, name text not null, relationship text not null);";
    private static final String TABLE_CREATE_LOCATION="create table locationList (username text not null, place text not null, latitude real not null, longitude real not null);";
    private static final String TABLE_CREATE_QUIZ="CREATE TABLE " + TABLE_NAME4 + " ( " + COLUMN_UNAME + " TEXT , " + QUESTION + " VARCHAR(255), " + OPTIONA + " VARCHAR(255), "+ OPTIONB + " VARCHAR(255), " + OPTIONC + " VARCHAR(255), " + OPTIOND + " VARCHAR(255), " + ANSWER + " VARCHAR(255));";
    private static final String TABLE_CREATE_TASKS="CREATE TABLE " + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
            + KEY_TIMESTAMP + " INTEGER, " + KEY_TASK_TIME + " INTEGER" + ")";

    SQLiteDatabase db;
    InsertImageActivity iActivity;
    public DynamoDBMapper dynamoDBMapper;

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME, null,DATABASE_VERSION  );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE_CONTACT);
        db.execSQL(TABLE_CREATE_IMAGE);
        db.execSQL(TABLE_CREATE_LOCATION);
        db.execSQL(TABLE_CREATE_QUIZ);
        db.execSQL(TABLE_CREATE_TASKS);

        this.db=db;

    }

    List<LocationInfo> getAllLocationCount()
    {
        List<LocationInfo> locList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {"username", "place", "latitude", "longitude"};
        Cursor cursor = db.query(TABLE_NAME3, coloumn, null, null, null, null, null);


        while (cursor.moveToNext()) {
            LocationInfo location = new LocationInfo();
            location.setUsername(cursor.getString(0));
            location.setPlace(cursor.getString(1));
            location.setLatitude(cursor.getDouble(2));
            location.setLongitude(cursor.getDouble(3));
            locList.add(location);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return locList;
    }


    public void insertLocations(ArrayList<LocationInfo> loclist)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues lvalues = new ContentValues();
            for (LocationInfo loc : loclist) {
                lvalues.put("username", loc.getUsername());
                lvalues.put("place", loc.getPlace());
                lvalues.put("latitude", loc.getLatitude());
                lvalues.put("longitude", loc.getLongitude());
                db.insert(TABLE_NAME3, null, lvalues);
            }
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
            db.close();
        }

    }

    public boolean insertImage(String username,String x, String sname, String srelation)
    {
        System.out.println("Username= " + username + "name= "+ sname + "Relation= " +srelation);
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            FileInputStream fs = new FileInputStream(x);
            byte[] imgbyte= new byte[fs.available()];
            fs.read(imgbyte);
            ContentValues values= new ContentValues();
            values.put("username", username);
            values.put("img", imgbyte);
            values.put("name", sname);
            values.put("relationship",srelation);
            db.insert("imageList", null, values);
            fs.close();
            return true;

        } catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProfilePicture(String x, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            FileInputStream fs = new FileInputStream(x);
            byte[] imgbyte = new byte[fs.available()];
            fs.read(imgbyte);
            ContentValues values = new ContentValues();
            values.put("profile_image",imgbyte);
            return db.update(TABLE_NAME,values, "username=?", new String[]{username})>0;
        } catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Cursor getData(String q)
    {
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);
        return cursor;
    }

    public boolean insertData(String name, String username)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("caretaker_name", name);
        return db.update(TABLE_NAME, cv,"username=?",new String[]{username} )>0;
    }


    public String getRole(String q) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        String roleType = null;
        while (cursor.moveToNext()) {
            roleType = cursor.getString(0);
        }
        return roleType;

    }

    public String getEmail(String q)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        String emailId = null;
        while (cursor.moveToNext()) {
            emailId = cursor.getString(0);
        }
        return emailId;
    }

    public boolean deleteAccount(String username)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from contacts where username=" + username);
        db.close();
        return true;
    }


    public void insertContact(Contact c)
    {
        db= this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query="select * from contacts";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, c.getName());
        values.put(COLUMN_UNAME, c.getUname());
        values.put(COLUMN_PASS, c.getPass());
        values.put(COLUMN_EMAIL, c.getEmailid());
        values.put(COLUMN_PHONE, c.getPhone_no());
        values.put(COLUMN_ROLE, c.getRole());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String searchPass(String uname)
    {

        db = this.getReadableDatabase();
        String query =" select username,password from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String un,pwd;
        pwd="not found";

        if(cursor.moveToFirst())
        {
            do{
                un = cursor.getString(0);
                if(un.equals(uname))
                {
                    pwd= cursor.getString(1);
                    break;
                }

            }while(cursor.moveToNext());

        }
        return pwd;
    }

    public void insertQuizQuestions(String username, String question, String optiona, String optionb, String optionc, String optiond, String answer)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("question", question);
            values.put("optionA", optiona);
            values.put("optionB", optionb);
            values.put("optionC", optionc);
            values.put("optionD", optiond);
            values.put("answer", answer);
            db.insert(TABLE_NAME4, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    public boolean resetPassword(String username, String newpassword)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("password", newpassword);
        return db.update(TABLE_NAME3, cv,"username=? ",new String[]{username}) >0;

    }


    public void insertLatLng(String username,String place, double latitude, double longitude) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("username", username);
            values.put("place", place);
            values.put("latitude", latitude);
            values.put("longitude", longitude);
            db.insert(TABLE_NAME3, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    public void updateLatLng(String username,String place, double latitude, double longitude)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        db.update(TABLE_NAME3, cv,"username=? and place=?",new String[]{username,place});
    }

    private void addAllQuestions(ArrayList<QuizQuestion> allQuestions)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (QuizQuestion question : allQuestions) {
                values.put(COLUMN_UNAME, question.getUsername());
                values.put(QUESTION, question.getQuestion());
                values.put(OPTIONA, question.getOptiona());
                values.put(OPTIONB, question.getOptionb());
                values.put(OPTIONC, question.getOptionc());
                values.put(OPTIOND, question.getOptiond());
                values.put(ANSWER, question.getAnswer());
                db.insert(TABLE_NAME4, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


    List<QuizQuestion> getAllQuestions(String username)
    {
        List<QuizQuestion> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {COLUMN_UNAME, QUESTION, OPTIONA, OPTIONB, OPTIONC, OPTIOND, ANSWER};
        Cursor cursor = db.query(TABLE_NAME4, coloumn, "username=?", new String[] {username}, null, null, null);


        while (cursor.moveToNext()) {
            QuizQuestion question = new QuizQuestion();
            question.setUsername(cursor.getString(0));
            question.setQuestion(cursor.getString(1));
            question.setOptiona(cursor.getString(2));
            question.setOptionb(cursor.getString(3));
            question.setOptionc(cursor.getString(4));
            question.setOptiond(cursor.getString(5));
            question.setAnswer(cursor.getString(6));
            questionsList.add(question);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }

    // Adding new task
    public void addTask(TaskModal task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_TIMESTAMP, task.getTimestamp());
        values.put(KEY_TASK_TIME, task.getTimeInMilliseconds());

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    // Getting single task
    public TaskModal getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_ID,
                        KEY_NAME, KEY_TIMESTAMP, KEY_TASK_TIME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TaskModal result = new TaskModal(cursor.getLong(0),
                cursor.getString(1), cursor.getLong(3), cursor.getLong(2));

        return result;
    }

    // Getting all tasks
    public ArrayList<TaskModal> getAllTasks() {
        ArrayList<TaskModal> tasks = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaskModal task = new TaskModal();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setName(cursor.getString(1));
                task.setTimestamp(cursor.getLong(2));
                task.setTimeInMilliseconds(cursor.getLong(3));
                // Adding contact to list
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return tasks;
    }

    // Updating single task
    public int updateTask(TaskModal task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_TIMESTAMP, task.getTimestamp());
        values.put(KEY_TASK_TIME, task.getTimeInMilliseconds());

        // updating row
        int result = db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        return result;
    }

    public void updateQuestion() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String Q="Where are you from?";
        db.execSQL("delete from quiz where question=" + Q);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query1= "DROP TABLE IF EXISTS " + TABLE_NAME;
        String query2= "DROP TABLE IF EXISTS " + TABLE_NAME2;
        String query3= "DROP TABLE IF EXISTS " + TABLE_NAME3;
        String query4= "DROP TABLE IF EXISTS " + TABLE_NAME4;
        String query5= "DROP TABLE IF EXISTS " + TABLE_TASKS;
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        this.onCreate(db);
    }
}
