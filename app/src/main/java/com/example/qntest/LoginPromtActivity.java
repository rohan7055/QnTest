package com.example.qntest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginPromtActivity extends AppCompatActivity {
    private Button btnhome,btncontinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promt_login);
        btnhome=(Button)findViewById(R.id.btnHome);
        btncontinue=(Button)findViewById(R.id.btnContinue);
        try {
            SharedPreferences user = getSharedPreferences("USER", MODE_PRIVATE);
            String mobile = user.getString("mobile", "");

            if (mobile.equals("") || mobile == null) {
                btnhome.setVisibility(View.GONE);
                btncontinue.setVisibility(View.VISIBLE);
            } else {
                btnhome.setVisibility(View.VISIBLE);
                btncontinue.setVisibility(View.GONE);
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    public void login(android.view.View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }
    public void home(android.view.View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    public void question(android.view.View view){
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
