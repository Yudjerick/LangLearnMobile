package com.example.myapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.OrderTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderTaskViewModel extends ViewModel {
    private OrderTask task;
    private final MutableLiveData<List<String>> answer;
    private final MutableLiveData<List<String>> bank;

    public OrderTaskViewModel(OrderTask task){
        this.task = task;
        answer = new MutableLiveData<>(new ArrayList<>());
        List<String> shuffled = task.getVariants();
        Collections.shuffle(shuffled);
        bank = new MutableLiveData<>(shuffled);
    }

    public OrderTaskViewModel() {
        answer = new MutableLiveData<>(new ArrayList<>());
        bank = new MutableLiveData<>(new ArrayList<>());
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
        return task;
    }

    public void setAnswer(List<String> words){
        answer.setValue(words);
    }

    public void clearAnswer(){
        answer.setValue(new ArrayList<>());
    }

    public void setTask(OrderTask task) {
        this.task = task;
        List<String> shuffled = task.getVariants();
        Collections.shuffle(shuffled);
        bank.setValue(shuffled);
    }
}
