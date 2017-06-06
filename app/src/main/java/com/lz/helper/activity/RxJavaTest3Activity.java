package com.lz.helper.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lz.helper.R;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**RxJavva线程测试*/
public class RxJavaTest3Activity extends AppCompatActivity {

    private TextView tv_content;

    public static Intent getIntent(Activity act){
        return new Intent(act,RxJavaTest3Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test3);

        tv_content= (TextView) this.findViewById(R.id.tv_content);
        test1();
        test2();
    }

    /**线程测试*/
    private void test1(){
        Flowable.fromArray(1,2,3,4)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->showText(s+""));
    }

    /**发送数据线程可多次指定但第一次有效
     * 接收数据的线程可多次指定最后一次有效
     * doOnNext如果在observeOn之后则在observeOn指定的线程中执行
     * */
    private void test2(){
        Flowable.fromArray(5,6,7,8)
                .subscribeOn(Schedulers.newThread())
                .doOnNext(s -> Log.d("RxJava Test","--------------thread:"+Thread.currentThread().getName()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    showText(s+"");
                    Log.d("RxJava Test","--------------thread:"+Thread.currentThread().getName());
                });
    }

    private void showText(String s){
        String co=tv_content.getText().toString().trim();
        co=(null==co?"":co);
        tv_content.setText(co+" "+s);
    }
}
