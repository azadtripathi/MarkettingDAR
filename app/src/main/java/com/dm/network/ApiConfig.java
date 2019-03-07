package com.dm.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiConfig {
    @Headers("Content-Type: application/json")
    @POST("send-otp")
    Call<String> sendOtp(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("sign-up")
    Call<String> registerUser(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("validate-otp")
    Call<String> validateOtp(@Body String body);

}
