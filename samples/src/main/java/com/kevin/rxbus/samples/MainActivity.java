package com.kevin.rxbus.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kevin.rxbus.RxBus;
import com.kevin.rxbus.internal.RxBusConsumer;
import com.kevin.rxbus.internal.RxBusPredicate;

import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    TextView tvName;
    TextView tvAge;
    TextView tvEmail;
    EditText etName;
    EditText etAge;
    EditText etEmail;
    TextView tvShow;
    Button btSendSticky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = (TextView) this.findViewById(R.id.tv_name);
        tvAge = (TextView) this.findViewById(R.id.tv_age);
        tvEmail = (TextView) this.findViewById(R.id.tv_email);
        etName = (EditText) this.findViewById(R.id.et_name);
        etAge = (EditText) this.findViewById(R.id.et_age);
        etEmail = (EditText) this.findViewById(R.id.et_email);
        tvShow = (TextView) this.findViewById(R.id.tv_show);
        btSendSticky = (Button) this.findViewById(R.id.bt_send_sticky);

        etName.addTextChangedListener(this);
        etAge.addTextChangedListener(this);
        etEmail.addTextChangedListener(this);
        btSendSticky.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RxBus.getDefault().subscribe(
                new RxBusPredicate<User>() {
                    @Override
                    public boolean test(@NonNull User user) throws Exception {
                        tvShow.setText("");
                        // Display the information only when all is not empty.
                        return !TextUtils.isEmpty(user.name)
                                && user.age != 0
                                && !TextUtils.isEmpty(user.email);
                    }
                }, new RxBusConsumer<User>() {
                    @Override
                    public void accept(@NonNull User user) throws Exception {
                        tvShow.setText(String.format("name: %1$s, gender: %2$d, email: %3$s.",
                                user.name, user.age, user.email));
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String name = etName.getText().toString();
        int age = 0;
        if (!TextUtils.isEmpty(etAge.getText().toString())) {
            age = Integer.parseInt(etAge.getText().toString());
        }
        String email = etEmail.getText().toString();

        // Send
        RxBus.getDefault().post(new User(name, age, email));
    }

    @Override
    public void afterTextChanged(Editable s) {
        // do nothing
    }

    @Override
    public void onClick(View v) {
        String name = etName.getText().toString();
        int age = 0;
        if (!TextUtils.isEmpty(etAge.getText().toString())) {
            age = Integer.parseInt(etAge.getText().toString());
        }
        String email = etEmail.getText().toString();

        // Send Sticky
        RxBus.getDefault().postSticky(new User(name, age, email));
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }
}
