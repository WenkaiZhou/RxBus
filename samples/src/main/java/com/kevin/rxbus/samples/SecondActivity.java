package com.kevin.rxbus.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kevin.rxbus.RxBus;
import com.kevin.rxbus.internal.RxBusConsumer;
import com.kevin.rxbus.internal.RxBusPredicate;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhouwenkai on 2017/5/8.
 */

public class SecondActivity extends AppCompatActivity {
    TextView textView1;
    TextView textView2;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        compositeDisposable = new CompositeDisposable();

        textView1 = (TextView) this.findViewById(R.id.tv1);
        textView2 = (TextView) this.findViewById(R.id.tv2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Disposable disposable1 = RxBus.getDefault().subscribeSticky(
                new RxBusPredicate<User>() {
                    @Override
                    public boolean test(@NonNull User user) throws Exception {
                        return user.name.equals("Kevin");
                    }
                },
                new RxBusConsumer<User>() {

                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        textView1.setText(String.format("name: %1$s, gender: %2$d, email: %3$s.",
                                user.name, user.age, user.email));
                    }
                }
        );

        Disposable disposable2 = RxBus.getDefault().subscribeSticky(
                new RxBusConsumer<User>() {

                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        textView2.setText(String.format("name: %1$s, gender: %2$d, email: %3$s.",
                                user.name, user.age, user.email));
                    }
                }
        );

        compositeDisposable.add(disposable1);
        compositeDisposable.add(disposable2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        RxBus.getDefault().removeSticky(User.class);
    }
}
