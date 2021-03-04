package com.example.baseslicedemo.activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;

import com.lee.project.activity.SliceActivity;
import com.lee.project.slice.BaseSlice;

import java.util.List;

public class DemoSliceActivity extends SliceActivity {
    @NonNull
    @Override
    protected List<BaseSlice> createSlices() {

        return null;
    }

    @Override
    protected void layoutSlice(BaseSlice slice, ConstraintSet set, int viewId) {

    }

    @Override
    protected void onInited() {

    }
}
