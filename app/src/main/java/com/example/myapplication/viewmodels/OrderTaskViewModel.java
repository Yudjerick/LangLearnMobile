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
    private boolean isOnTaskScreen;
    private final MutableLiveData<OrderTask> task;
    private Lesson lesson;
    private int taskIndex;

    private boolean isRightAnswerBannerActive;

    private boolean isEndOfLesson;


    private final MutableLiveData<List<AnswerItem>> answer;
    private final MutableLiveData<List<BankItem>> bank;

    public boolean isRightAnswerBannerActive() {
        return isRightAnswerBannerActive;
    }

    public void setRightAnswerBannerActive(boolean rightAnswerBannerActive) {
        isRightAnswerBannerActive = rightAnswerBannerActive;
    }

    public boolean isEndOfLesson() {
        return isEndOfLesson;
    }

    public void setEndOfLesson(boolean endOfLesson) {
        isEndOfLesson = endOfLesson;
    }


    public boolean isOnTaskScreen() {
        return isOnTaskScreen;
    }

    public void setOnTaskScreen(boolean onTaskScreen) {
        isOnTaskScreen = onTaskScreen;
    }

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



    public OrderTaskViewModel(OrderTask task){
        this.task = new MutableLiveData<>(task);
        answer = new MutableLiveData<>(new ArrayList<>());
        List<String> shuffled = task.getVariants();
        List<BankItem> bankItems = new ArrayList<>();
        for (String word: shuffled) {
            bankItems.add(new BankItem(word));
        }
        Collections.shuffle(shuffled);
        bank = new MutableLiveData<>(bankItems);
    }

    public OrderTaskViewModel() {
        answer = new MutableLiveData<>(new ArrayList<>());
        bank = new MutableLiveData<>(new ArrayList<>());
        task = new MutableLiveData<>();
    }

    public LiveData<List<AnswerItem>> getAnswer() {
        return answer;
    }

    public LiveData<List<BankItem>> getBank() {
        return bank;
    }

    public OrderTask getTask() {
        return task.getValue();
    }

    public LiveData<OrderTask> getTaskLiveData(){
        return task;
    }

    public void setAnswer(List<AnswerItem> words){
        answer.setValue(words);
    }

    public void setBank(List<BankItem> bankItems){
        bank.setValue(bankItems);
    }

    public void clearAnswer(){
        answer.setValue(new ArrayList<>());
    }

    public void setTask(OrderTask task) {
        this.task.setValue(task);
        List<String> shuffled = task.getVariants();
        Collections.shuffle(shuffled);
        List<BankItem> bankItems = new ArrayList<>();
        for (String word: shuffled) {
            bankItems.add(new BankItem(word));
        }
        bank.setValue(bankItems);
        clearAnswer();
        isEndOfLesson = false;
        isRightAnswerBannerActive = false;
    }
}
