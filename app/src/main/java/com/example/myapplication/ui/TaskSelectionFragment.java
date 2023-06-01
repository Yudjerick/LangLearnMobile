package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.LessonsAPI;
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.data.Repository;
import com.example.myapplication.ui.listutility.LessonItem;
import com.example.myapplication.ui.listutility.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskSelectionFragment extends Fragment {

    public static String BASE_URL = "https://raw.githubusercontent.com/Yudjerick/FakeServer/main/";
    public TaskSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<LessonItem> items = new ArrayList<>();
        for (int i = 0; i < Repository.getLessons().size(); i++) {
            items.add(new LessonItem(Repository.getLessons().get(i)));
        }

        RecyclerView taskList = view.findViewById(R.id.task_list);
        TaskListAdapter adapter = new TaskListAdapter(getContext(), items);
        TaskListAdapter.context = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        taskList.setLayoutManager(layoutManager);
        taskList.setAdapter(adapter);

        //syncLessonsWithServer();
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