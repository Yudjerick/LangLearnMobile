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
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.data.Repository;
import com.example.myapplication.ui.listutility.LessonItem;
import com.example.myapplication.ui.listutility.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskSelectionFragment extends Fragment {

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


    }
}