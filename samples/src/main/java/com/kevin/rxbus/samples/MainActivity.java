package com.kevin.rxbus.samples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.kevin.rxbus.RxBus;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

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
                RxBus.getDefault().post(s.toString(), 666);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        RxBus.getDefault().subscribe(new Predicate<RxBus.ObserverObject<String>>() {
            @Override
            public boolean test(@NonNull RxBus.ObserverObject<String> observerObj) throws Exception {
                return observerObj.tag == 666;
            }
        }, new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                textView.setText(s);
            }
        });
    }
}
