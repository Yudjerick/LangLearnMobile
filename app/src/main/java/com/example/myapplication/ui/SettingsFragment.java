package com.example.myapplication.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSettingsBinding;

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
            

        });
    }
}