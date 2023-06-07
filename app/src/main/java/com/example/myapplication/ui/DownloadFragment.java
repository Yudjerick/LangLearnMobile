package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.FakeAPI;
import com.example.myapplication.data.FakeResponse;
import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.LessonsAPI;
import com.example.myapplication.data.Repository;
import com.example.myapplication.databinding.FragmentDownloadBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadFragment extends Fragment {

    public static String BASE_URL = "https://raw.githubusercontent.com/Yudjerick/FakeServer/main/";

    FragmentDownloadBinding binding;
    public DownloadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDownloadBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.postButton.setOnClickListener(view1 -> makeFakePostRequest());

        binding.syncButton.setOnClickListener(view12 -> syncLessonsWithServer());
    }

    private void makeFakePostRequest(){
        String baseUrl = "https://ptsv3.com/t/fakefakefake/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create()).build();
        FakeAPI api = retrofit.create(FakeAPI.class);
        FakeResponse body = new FakeResponse("post request");
        Call<FakeResponse> call = api.makeFakePostRequest(body);
        call.enqueue(new Callback<FakeResponse>() {
            @Override
            public void onResponse(Call<FakeResponse> call, Response<FakeResponse> response) {
                FakeResponse fakeResponse = response.body();
                Toast.makeText(getContext(), fakeResponse.hello, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<FakeResponse> call, Throwable t) {}
        });
    }

    public void syncLessonsWithServer(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LessonsAPI api = retrofit.create(LessonsAPI.class);
        Call<List<String>> idsCall = api.getAllIds();
        idsCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    for (String id: response.body()) {
                        downloadLessonFromServer(id);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {}
        });
    }

    public void downloadLessonFromServer(String taskId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LessonsAPI api = retrofit.create(LessonsAPI.class);
        Call<Lesson> lessonCall = api.loadLesson(taskId);
        lessonCall.enqueue(new Callback<Lesson>() {
            @Override
            public void onResponse(Call<Lesson> call, Response<Lesson> response) {
                if(response.isSuccessful()){
                    Lesson lesson = response.body();
                    Repository.addLesson(lesson);
                }
            }
            @Override
            public void onFailure(Call<Lesson> call, Throwable t) {}
        });
    }
}