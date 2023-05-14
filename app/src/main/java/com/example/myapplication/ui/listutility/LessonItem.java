package com.example.myapplication.ui.listutility;

import com.example.myapplication.R;
import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.data.OrderTaskData;

public class LessonItem {
    private Lesson lesson;

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    private boolean isCompleted;

    public LessonItem(Lesson lesson){
        this.lesson = lesson;
        this.isCompleted = lesson.isCompleted;
    }

    public String getText(){
        return lesson.title;
    }

    public int getImageId(){
        if(isCompleted)
            return R.drawable.roadmap_level_done;
        else return R.drawable.roadmap_level_current;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
