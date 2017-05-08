package com.kevin.rxbus.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.kevin.rxbus.RxBus;
import com.kevin.rxbus.internal.RxBusConsumer;
import com.kevin.rxbus.internal.RxBusPredicate;

import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) this.findViewById(R.id.et);
        final TextView textView = (TextView) this.findViewById(R.id.tv);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RxBus.getDefault().post(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RxBus.getDefault().subscribe(
                new RxBusPredicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return true;
                    }
                }, new RxBusConsumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        textView.setText(s);
                    }
                });

        RxBus.getDefault().subscribe(
                new RxBusPredicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer o) throws Exception {
                        return true;
                    }
                }, new RxBusConsumer<Integer>() {

                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.d("aaaa", "integer = " + integer);
                    }
                });
    }
}
