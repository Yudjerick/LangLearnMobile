package com.example.myapplication.data;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.data.converters.OrderTaskListConverter;
import com.example.myapplication.viewmodels.OrderTaskViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static Application application;


    public static List<Lesson> getLessons(){
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        List<Lesson> lessons = dao.getAll();
        return lessons;
    }
    public static Application getApplication() {
        return application;
    }

    public static void setLessonCompleted(String id){
        Lesson lesson = getLessonByID(id);
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        dao.updateIsCompleted(id);
    }

    public static void setApplication(Application application) {
        Repository.application = application;
    }

    public static boolean containsLesson(String id){
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        List<Lesson> lessons = dao.getAll();
        for (Lesson l:lessons) {
            if(l.id.equals(id)){
                return true;
            }
        }
        return false;
    }

    public static void addLesson(Lesson lesson){
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        if(!containsLesson(lesson.id))
            dao.insertAll(lesson);
    }

    public static void addTask(String lessonId, OrderTask task){
        Lesson lesson = getLessonByID(lessonId);
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        lesson.tasks.add(task);
        OrderTaskListConverter converter = new OrderTaskListConverter();
        dao.updateTasks(lessonId, converter.fromOrderTaskList(lesson.tasks));
    }

    public static void removeLesson(Lesson lesson){
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        dao.delete(lesson);
    }

    public static Lesson getLessonByID(String id){
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        List<Lesson>lessons = dao.getAll();
        for (Lesson l:lessons) {
            if(l.id.equals(id)){
                return l;
            }
        }
        return null;
    }

    public static void nukeDataBase(){
        LessonDataBase db = LessonDataBase.getDatabase(application);
        Dao dao = db.dao();
        dao.nukeTable();
    }


}
