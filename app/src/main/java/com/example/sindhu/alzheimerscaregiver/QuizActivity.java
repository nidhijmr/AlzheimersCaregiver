package com.example.sindhu.alzheimerscaregiver;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private Button buttonA, buttonB,buttonC, buttonD;
    TextView questionText, timeText, coinText, resultText,quizText;
    String userName;
    DatabaseHelper databaseHelper;
    QuizQuestion currentQuestion;
    List<QuizQuestion> list;
    int timeValue=20, qid=0;
    int coinValue=0;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        userName = getIntent().getExtras().getString("username");
        buttonA= (Button) findViewById(R.id.buttonA);
        buttonB= (Button) findViewById(R.id.buttonB);
        buttonC= (Button) findViewById(R.id.buttonC);
        buttonD= (Button) findViewById(R.id.buttonD);
        questionText=(TextView)findViewById(R.id.question);
        timeText = (TextView)findViewById(R.id.timeText);
        coinText = (TextView)findViewById(R.id.coinText);
        quizText = (TextView)findViewById(R.id.quizText);
        resultText = (TextView)findViewById(R.id.resultText);

        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        if(databaseHelper.getAllQuestions(userName).size()==0)
        {
            //databaseHelper.Questions();
            finish();
        }

        //databaseHelper.updateQuestion();
        list = databaseHelper.getAllQuestions(userName);
        Collections.shuffle(list);

        currentQuestion = list.get(qid);

        countDownTimer = new CountDownTimer(22000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeText.setText(String.valueOf(timeValue) + "\"");
                timeValue -=1;

                if(timeValue==-1)
                {
                    resultText.setText("Time's Up!!!");
                    disableButton();
                }
            }

            @Override
            public void onFinish() {
                timeUp();

            }
        }.start();

        updateQuestionsAndOptions();

    }

    public void updateQuestionsAndOptions()
    {
        questionText.setText(currentQuestion.getQuestion());
        buttonA.setText(currentQuestion.getOptiona());
        buttonB.setText(currentQuestion.getOptionb());
        buttonC.setText(currentQuestion.getOptionc());
        buttonD.setText(currentQuestion.getOptiond());

        timeValue=20;

        countDownTimer.cancel();
        countDownTimer.start();

        coinText.setText(String.valueOf(coinValue));
        coinValue++;

    }

    public void buttonA(View view)
    {
        if (currentQuestion.getOptiona().equals(currentQuestion.getAnswer())) {
            buttonA.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
            //Check if user has not exceeds the que limit
            if (qid < list.size() - 1) {

                //Now disable all the option button since user ans is correct so
                //user won't be able to press another option button after pressing one button
                disableButton();

                //Show the dialog that ans is correct
                correctDialog();
            }
            //If user has exceeds the que limit just navigate him to GameWon activity
            else {

                gameWon();

            }
        }
        //User ans is wrong then just navigate him to the PlayAgain activity
        else {

            gameLostPlayAgain();

        }

    }

    public void buttonB(View view)
    {
        if (currentQuestion.getOptionb().equals(currentQuestion.getAnswer())) {
            buttonB.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
            //Check if user has not exceeds the que limit
            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
            }

            else {
                gameWon();
            }
        }
        else {

            gameLostPlayAgain();
        }
    }

    public void buttonC(View view)
    {
        if (currentQuestion.getOptionc().equals(currentQuestion.getAnswer())) {
            buttonC.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
            //Check if user has not exceeds the que limit
            if (qid < list.size() - 1) {
                disableButton();
                correctDialog();
            }

            else {

                gameWon();

            }
        }

        else {

            gameLostPlayAgain();

        }

    }

    public void buttonD(View view)
    {
        if (currentQuestion.getOptiond().equals(currentQuestion.getAnswer())) {
            buttonD.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
            if (qid < list.size() - 1) {

                disableButton();
                correctDialog();
            }

            else {

                gameWon();

            }
        }

        else {

            gameLostPlayAgain();

        }

    }

    public void gameWon() {
        Intent intent = new Intent(QuizActivity.this, GameWon.class);
        intent.putExtra("username", userName );
        intent.putExtra("CoinValue", coinValue);
        startActivity(intent);
        finish();
    }

    public void gameLostPlayAgain() {
        Intent intent = new Intent(QuizActivity.this, PlayAgain.class);
        intent.putExtra("username", userName );
        startActivity(intent);
        finish();
    }

    public void timeUp() {
        Intent intent = new Intent(QuizActivity.this, TimeUp.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        countDownTimer.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
    }


    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, QuizMainActivity.class);
        intent.putExtra("username",userName);
        startActivity(intent);
        finish();
    }


    public void correctDialog() {
        final Dialog correctAnswer = new Dialog(QuizActivity.this);
        correctAnswer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (correctAnswer.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            correctAnswer.getWindow().setBackgroundDrawable(colorDrawable);
        }
        correctAnswer.setContentView(R.layout.correct_answer);
        correctAnswer.setCancelable(false);
        correctAnswer.show();


        onPause();


        TextView correctText = (TextView) correctAnswer.findViewById(R.id.correctText);
        Button buttonNext = (Button) correctAnswer.findViewById(R.id.dialogNext);


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correctAnswer.dismiss();

                qid++;

                currentQuestion = list.get(qid);

                updateQuestionsAndOptions();

                resetColor();

                enableButton();
            }
        });
    }

    public void resetColor() {
        buttonA.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonB.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonC.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        buttonD.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
    }


    public void disableButton() {
        buttonA.setEnabled(false);
        buttonB.setEnabled(false);
        buttonC.setEnabled(false);
        buttonD.setEnabled(false);
    }

    public void enableButton() {
        buttonA.setEnabled(true);
        buttonB.setEnabled(true);
        buttonC.setEnabled(true);
        buttonD.setEnabled(true);
    }


}
