package com.lee.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import com.lee.project.R;
import com.lee.project.slice.BaseSlice;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lixuce
 * 实现复杂页面的activity基类
 */
public abstract class SliceActivity extends AppCompatActivity {

    public ConstraintLayout mRootView;
    //所有碎片的集合
    private List<BaseSlice> mSliceList;

    private LayoutInflater inflater;

    public LayoutInflater getLayoutInflater() {
        return inflater;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slice);
        initData(savedInstanceState);
    }


    protected void initData(@Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(this);
        mRootView = findViewById(R.id.id_root);
        mSliceList = new ArrayList<>();
        mSliceList.addAll(createSlices());
        for (BaseSlice item : mSliceList) {
            initSlice(item);
        }
        onInited();
    }
    //不需要在onInit里面添加  在有需要的时候在添加
    public void addSlice(BaseSlice item) {
        if (mSliceList != null && item != null) {
            mSliceList.add(item);
        }
    }
    //初始化单个碎片  给碎片设置位置等等
    public void initSlice(BaseSlice item) {
        if (!item.isAddSlice()) {
            return;
        }
        item.setActivity(this);
        View v = item.onCreateView(inflater, mRootView);
        mRootView.addView(v);

        ConstraintSet set = new ConstraintSet();
        set.clone(mRootView);
        layoutSlice(item, set, v.getId());
        set.applyTo(mRootView);
        item.onViewCreated(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //让每个碎片跟随activity的生命周期
        for (BaseSlice item : mSliceList) {
            item.onResume();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //让每个碎片跟随activity的生命周期
        for (BaseSlice item : mSliceList) {
            item.onNewIntent();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //让每个碎片跟随activity的生命周期
        for (BaseSlice item : mSliceList) {
            item.onPause();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //让每个碎片跟随activity的生命周期
        for (BaseSlice item : mSliceList) {
            item.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //让每个碎片跟随activity的生命周期
        if (mSliceList != null && mSliceList.size() > 0) {
            for (BaseSlice item : mSliceList) {
                item.onDestroy();
            }
            mSliceList.clear();
        }
    }


    //需要在打开页面的时候就显示的碎片 需要在这里初始化 并且添加到碎片集合里面
    protected abstract @NonNull List<BaseSlice> createSlices();

    //碎片的布局位置设置
    protected abstract void layoutSlice(BaseSlice slice, ConstraintSet set, int viewId);

    //所有的碎片都初始化完成  需要做某些操作的时候
    protected abstract void onInited();


    protected ConstraintSet getConstraintSet() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mRootView);

        return set;
    }

    protected void updateConstraintSet(ConstraintSet set) {
        set.applyTo(mRootView);
    }
}
