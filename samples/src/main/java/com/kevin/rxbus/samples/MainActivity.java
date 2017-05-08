package com.kevin.rxbus.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kevin.rxbus.RxBus;
import com.kevin.rxbus.internal.RxBusConsumer;
import com.kevin.rxbus.internal.RxBusPredicate;

import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) this.findViewById(R.id.et);
        textView = (TextView) this.findViewById(R.id.tv);
        final Button button = (Button) this.findViewById(R.id.btn);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RxBus.getDefault().post(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RxBus.getDefault().postSticky("zwenkai@foxmail.com");
                RxBus.getDefault().postSticky(new User("zwenkai1", "male", "zwenkai@foxmail.com"));
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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
    }
}
