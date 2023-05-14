package com.example.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.data.OrderTaskData;
import com.example.myapplication.data.Repository;
import com.example.myapplication.databinding.FragmentEditorBinding;
import com.example.myapplication.viewmodels.OrderTaskViewModel;

import java.util.ArrayList;

public class EditorFragment extends Fragment {
    private FragmentEditorBinding binding;
    public EditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditorBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("dark", false)){
            binding.getRoot().setBackgroundColor(getResources().getColor(R.color.dark_background));
        }
        binding.saveTask.setOnClickListener(view1 -> {
            SaveTask();
        });
    }

    private void SaveTask(){
        OrderTask task = new OrderTask(binding.phraseToTranslateEditorInput.getText().toString(),
                binding.answerEditorInput.getText().toString().split(" "),
                binding.additionalVariantsEditorInput.getText().toString().split(" "));
        if(Repository.containsLesson(binding.lessonNameInput.getText().toString())){
            Repository.addTask(binding.lessonNameInput.getText().toString(), task);
        }
        else {
            Lesson lesson = new Lesson();
            lesson.isCompleted = false;
            lesson.id = binding.lessonNameInput.getText().toString();
            lesson.description = "Try to save lesson";
            lesson.title = lesson.id;
            lesson.tasks = new ArrayList<>();
            Repository.addLesson(lesson);
            SaveTask();
        }
    }
}