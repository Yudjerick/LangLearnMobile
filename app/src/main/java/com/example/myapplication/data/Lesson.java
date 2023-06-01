package com.example.myapplication.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.myapplication.data.converters.OrderTaskListConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Lesson {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String description;
    @TypeConverters({OrderTaskListConverter.class})
    public List<OrderTask> tasks;
    @ColumnInfo(name = "is_completed")
    public boolean isCompleted;
    public Lesson(){
        tasks = new ArrayList<>();
        id = "default";
        title = "default";
        description = "";
        isCompleted = false;
    }
    public Lesson(String id){
        tasks = new ArrayList<>();
        this.id = id;
        title = id;
        description = "";
        isCompleted = false;
    }
}
