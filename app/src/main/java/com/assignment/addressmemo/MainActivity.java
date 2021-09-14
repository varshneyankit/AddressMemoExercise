package com.assignment.addressmemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.errorMessage.observe(this, this::showSnackbar);
    }
    public void showSnackbar(String message){
        Snackbar.make(findViewById(R.id.main_activity_root) , message, Snackbar.LENGTH_SHORT).show();
    }
}