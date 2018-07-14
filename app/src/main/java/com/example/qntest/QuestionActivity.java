package com.example.qntest;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.example.qntest.events.ActiveUserEvent;
import com.example.qntest.events.GetActiveUserCall;
import com.example.qntest.events.QuestionEvent;
import com.example.qntest.events.QuizOverEvent;
import com.example.qntest.restapi.models.QuestionModel;
import com.example.qntest.service.QuizReceive;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private View mContentView;
    EventBus eventBus;

    TextView tvQuestion,tvOption1,tvOption2,tvOption3,tvOption4,tvActiveUsers,tvScore;
     Runnable runnable;
     Handler h = new Handler();

     private int score=0;
     private String answer;
     private boolean userTouched=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion=(TextView)findViewById(R.id.tv_question);
        tvOption1=(TextView)findViewById(R.id.tv_opt1);
        tvOption2=(TextView)findViewById(R.id.tv_opt2);
        tvOption3=(TextView)findViewById(R.id.tv_opt3);
        tvOption4=(TextView)findViewById(R.id.tv_opt4);



        tvActiveUsers=(TextView)findViewById(R.id.tv_activeuser);
        tvScore=(TextView)findViewById(R.id.tv_score);
        tvScore.setText("0");

        eventBus = EventBus.getDefault();
        eventBus.register(this);
        mContentView = findViewById(R.id.fullscreen);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);



            tvOption1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    submit(tvOption1.getText().toString());
                    tvOption1.setBackground(getDrawable(R.drawable.bg_response));

                }
            });

            tvOption2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvOption2.setBackground(getDrawable(R.drawable.bg_response));

                    submit(tvOption2.getText().toString());
                }
            });

            tvOption3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvOption3.setBackground(getDrawable(R.drawable.bg_response));

                    submit(tvOption3.getText().toString());

                }
            });

            tvOption4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvOption4.setBackground(getDrawable(R.drawable.bg_response));

                    submit(tvOption4.getText().toString());

                }
            });







      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                eventBus.post(new GetActiveUserCall());
                handler.postDelayed(this, 2000);
            }
        }, 1500);*/

    }

    public void submit(String response) {
        userTouched=true;

        tvOption1.setClickable(false);
        tvOption2.setClickable(false);
        tvOption3.setClickable(false);
        tvOption4.setClickable(false);
        if(answer.equals(response)){
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
            score++;
            tvScore.setText(String.valueOf(score));
        }else{
            Toast.makeText(this, "InCorrect Answer", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!eventBus.isRegistered(this)){
            eventBus.register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQuestionReceived(final QuestionEvent event){


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userTouched=false;
                tvOption1.setClickable(true);
                tvOption2.setClickable(true);
                tvOption3.setClickable(true);
                tvOption4.setClickable(true);

                tvOption1.setBackground(getDrawable(R.drawable.bg_answer));
                tvOption2.setBackground(getDrawable(R.drawable.bg_answer));
                tvOption3.setBackground(getDrawable(R.drawable.bg_answer));
                tvOption4.setBackground(getDrawable(R.drawable.bg_answer));

                 QuestionModel model= event.getQuestionModel();
                ArrayList<String> options =(ArrayList<String>)model.getOptions();
                answer=options.get(model.getAnswer()-1);
                tvQuestion.setText(model.getQuestion());
                tvOption1.setText(options.get(0));
                tvOption2.setText(options.get(1));
                tvOption3.setText(options.get(2));
                tvOption4.setText(options.get(3));

                new CountDownTimer(10000, 25) {

                    CircularProgressBar progressBar = findViewById(R.id.timer);

                    public void onTick(long millisUntilFinished) {
                        progressBar.setProgress(millisUntilFinished);
                    }

                    public void onFinish() {
                        progressBar.setProgress(0);
                    }
                }.start();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQuizOver(QuizOverEvent event){
        if(event.isStatus()){
            Intent intent =new Intent(QuestionActivity.this,LeaderboardActivity.class);
            intent.putExtra("score",score);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_to_left,R.anim.slide_from_right);
            finish();
        }
    }

    @Subscribe
    public void onActiveUser(final ActiveUserEvent event){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvActiveUsers.setText(event.getSize());

            }
        });

    }
}
