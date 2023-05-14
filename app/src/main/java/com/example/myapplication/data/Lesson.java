package com.example.myapplication.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.myapplication.data.converters.OrderTaskListConverter;

import java.util.List;

@Entity
public class Lesson {
    @PrimaryKey
    public int id;
    public String title;
    public String description;
    @TypeConverters({OrderTaskListConverter.class})
    public List<OrderTask> tasks;
    public boolean isCompleted;
}
