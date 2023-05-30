package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
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
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
        binding.shareLesson.setOnClickListener(view1 -> {
            Lesson lesson = Repository.getLessonByID(binding.lessonNameInput.getText().toString());
            shareLessonAsText(lesson);
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
        //saveTaskToFile(task);
    }

    private void saveTaskToFile(OrderTask task){

        Gson gson = new Gson();
        String json = gson.toJson(task);
        File externalStorage = Environment.getExternalStorageDirectory();
        File fileShared = new File(externalStorage, "task.txt");
        try {
            FileOutputStream fout = new FileOutputStream(fileShared);
            fout.write(json.getBytes());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareLessonAsText(Lesson lesson){
        Gson gson = new Gson();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {"denispetrenko2004@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Task");
        emailIntent.putExtra(Intent.EXTRA_TEXT, gson.toJson(lesson));
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(emailIntent, null));
    }

    //not working
    private void shareTaskAsFile(){
        File file = new File(Environment.getExternalStorageDirectory() + "/task.txt");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                {"denispetrenko2004@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Task");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("content://" + file.getAbsolutePath()));
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(emailIntent, null));
    }
}