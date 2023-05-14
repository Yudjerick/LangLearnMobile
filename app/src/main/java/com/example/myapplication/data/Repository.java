package com.example.myapplication.data;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    public static final List<OrderTaskData> tasks = new ArrayList<>();
    public static final List<Lesson> lessons = new ArrayList<>();
    public static List<OrderTask> getOrderTasks(){
        ArrayList<OrderTask> orderTasks = new ArrayList<>();
        for (OrderTaskData a: tasks) {
            orderTasks.add(a.getOrderTask());
        }
        return orderTasks;
    }
}
