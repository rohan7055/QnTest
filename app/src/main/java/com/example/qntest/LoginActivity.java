package com.example.qntest;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone=(EditText)findViewById(R.id.et_phone);

    }

    public void back(android.view.View view){
        onBackPressed();
    }

    public void continue_login(android.view.View view){

        new CountDownTimer(30000, 1) {
            public void onTick(long millisUntilFinished) {
                String msg = "" + millisUntilFinished;
            }
            public void onFinish() {
                View resendBtn = findViewById(R.id.resend_btn);
                resendBtn.setVisibility(View.VISIBLE);
            }
        }.start();

        Button proceedBtn = findViewById(R.id.proceed_btn);
        proceedBtn.setVisibility(View.GONE);
        View otpLayout = findViewById(R.id.otpLayout);
        otpLayout.setVisibility(View.VISIBLE);
    }

    public void confirmOTP(android.view.View view){
        if(!etPhone.getText().toString().equals("")) {
            Intent intent = new Intent(this, GetUsernameActivity.class);
            intent.putExtra("mobile",etPhone.getText().toString());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }else
        {
            Toast.makeText(this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
