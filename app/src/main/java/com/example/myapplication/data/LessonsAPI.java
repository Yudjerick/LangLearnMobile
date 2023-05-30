package com.example.myapplication.data;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LessonsAPI {
    @GET("lessons/{id}.json")
    Call<Lesson> loadLesson(@Path("id") String lessonId);

    @GET("manifest.json")
    Call<List<String>> getAllIds();
}
