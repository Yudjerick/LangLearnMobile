package com.example.myapplication.ui.listutility;

import com.example.myapplication.R;
import com.example.myapplication.data.OrderTask;

public class TaskItem {
    private OrderTask task;
    private boolean isCompleted;

    public TaskItem(OrderTask task, boolean isCompleted) {
        this.task = task;
        this.isCompleted = isCompleted;
    }
    public String getText(){
        return task.getTranslatedPhrase();
    }

    public int getImageId(){
        if(isCompleted)
            return R.drawable.completed_icon;
        else return R.drawable.incompleted_icon;
    }

    public OrderTask getTask() {
        return task;
    }

    public void setTask(OrderTask task) {
        this.task = task;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
