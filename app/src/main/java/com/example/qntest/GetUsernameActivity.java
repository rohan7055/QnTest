package com.example.qntest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qntest.restapi.Api;
import com.example.qntest.restapi.RetrofitManager;
import com.example.qntest.restapi.models.StatusModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUsernameActivity extends AppCompatActivity {
    EditText etUsername;
    private String userMobile;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_username);
        etUsername=(EditText)findViewById(R.id.et_username);
       userMobile= getIntent().getStringExtra("mobile");
    }

    public void confirmUsername(android.view.View view){

        //Register Logic here
        final String name=etUsername.getText().toString();
        if(!name.equals("")) {
            Api service = RetrofitManager.getRetrofit().create(Api.class);
            Call<StatusModel> call = service.register(name, userMobile, "abc12345", false);
            dialog = ProgressDialog.show(this, "",
                    "Loading. Please wait...", true);
            call.enqueue(new Callback<StatusModel>() {
                @Override
                public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                    dialog.dismiss();
                    StatusModel model=response.body();
                    if(model.isStatus()){
                        SharedPreferences user = getSharedPreferences("USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = user.edit();
                        editor.putString("mobile",userMobile);
                        editor.putString("name",name);
                        editor.apply();
                        Toast.makeText(GetUsernameActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GetUsernameActivity.this, HomeActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    }else{
                        Toast.makeText(GetUsernameActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<StatusModel> call, Throwable t) {
                    dialog.dismiss();

                }
            });

        }else{
            Toast.makeText(this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();

        }
    }
}
