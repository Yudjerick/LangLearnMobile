package com.example.myapplication.data.converters;

import androidx.room.TypeConverter;

import com.example.myapplication.data.OrderTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class OrderTaskListConverter {
    @TypeConverter
    public String fromOrderTaskList(List<OrderTask> orderTaskList){
        Gson gson = new Gson();
        return gson.toJson(orderTaskList);
    }
    @TypeConverter
    public List<OrderTask> fromString(String json){
        if (json == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<OrderTask>>() {}.getType();

        Gson gson = new Gson();
        return gson.fromJson(json, listType);
    }
}
