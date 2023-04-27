package com.example.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.OrderTask;

import java.util.ArrayList;
import java.util.List;

public class OrderTaskViewModel extends ViewModel {
    private OrderTask task;
    private MutableLiveData<List<String>> answer;
    private MutableLiveData<List<String>> bank;

    public OrderTaskViewModel(OrderTask task){
        this.task = task;
        answer = new MutableLiveData<>(new ArrayList<>());
        bank = new MutableLiveData<>(task.getVariants());
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
}
