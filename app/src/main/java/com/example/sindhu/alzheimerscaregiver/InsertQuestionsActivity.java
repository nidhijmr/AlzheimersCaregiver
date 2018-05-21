package com.example.sindhu.alzheimerscaregiver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertQuestionsActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etQuestion, etOptionA, etOptionB, etOptionC, etOptionD,etAnswer;
    Button nextButton,doneButton, insertButton;
    String question, optiona, optionb, optionc,optiond,answer;
    String userName;
    DatabaseHelper db;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_questions);
        userName=getIntent().getExtras().getString("username");
        db= new DatabaseHelper(this);
        etQuestion= (EditText) findViewById(R.id.question);
        etOptionA=(EditText) findViewById(R.id.optiona);
        etOptionB=(EditText) findViewById(R.id.optionb);
        etOptionC=(EditText) findViewById(R.id.optionc);
        etOptionD=(EditText) findViewById(R.id.optiond);
        etAnswer=(EditText) findViewById(R.id.answer);
        nextButton=(Button) findViewById(R.id.nextbutton);
        doneButton=(Button) findViewById(R.id.donebutton);
        insertButton=(Button) findViewById(R.id.insertbutton);
        nextButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        insertButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {


        switch(view.getId()) {
            case R.id.insertbutton:
                question= etQuestion.getText().toString();
                optiona=etOptionA.getText().toString();
                optionb=etOptionB.getText().toString();
                optionc=etOptionC.getText().toString();
                optiond=etOptionD.getText().toString();
                answer=etAnswer.getText().toString();
                db.insertQuizQuestions(userName,question,optiona,optionb,optionc,optiond,answer);
                Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nextbutton:
                Intent nextQuestion = new Intent(InsertQuestionsActivity.this, InsertQuestionsActivity.class);
                nextQuestion.putExtra("username", userName);
                startActivity(nextQuestion);
                break;

            case R.id.donebutton:
               // finish();
                Intent quizActivity =  new Intent(InsertQuestionsActivity.this, QuizMainActivity.class);
                quizActivity.putExtra("username", userName);
                startActivity(quizActivity);
                break;
        }
    }

}
