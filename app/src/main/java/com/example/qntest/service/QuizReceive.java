package com.example.qntest.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.qntest.ConnectivityReceiver;
import com.example.qntest.MyApplication;
import com.example.qntest.events.ActiveUserEvent;
import com.example.qntest.events.ConnectivityEvent;
import com.example.qntest.events.GetActiveUserCall;
import com.example.qntest.events.QuestionEvent;
import com.example.qntest.events.QuizOverEvent;
import com.example.qntest.events.StartQuizEvent;
import com.example.qntest.restapi.models.QuestionModel;
import com.example.qntest.restapi.models.QuizModel;
import com.example.qntest.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by rohan on 6/24/2018.
 */

public class QuizReceive extends Service implements ConnectivityReceiver.ConnectivityReceiverListener {

    private Socket mSocket;
    private final static String TOKEN = "domilearquizapp";
    EventBus eventBus;
    Handler handler;
    SharedPreferences preferences;
    Boolean retry;
    String socket_id;
    ProgressDialog dialog;


    @Override
    public void onCreate() {

        super.onCreate();

        eventBus = EventBus.getDefault();
        eventBus.register(QuizReceive.this);
        handler = new Handler();
        retry=true;


        try {
            mSocket = IO.socket("http://myrhband.tech:4444/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        MyApplication.getInstance().setConnectivityListener(this);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        preferences = getSharedPreferences("USER", MODE_PRIVATE);
        socket_id=preferences.getString("mobile","");
        //socket_id="7055743169";


        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("authenticate", OnAuthentication);
        mSocket.on("question", onQuestion);
        mSocket.on("quizover", onQuizOver);
        mSocket.on("activeusers", onActiveUsers);
        mSocket.on("initquiz", onInitQuiz);
        mSocket.connect();

        return START_STICKY;
    }


    private Emitter.Listener OnAuthentication = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Boolean isAuthenticated = (Boolean) args[0];
            if (isAuthenticated) {
                Log.i("yoloDomilearn", "Socket is authenticated");

            }
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("yoloDomilearn", "connected");
            try {
                sendToken();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void sendToken() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("username",socket_id);
        Log.i("yoloDomilearn_uname", "=" +socket_id);
        object.put("token", Utils.md5(TOKEN));
        mSocket.emit("authenticate", object);
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("yolodomilearn", "disconnected");

            if(retry){
                mSocket.connect();
            }

        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {


            if(retry){
                mSocket.connect();
            }
        }
    };

    private Emitter.Listener onInitQuiz=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object=(JSONObject)args[0];

            try {
                String userid=object.getString("user_id");
                if(userid.equals(socket_id)){
                    eventBus.post(new StartQuizEvent(true));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    private Emitter.Listener onQuestion=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object= (JSONObject) args[0];

            try {
                JSONObject result= (JSONObject) object.get("result");
                Log.i ("Question",object.getString("user_id"));
                Log.i ("Result",object.get("result").toString());
                Log.i ("VALUES",((JSONObject) object.get("result")).getString("question"));



                ArrayList<String> options = new Gson().fromJson(result.get("options").toString(), new TypeToken<List<String>>(){}.getType());


                QuestionModel questionModel =new QuestionModel(result.getInt("quiz_id"),
                        result.getString("_id"),
                        result.getString("question"),
                        result.getString("question_category"),
                        options,
                        result.getInt("answer"));
                QuestionEvent questionEvent=new QuestionEvent(questionModel);
                eventBus.post(questionEvent);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    private Emitter.Listener onQuizOver=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject object=(JSONObject)args[0];
            try {
                if (socket_id.equals(object.getString("user_id"))) {
                    eventBus.post(new QuizOverEvent(true));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    
    private Emitter.Listener onActiveUsers=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String size=args[0].toString();
            Log.i("ActiveUsers",size);
            Log.d("ActiveUsers","default value");

            try{
                int modsize=Integer.valueOf(size)-1;
                eventBus.post(new ActiveUserEvent(String.valueOf(modsize)));

            }catch (Exception e)
            {

            }



            
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected){
            retry=true;
            if(!mSocket.connected()){
                mSocket.connect();
                eventBus.post(new ConnectivityEvent(true));}
        }
        else{
            retry=false;

        }
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void dummyEvent(ConnectivityEvent event){}

    /*@Subscribe
    public void getActiveUser(GetActiveUserCall event) throws JSONException {
        JSONObject object=new JSONObject();
        object.put("status",true);
        if(!mSocket.connected()){
            mSocket.connect();
            mSocket.emit("getactiveusers",object);

        }else{
            mSocket.emit("getactiveusers",object);
        }
    }*/

}
