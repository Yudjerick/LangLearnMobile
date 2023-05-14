package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.Lesson;
import com.example.myapplication.data.OrderTask;
import com.example.myapplication.data.OrderTaskData;
import com.example.myapplication.data.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository.setApplication(getApplication());

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        String s = "Он обещал закончить проект через неделю";
        String[] words = {"He","promised","to", "finish", "the", "project", "in", "one", "week"};
        String[] additional = {"day", "she"};
        OrderTask task1 = new OrderTask(s,words, additional);
        String[] words2 = {"I","like","bananas"};
        String[] additional2 = {"hate", "apples","We"};
        OrderTask task2 = new OrderTask("Я люблю бананы", words2, additional2);
        String[] words3 = {"He","prefers","pineapples"};
        String[] additional3 = {"like", "apples","She", "oranges"};
        OrderTask task3 = new OrderTask("Он предпочитает ананасы", words3, additional3);
        String[] words4 = {"She","wants","pears"};
        String[] additional4 = {"pear", "apples","Her", "want"};
        OrderTask task4 = new OrderTask("Она хочет груши", words4, additional4);

        Lesson lesson = new Lesson();
        lesson.id = "1";
        lesson.isCompleted = false;
        lesson.description = "Try to save lesson";
        lesson.title = "Lesson 1. Test";
        lesson.tasks = new ArrayList<>();
        lesson.tasks.add(task2);
        lesson.tasks.add(task3);
        lesson.tasks.add(task4);
        lesson.tasks.add(task1);
        Log.i("AAAA", "onCreate: ");
        Repository.addLesson(lesson);
        Log.i("AAAA", Repository.getLessons().toString());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment_container);

        switch (item.getItemId()){
            case R.id.nav_lessons:
                navController.navigate(R.id.taskSelectionFragment);
                break;
            case R.id.nav_editor:
                navController.navigate(R.id.editorFragment);
                break;
            case R.id.nav_settings:
                navController.navigate(R.id.settingsFragment);
                break;
        }
        return true;
    }
}