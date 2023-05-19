package com.example.myapplication.ui;

import static java.net.Proxy.Type.HTTP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSettingsBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try{
            readFiles();
        }catch (Exception ignored){

        }


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        boolean darkMode = sharedPreferences.getBoolean("dark", false);

        if(darkMode){
            binding.getRoot().setBackgroundColor(getResources().getColor(R.color.dark_background));
            binding.darkSwitch.setChecked(true);
        }

        binding.darkSwitch.setOnClickListener(view1 -> {
            if (binding.darkSwitch.isChecked()){
                binding.getRoot().setBackgroundColor(getResources().getColor(R.color.dark_background));
            }
            else {
                binding.getRoot().setBackgroundColor(getResources().getColor(R.color.white));
            }
            sharedPreferences.edit().putBoolean("dark", binding.darkSwitch.isChecked()).apply();

            saveFiles();
        });
    }

    private void saveFiles(){
        File file = new File(getContext().getFilesDir(), "file.txt");
        try {
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(binding.appSpecificData.getText().toString().getBytes());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File externalStorage = getContext().getExternalFilesDir("storage");
        File fileShared = new File(externalStorage, "file_shared.txt");
        try {
            FileOutputStream fout = new FileOutputStream(fileShared);
            fout.write(binding.sharedData.getText().toString().getBytes());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFiles(){
        try {
            File file = new File(getContext().getFilesDir(), "file.txt");
            FileInputStream fin = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fin.read(data);
            fin.close();
            binding.appSpecificData.setText(new String(data, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File externalStorage = getContext().getExternalFilesDir("storage");
            File fileShared = new File(externalStorage, "file_shared.txt");
            FileInputStream fin = new FileInputStream(fileShared);
            byte[] data = new byte[(int) fileShared.length()];
            fin.read(data);
            fin.close();
            binding.sharedData.setText(new String(data, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}