package com.example.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.LessonsAPI;
import com.example.myapplication.data.Repository;
import com.example.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.viewmodels.OrderTaskViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SettingsFragment extends Fragment {



    public static String BASE_URL = "https://raw.githubusercontent.com/Yudjerick/FakeServer/main/";
    FragmentSettingsBinding binding;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.syncButton.setOnClickListener(view12 -> syncLessonsWithServer());


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        boolean darkMode = sharedPreferences.getBoolean("dark", false);

        if(darkMode){
            binding.darkSwitch.setChecked(true);
        }

        binding.darkSwitch.setOnClickListener(view1 -> {
            if (binding.darkSwitch.isChecked()){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            sharedPreferences.edit().putBoolean("dark", binding.darkSwitch.isChecked()).apply();
        });

        binding.nukeDatabaseBtn.setOnClickListener(view12 -> Repository.nukeDataBase());

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