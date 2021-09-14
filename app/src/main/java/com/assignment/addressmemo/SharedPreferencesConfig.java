package com.assignment.addressmemo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesConfig {
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public SharedPreferencesConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.assignment.addressmemo.Data_preferences", Context.MODE_PRIVATE);
    }

    public void writeDefaultAddress(int addressId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("defaultAddress", addressId);
        editor.apply();
    }

    public int readDefaultAddress() {
        return sharedPreferences.getInt("defaultAddress",0);
    }
}