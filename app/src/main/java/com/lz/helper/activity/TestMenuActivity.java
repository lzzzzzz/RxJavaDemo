package com.lz.helper.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lz.helper.R;

public class TestMenuActivity extends AppCompatActivity {


    public static Intent getIntent(Activity act){
        return new Intent(act,TestMenuActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_menu_activity);
    }

    /**RxJava基础篇测试*/
    public void onTest1Click(View view) {
        startActivity(RxJavaTest1Activity.getIntent(this));
    }

    /**RxJava操作符测试*/
    public void onTest2Click(View view) {
        startActivity(RxJavaTest2Activity.getIntent(this));
    }

    /**RxJava线程测试*/
    public void onTest3Click(View view) {
        startActivity(RxJavaTest3Activity.getIntent(this));
    }
}
