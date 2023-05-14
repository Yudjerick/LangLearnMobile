package com.example.myapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.OrderTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderTaskViewModel extends ViewModel {
    private final MutableLiveData<OrderTask> task;
    private Lesson lesson;
    private int taskIndex;

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
        setTask(lesson.tasks.get(0));
        taskIndex = 0;
        clearAnswer();
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    private final MutableLiveData<List<String>> answer;
    private final MutableLiveData<List<String>> bank;

    public OrderTaskViewModel(OrderTask task){
        this.task = new MutableLiveData<>(task);
        answer = new MutableLiveData<>(new ArrayList<>());
        List<String> shuffled = task.getVariants();
        Collections.shuffle(shuffled);
        bank = new MutableLiveData<>(shuffled);
    }

    public OrderTaskViewModel() {
        answer = new MutableLiveData<>(new ArrayList<>());
        bank = new MutableLiveData<>(new ArrayList<>());
        task = new MutableLiveData<>();
    }

    public LiveData<List<String>> getAnswer() {
        return answer;
    }

    public LiveData<List<String>> getBank() {
        return bank;
    }
    public void log(){
        Log.i("AAAA", "I'm alive");
    }

    public OrderTask getTask() {
        return task.getValue();
    }

    public LiveData<OrderTask> getTaskLiveData(){
        return task;
    }

    public void setAnswer(List<String> words){
        answer.setValue(words);
    }

    public void clearAnswer(){
        answer.setValue(new ArrayList<>());
    }

    public void setTaskSavingAnswer(OrderTask task) {
        this.task.setValue(task);
        List<String> shuffled = task.getVariants();
        Collections.shuffle(shuffled);
        bank.setValue(shuffled);
    }

    public void setTask(OrderTask task) {
        setTaskSavingAnswer(task);
        clearAnswer();
    }
}
