package com.example.myapplication.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        List<LessonItem> items = new ArrayList<>();

        RecyclerView taskList = view.findViewById(R.id.task_list);
        TaskListAdapter adapter = new TaskListAdapter(getContext(), items);
        TaskListAdapter.context = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        taskList.setLayoutManager(layoutManager);
        taskList.setAdapter(adapter);

        Repository.getLessonsLiveData().observe(getViewLifecycleOwner(), lessons -> updateList(adapter, items));
    }

    private void updateList(TaskListAdapter adapter, List<LessonItem> items){
        List<String> lessonIds = new ArrayList<>();
        for (int j = 0; j < items.size(); j++) {
            lessonIds.add(items.get(j).getLesson().id);
        }
        for (int i = 0; i < Repository.getLessons().size(); i++) {
            if(!lessonIds.contains(Repository.getLessons().get(i).id))
                items.add(new LessonItem(Repository.getLessons().get(i)));
        }
        adapter.notifyItemInserted(items.size() - 1);
    }
}