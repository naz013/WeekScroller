package com.github.naz013.weekscroller.example;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.naz013.weekscroller.WeekScroller;

import org.joda.time.DateTime;

@SuppressLint("LogNotTimber")
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeekScroller weekScroller = findViewById(R.id.week_scroller);
        weekScroller.setDateRangeChangeListener(new WeekScroller.DateRangeChangeListener() {
            @Override
            public void onRangeChanged(@NonNull DateTime start, @NonNull DateTime end) {
                Log.d(TAG, "onRangeChanged: " + start + ", " + end);
            }
        });
        weekScroller.setDateSelectListener(new WeekScroller.DateSelectListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                Log.d(TAG, "onDateSelected: " + year + "/" + month + "/" + day);
            }
        });
        weekScroller.setDateTimeSelectListener(new WeekScroller.DateTimeSelectListener() {
            @Override
            public void onDateTimeSelected(@NonNull DateTime dateTime) {
                Log.d(TAG, "onDateTimeSelected: " + dateTime);
            }
        });
    }
}
