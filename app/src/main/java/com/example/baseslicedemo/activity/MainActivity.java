package com.example.baseslicedemo.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baseslicedemo.BuildConfig;
import com.example.baseslicedemo.R;


public class MainActivity extends AppCompatActivity {
    private TextView mTvMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTvMessage =  findViewById(R.id.tv_message);
        mTvMessage.setText("app的名字：" + getString(R.string.app_name) + "\n渠道：" + BuildConfig.channel);
    }
}
