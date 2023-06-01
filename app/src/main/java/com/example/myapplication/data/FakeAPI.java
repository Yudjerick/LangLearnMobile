package com.example.myapplication.data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FakeAPI {
    @POST("post/")
    Call<FakeResponse> makeFakePostRequest(@Body FakeResponse body);
}
