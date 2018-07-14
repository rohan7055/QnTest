package com.example.qntest.restapi;


import com.example.qntest.restapi.models.StartQuizResponse;
import com.example.qntest.restapi.models.StatusModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rohan on 6/6/2018.
 */

public interface Api {


    @FormUrlEncoded
    @POST("register")
    Call<StatusModel> register(@Field("name") String name, @Field("username") String mobile,
                                      @Field("password") String password, @Field("admin") Boolean admin);

    @FormUrlEncoded
    @POST("startQuiz")
    Call<StartQuizResponse> startQuizRequest(@Field("quiz_id") Integer quiz_id,@Field("user_id") String mobile);

/*
    var user=new User({
            name:req.body.name,
    username:req.body.username,
    password:md5(req.body.password),
    admin:req.body.admin
});*/

}
