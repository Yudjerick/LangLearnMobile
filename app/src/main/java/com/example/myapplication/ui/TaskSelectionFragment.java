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
import com.example.myapplication.ui.listutility.TaskItem;
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
        List<TaskItem> items = new ArrayList<>();
        String s = "Он обещал закончить проект через неделю";
        String[] words = {"He","promised","to", "finish", "the", "project", "in", "one", "week"};
        String[] additional = {"day", "she"};
        OrderTask task1 = new OrderTask(s,words, additional);
        String[] words2 = {"I","like","bananas"};
        String[] additional2 = {"hate", "apples","We"};
        OrderTask task2 = new OrderTask("Я люблю бананы", words2, additional2);
        items.add(new TaskItem(task1, true));
        items.add(new TaskItem(task2, false));

        RecyclerView taskList = view.findViewById(R.id.task_list);
        TaskListAdapter adapter = new TaskListAdapter(getContext(), items);
        TaskListAdapter.context = getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        taskList.setLayoutManager(layoutManager);
        taskList.setAdapter(adapter);


    }
}