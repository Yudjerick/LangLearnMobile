package com.example.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Query("SELECT * FROM lesson")
    List<Lesson> getAll();

    @Query("SELECT * FROM lesson")
    LiveData<List<Lesson>> getAllLiveData();

    @Insert
    void insertAll(Lesson... lessons);

    @Query("UPDATE lesson SET tasks = :updatedTasksJson WHERE id = :targetId")
    void updateTasks(String targetId, String updatedTasksJson);

    @Query("UPDATE lesson SET is_completed = 1 where id = :targetId")
    void updateIsCompleted(String targetId);

    @Delete
    void delete(Lesson lesson);

    @Query("DELETE FROM lesson")
    void nukeTable();
}
