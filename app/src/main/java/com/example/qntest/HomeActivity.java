package com.example.qntest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qntest.events.GetActiveUserCall;
import com.example.qntest.events.StartQuizEvent;
import com.example.qntest.restapi.Api;
import com.example.qntest.restapi.RetrofitManager;
import com.example.qntest.restapi.models.QuizModel;
import com.example.qntest.restapi.models.StartQuizResponse;
import com.example.qntest.service.QuizReceive;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private CardView cvStartQuiz;
    SharedPreferences preferences;
    ProgressDialog dialog;
    EventBus eventBus;
    Handler h = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cvStartQuiz=(CardView)findViewById(R.id.cv_activequiz);
        eventBus = EventBus.getDefault();
        eventBus.register(HomeActivity.this);

        if (isMyServiceRunning(QuizReceive.class)) {
            Log.i("yoloDomiLearn", "service is already running");
        } else {
            Intent intent = new Intent(this, QuizReceive.class);
            startService(intent);
        }






        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_appbar);
        View actionbar = getSupportActionBar().getCustomView();
        ImageView fab = actionbar.findViewById(R.id.drawer_toogle);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDiag();
                    }
                }
        );

        cvStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("USER", MODE_PRIVATE);
                String mobile=preferences.getString("mobile","");
                Api service= RetrofitManager.getRetrofit().create(Api.class);
                Call<StartQuizResponse> startQuizResponseCall = service.startQuizRequest(0, mobile);
                dialog = ProgressDialog.show(HomeActivity.this, "",
                        "Loading. Please wait...", true);
                startQuizResponseCall.enqueue(new Callback<StartQuizResponse>() {
                    @Override
                    public void onResponse(Call<StartQuizResponse> call, Response<StartQuizResponse> response) {
                        dialog.dismiss();
                        StartQuizResponse startQuizResponse=response.body();
                        if(startQuizResponse.isStatus()){
                            Toast.makeText(HomeActivity.this, startQuizResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            QuizModel quizModel=startQuizResponse.getData();
                            SharedPreferences quiz = getSharedPreferences("QUIZ", MODE_PRIVATE);
                            SharedPreferences.Editor quizeditor = quiz.edit();
                            quizeditor.putString("",quizModel.getQuiz_id());
                            quizeditor.putString("",quizModel.getQuiz_category());
                            quizeditor.putString("",quizModel.getCreated_at());
                            quizeditor.putString("",quizModel.getUpdated_at());
                            quizeditor.apply();

                        }else{

                            Toast.makeText(HomeActivity.this, startQuizResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<StartQuizResponse> call, Throwable t) {
                        dialog.dismiss();


                    }
                });


            }
        });



    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void showDiag() {
        final View dialogView = View.inflate(this,R.layout.nav_drawer,null);
        final Dialog dialog = new Dialog(this,R.style.MyAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        ImageView imageView = dialog.findViewById(R.id.close_drawer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealShow(dialogView, false, dialog);
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShow(dialogView, true, null);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){
                    revealShow(dialogView, false, dialog);
                    return true;
                }
                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void revealShow(View dialogView, boolean b, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.nav_drawer);
        View actionbar = getSupportActionBar().getCustomView();
        final ImageView fab = actionbar.findViewById(R.id.drawer_toogle);

        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (fab.getX() + fab.getWidth());
        int cy = (int) (fab.getY())+ (fab.getHeight()/2);

        if(b){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(500);
            revealAnimator.start();
        } else {
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(500);
            anim.start();
        }
    }

    private Bitmap getReducedBitmap(int albumArtResId) {
        // reduce image size in memory to avoid memory errors
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 8;
        return BitmapFactory.decodeResource(getResources(), albumArtResId, options);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startQuiz(StartQuizEvent event)
    {
        if(event.isStart()){
             Intent intent = new Intent(HomeActivity.this,QuestionActivity.class);
             startActivity(intent);
             overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
             finish();
        }else{
            Toast.makeText(this, "Failed to start Quiz", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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
}
