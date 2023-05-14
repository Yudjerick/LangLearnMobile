package com.example.myapplication.data;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrderTaskData {
    @Embedded
    public OrderTask orderTask;
    public boolean isCompleted;
    public OrderTaskData(OrderTask orderTask, boolean isCompleted) {
        this.orderTask = orderTask;
        this.isCompleted = isCompleted;
    }
    public OrderTaskData(OrderTask orderTask){
        this.orderTask = orderTask;
        isCompleted = false;
    }
    public OrderTaskData(){

    }
    public OrderTask getOrderTask() {
        return orderTask;
    }
    public void setOrderTask(OrderTask orderTask) {
        this.orderTask = orderTask;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
