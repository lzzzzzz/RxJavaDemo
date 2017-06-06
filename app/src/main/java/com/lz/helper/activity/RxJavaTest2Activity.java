package com.lz.helper.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lz.helper.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

/**
 * 此界面主要测试RxJava操作符
 * 注意：操作符不需要处理异常，交给订阅者来做
 * 将异常处理交给订阅者来做，一旦有调用链中有一个抛出了异常，就会直接执行onError()方法，停止数据传送。
 * */
public class RxJavaTest2Activity extends AppCompatActivity {

    private TextView tv_content;

    public static Intent getIntent(Activity act){
        return new Intent(act,RxJavaTest2Activity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_test2);
        tv_content= (TextView) this.findViewById(R.id.tv_content);

        test1();//map操作符
        test2();//lambda简写格式
        test3();//flatmap
        test4();//fromIterable
        test5();//filter
        test6();//take
        test7();//doOnNext
        test8();//zip操作符
        test9();//sample操作符
        test10();//scan操作符
    }

    /**map操作符
     * cast：cast操作符类似于map操作符，不同的地方在于map操作符可以通过自定义规则，
     * 把一个值A1变成另一个值A2，A1和A2的类型可以一样也可以不一样；而cast操作符主要是做类型转换的，
     * 传入参数为类型class，如果源Observable产生的结果不能转成指定的class，则会抛出ClassCastException运行时异常
     * */
    private void test1(){
        Flowable.just("hello world").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s+"1";
            }
        }).subscribe(s -> showText(s));
    }

    /**简写*/
    private void test2(){
        Flowable.just("hello world").map(s -> s+"2").subscribe(s -> showText(s));
    }

    /**flatmap 转换flowable
     * FlatMap：将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里
     * concatMap： 它和flatMap的作用几乎一模一样, 只是它的结果是严格按照上游发送的顺序来发送的
     * switchMap：switchMap操作符会保存最新的Observable产生的结果而舍弃旧的结果
     * buffer：buffer操作符周期性地收集源Observable产生的结果到列表中，并把这个列表提交给订阅者，订阅者处理后，清空buffer列表，
     * 同时接收下一次收集的结果并提交给订阅者，周而复始。
     * 需要注意的是，一旦源Observable在产生结果的过程中出现异常，即使buffer已经存在收集到的结果，订阅者也会马上收到这个异常，并结束整个过程。
     * */
    private void test3(){
        ArrayList<String> list=new ArrayList<String>();
        list.add("list1");
        list.add("list2");
        list.add("list3");
        Flowable.just(list)
                .flatMap(listData -> Flowable.fromIterable(listData))
                .subscribe(s -> showText(s));
    }

    /**fromIterable迭代器操作符*/
    private void test4(){
        ArrayList<String> list=new ArrayList<String>();
        list.add("list4");
        list.add("list5");
        list.add("list6");
        Flowable.fromIterable(list).subscribe(s -> showText(s));
    }

    /**filter 数据过滤*/
    private void test5(){
        Flowable.fromArray(2,6,0,3,19,1,-7,16)
                .filter(in -> in > 5 )
                .subscribe(s ->showText(s+""));
    }

    /**take 指定接收者接收数据个数*/
    private void test6(){
        Flowable.fromArray(2,6,0,3,19,1,-7,16)
                .take(2).filter(in -> in > 5 )
                .subscribe(s ->showText(s+""));
    }

    /**doOnNext 订阅者接收数据之前做的事情*/
    private void test7(){
        Flowable.fromArray(2,6,0,3,19,1,-7,16)
                .doOnNext(in -> in=in+1)
                .take(2)
                .filter(in -> in > 5 )
                .subscribe(s ->showText(s+""));
    }

    /**zip操作符
     *Zip通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件.
     * 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据。
     * 在同一个线程中剩余数据将继续发送但不接收；不同线程中complete后剩余数据不发送，没有complete则发送数据但不接收
     * zip使用队列缓存不同的发射数据流
     * */
    //结果：136；247；
    private void test8(){
        Flowable flowable1=Flowable.fromArray(1,2);
        Flowable flowable2=Flowable.fromArray(3,4,5);
        Flowable flowable3=Flowable.fromArray(6,7,8,9);
        Flowable.zip(flowable1, flowable2, flowable3, new Function3<Integer,Integer,Integer,String>() {//前三个参数对应flowable发射参数类型，第四个对应转换数据类型
            @Override
            public String apply(Integer o, Integer o2, Integer o3) throws Exception {
                return o+""+o2+""+o3+";";
            }
        }).subscribe(s -> showText(s+""));
    }

    /**sample操作符
     * 每隔指定的时间发送一次数据（注意此处的数据为在指定时间间隔时对发射源数据采样，若此时发射源未发射数据则采集到的数据为空）
     * */
    private void test9(){
       Observable.fromArray(10,11,12,13,14,15)
               .sample(2, TimeUnit.SECONDS)//每隔2秒采集数据源正在发送的数据交给订阅者
               .subscribe(s -> showText(s+""));
    }

    /**scan
     * scan操作符通过遍历源Observable产生的结果，依次对每一个结果项按照指定规则进行运算，计算后的结果作为下一个迭代项参数，
     * 每一次迭代项都会把计算结果输出给订阅者
     * */
    private void test10(){
        Flowable.fromArray(1,2,3,4,5).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer sum, Integer item) throws Exception {
                return sum+item;
            }
        }).subscribe(s -> showText(s+""));
    }
    private void showText(String s){
        String co=tv_content.getText().toString().trim();
        co=(null==co?"":co);
        tv_content.setText(co+" "+s);
    }

    /*后记：
    * RxJava操作符有很多另外未包括的以下介绍“一部分”
    * window：window操作符非常类似于buffer操作符，区别在于buffer操作符产生的结果是一个List缓存，
    *         而window操作符产生的结果是一个Observable，订阅者可以对这个结果Observable重新进行订阅处理。
    * debounce：操作符对源Observable每产生一个结果后，如果在规定的间隔时间内没有别的结果产生，则把这个结果提交给订阅者处理，否则忽略该结果。
    *           值得注意的是，如果源Observable产生的最后一个结果后在规定的时间间隔内调用了onCompleted，那么通过debounce操作符也会把这个结果提交给订阅者。
    * distinct：distinct操作符对源Observable产生的结果进行过滤，把重复的结果过滤掉，只输出不重复的结果给订阅者，非常类似于SQL里的distinct关键字。
    * elementAt：elementAt操作符在源Observable产生的结果中，仅仅把指定索引的结果提交给订阅者，索引是从0开始的。
    * ofType：ofType操作符类似于filter操作符，区别在于ofType操作符是按照类型对结果进行过滤（例如：.ofType(Float.class)）
    * first：first操作符是把源Observable产生的结果的第一个提交给订阅者，first操作符可以使用elementAt(0)和take(1)替代。
    * single：single操作符是对源Observable的结果进行判断，如果产生的结果满足指定条件的数量不为1，则抛出异常，否则把满足条件的结果提交给订阅者
    * last：last操作符把源Observable产生的结果的最后一个提交给订阅者，last操作符可以使用takeLast(1)替代。
    * ignoreElements：ignoreElements操作符忽略所有源Observable产生的结果，只把Observable的onCompleted和onError事件通知给订阅者。
    *                 ignoreElements操作符适用于不太关心Observable产生的结果，只是在Observable结束时(onCompleted)或者出现错误时能够收到通知。
    * sample：sample操作符定期扫描源Observable产生的结果，在指定的时间间隔范围内对源Observable产生的结果进行采样
    * skip：skip操作符针对源Observable产生的结果，跳过前面n个不进行处理，而把后面的结果提交给订阅者处理
    * skipLast：skipLast操作符针对源Observable产生的结果，忽略Observable最后产生的n个结果，而把前面产生的结果提交给订阅者处理，
    *           值得注意的是，skipLast操作符提交满足条件的结果给订阅者是存在延迟效果的
    * takeFirst：takeFirst操作符类似于take操作符，同时也类似于first操作符，都是获取源Observable产生的结果列表中符合指定条件的前一个或多个，
    *            与first操作符不同的是，first操作符如果获取不到数据，则会抛出NoSuchElementException异常，
    *            而takeFirst则会返回一个空的Observable，该Observable只有onCompleted通知而没有onNext通知。
    * takeLast：takeLast操作符是把源Observable产生的结果的后n项提交给订阅者，提交时机是Observable发布onCompleted通知之时。
    * interval：interval操作符发送Long型的事件, 从0开始, 每隔指定的时间就把数字加1并发送出来,2.0版本需要配合背压解决策略
    *           onBackpressureBuffer()
    *           onBackpressureDrop()
    *           onBackpressureLatest()
    * */
}
