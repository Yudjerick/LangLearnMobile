package com.example.myapplication.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Query("SELECT * FROM lesson")
    List<Lesson> getAll();

    @Insert
    void insertAll(Lesson... lessons);

    @Query("UPDATE lesson SET tasks = :updatedTasksJson WHERE id = :targetId")
    void updateTasks(String targetId, String updatedTasksJson);

    @Query("UPDATE lesson SET is_completed = 1 where id = :targetId")
    void updateIsCompleted(String targetId);

    @Delete
    void delete(Lesson lesson);
}
