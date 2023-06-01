package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.net.Uri;
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
import com.example.myapplication.ui.background.SyncLessonsWorker;
import com.example.myapplication.viewmodels.OrderTaskViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository.setApplication(getApplication());
        //Repository.nukeDataBase();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(SyncLessonsWorker.class,
                15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("LessonsSync",
                ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);


        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        OrderTaskViewModel model = new ViewModelProvider(this).get(OrderTaskViewModel.class);

        switch (item.getItemId()){
            case R.id.nav_lessons:
                if(model.isOnTaskScreen()){
                    navController.navigate(R.id.orderTaskFragment);
                }else {
                    navController.navigate(R.id.taskSelectionFragment);
                }
                break;
            case R.id.nav_editor:
                navController.navigate(R.id.editorFragment);
                break;
            case R.id.nav_settings:
                navController.navigate(R.id.settingsFragment);
                break;
            case R.id.nav_download:
                navController.navigate(R.id.downloadFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                try{
                    Gson gson = new Gson();
                    Lesson lesson = gson.fromJson(sharedText, Lesson.class);
                    if(lesson!=null){
                        NavController navController = Navigation.findNavController(this,
                                R.id.nav_host_fragment_container);
                        navController.navigate(R.id.orderTaskFragment);
                        OrderTaskViewModel model = new ViewModelProvider(this).get(OrderTaskViewModel.class);
                        model.setOnTaskScreen(true);
                        model.setLesson(lesson);
                    }
                    Navigation.findNavController(this, R.id.nav_host_fragment_container)
                            .navigate(R.id.orderTaskFragment);
                }
                catch (Exception ignored){}
            }
        }
    }
}