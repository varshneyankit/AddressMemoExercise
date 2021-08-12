package com.assignment.addressmemo;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SplashScreenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new Handler().postDelayed(this::navigateToDashboard, 2000);
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    private void navigateToDashboard() {
        NavHostFragment.findNavController(SplashScreenFragment.this)
                .navigate(R.id.action_SplashScreenFragment_to_DashboardFragment);
    }
}