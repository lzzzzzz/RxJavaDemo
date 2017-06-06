package com.lz.helper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lz.helper.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(TestMenuActivity.getIntent(MainActivity.this));
    }
}
