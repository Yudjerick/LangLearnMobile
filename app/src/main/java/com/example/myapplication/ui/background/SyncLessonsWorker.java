package com.example.myapplication.ui.background;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.LessonsAPI;
import com.example.myapplication.data.Repository;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncLessonsWorker extends Worker {

    public static String BASE_URL = "https://raw.githubusercontent.com/Yudjerick/FakeServer/main/";

    public SyncLessonsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            syncLessonsWithServer();
            Log.i("Worker", "work is done");
        } catch (IOException e) {
            return Result.failure();
        }
        return Result.success();
    }

    public void syncLessonsWithServer() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LessonsAPI api = retrofit.create(LessonsAPI.class);
        Call<List<String>> idsCall = api.getAllIds();
        Response<List<String>> response = idsCall.execute();
        if(response.isSuccessful()){
            for (String id: response.body()) {
                downloadLessonFromServer(id);
            }
        }
    }

    public void downloadLessonFromServer(String taskId) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LessonsAPI api = retrofit.create(LessonsAPI.class);
        Call<Lesson> lessonCall = api.loadLesson(taskId);
        Response<Lesson> response = lessonCall.execute();
        if(response.isSuccessful()){
            Lesson lesson = response.body();
            Repository.addLesson(lesson);
        }
    }
}
