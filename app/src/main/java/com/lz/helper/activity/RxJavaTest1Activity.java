package com.lz.helper.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lz.helper.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RxJavaTest1Activity extends AppCompatActivity {

    private TextView tv_content;

    public static Intent getIntent(Activity act){
        return new Intent(act,RxJavaTest1Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test1);
        tv_content= (TextView) this.findViewById(R.id.tv_content);
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
    }

    /**1.0版方法
     * observable对应observer
     * */
    private void test1() {
        Observable<String> obervable=Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("hello world1\n");
                e.onComplete();
            }});

        Observer subscriber=new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String o) {
                String co=tv_content.getText().toString().trim();
                co=(null==co?"":co);
                tv_content.setText(co+" "+o);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        obervable.subscribe(subscriber);
    }

    /**2.0版方法
     * flowable对应subscriber
     * */
    private void test2() {
        Flowable<String> flowable=Flowable.create(new FlowableOnSubscribe<String>() {
            //此处e可以接受订阅者需要接收多少个请求参数onNext方法将减少个数
            //若数量减为零（订阅者没有了处理能力不能再接收参数）需要暂停发送数据，否则将抛出backpress异常
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("hello world2\n");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);//2.0版本可以采用flowable,第二个参数 BackpressureStrategy.BUFFER指明产生异常时如何处理
                                        // (BUFFER:增大发送信息存储量，若不及时处理将导致OOM；
                                        //  ERROR：超过最大信息存储量抛出异常；
                                        //  DROP：将超出的事件丢弃；
                                        //  LATEST：只保留最新的事件)

        Subscriber subscriber=new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {//此处Subscription对应1.0版本的Disposable
                //注意此处Long.MAX_VALUE也是2.0版本新增的方法，
                // 是flowable新增的响应式拉去策略用于解决发送接收不均衡造成的backpress问题方法之一
                //MAX_VALUE最大相应数据为128个
                //不写此响应池数据则默认不接收发送者数据（同一个线程内将抛出backpressure异常，
                // 不同线程内发送数据正常发送，但是不接收数据，发送数据超过缓存池大小将抛出backpressure异常）
                s.request(Long.MAX_VALUE);//指定接收多少个数据
            }

            @Override
            public void onNext(String o) {
                String co=tv_content.getText().toString().trim();
                co=(null==co?"":co);
                tv_content.setText(co+" "+o);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
            }
        };
        flowable.subscribe(subscriber);
    }

    /**1.0版本方法*/
    private void test3(){
        //2.0版本中just方法没有action1...,所以只能添加监听者
        Observable<String> observable=Observable.just("hello world3\n");
        Observer<String> observer=new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                String co=tv_content.getText().toString().trim();
                co=(null==co?"":co);
                tv_content.setText(co+" "+s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
    }

    /**2.0版本方法*/
    private void test4(){
        //just方法不能使用添加subscriber方法，onNext()方法不执行
        Flowable.just("hello world4\n").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                String co=tv_content.getText().toString().trim();
                co=(null==co?"":co);
                tv_content.setText(co+" "+s);
            }
        });
    }

    /**简写格式*/
    private void test5(){
        Flowable.just("hello world 5").subscribe(s -> showText(s));
    }

    /**订阅者处理过程
     * 能够知道什么时候订阅者已经接收了全部的数据。
     * */
    private void test6(){

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("hello world 6");
                int a=1/0;//出现异常不处理，交给订阅者处理
                e.onComplete();//订阅者接收处理数据完毕
            }
        },BackpressureStrategy.BUFFER).subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d("RxJava1_Test","-------------onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                Log.d("RxJava1_Test","-------------onNext");
                showText(s);
            }

            @Override
            public void onError(Throwable t) {
                Log.d("RxJava1_Test","-------------onError");
            }

            @Override
            public void onComplete() {
                Log.d("RxJava1_Test","-------------onComplete");
            }
        });
    }



    private void showText(String s){
        String co=tv_content.getText().toString().trim();
        co=(null==co?"":co);
        tv_content.setText(co+" "+s);
    }
}
