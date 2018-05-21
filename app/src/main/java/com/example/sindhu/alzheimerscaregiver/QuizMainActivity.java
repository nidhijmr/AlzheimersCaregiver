package com.example.sindhu.alzheimerscaregiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizMainActivity extends AppCompatActivity implements View.OnClickListener{

    Button playGame, quitGame;
    TextView textView;
    String uname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        uname=getIntent().getExtras().getString("username");
        playGame = (Button) findViewById(R.id.playGame);
        quitGame = (Button) findViewById(R.id.quitGame);

        playGame.setOnClickListener((View.OnClickListener) this);
        quitGame.setOnClickListener((View.OnClickListener) this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.insertquestions, menu);
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        if(id==R.id.logout)
        {
            finish();
            return true;
        }

        if(id==R.id.insertquestions)
        {
            Intent nextQuestion= new Intent(QuizMainActivity.this,InsertQuestionsActivity.class);
            nextQuestion.putExtra("username", uname);
            this.startActivity(nextQuestion);
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.playGame:
                Intent intent = new Intent(QuizMainActivity.this,QuizActivity.class);
                intent.putExtra("username",uname);
                startActivity(intent);
                break;

            case R.id.quitGame:
                finish();
                break;
        }

    }
}
