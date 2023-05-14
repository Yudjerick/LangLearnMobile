package com.example.myapplication.data;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Lesson.class}, version = 1)
public abstract class LessonDataBase extends RoomDatabase {
    public abstract Dao dao();
    private static volatile LessonDataBase INSTANCE;
    static LessonDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LessonDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LessonDataBase.class, "lesson").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
