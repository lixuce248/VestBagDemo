package com.lee.project.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.lee.project.R;
import com.lee.project.activity.SliceActivity;
import com.lee.project.slice.BaseSlice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 碎片fragment base
 * @author lixuce
 */
public abstract class SliceFragment<T extends SliceActivity> extends BaseFragment {

    ConstraintLayout mRootView;

    private List<BaseSlice> mSliceList;

    public List<BaseSlice> getmSliceList() {
        return mSliceList;
    }

    public void setmSliceList(List<BaseSlice> mSliceList) {
        this.mSliceList = mSliceList;
    }

    private LayoutInflater inflater;

    private Map<String, Object> cacheMap = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_slice;
    }

    @Override
    protected void initData() {
        mRootView=rootView.findViewById(R.id.id_root);
        inflater = getLayoutInflater();
        onInit();

        mSliceList = new ArrayList<BaseSlice>();
        mSliceList.addAll(createSlices());

        Map<BaseSlice, View> viewMap = new HashMap<>();

        for(BaseSlice item:mSliceList) {
            item.setActivity((T) getActivity());
            View v = item.onCreateView(inflater, mRootView);
            mRootView.addView(v);
            viewMap.put(item, v);
        }

        int i = 0;
        for(BaseSlice item:mSliceList) {
            View v = viewMap.get(item);
            ConstraintSet set = new ConstraintSet();
            set.clone(mRootView);
            layoutSlice(item, set, v.getId(), i);
            set.applyTo(mRootView);
            i++;
        }

        for(BaseSlice item:mSliceList) {
            View v = viewMap.get(item);
            item.onViewCreated(v);
        }

        onInited();
    }

    @Override
    public void onResume() {
        super.onResume();

        for(BaseSlice item:mSliceList) {
            item.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        for(BaseSlice item:mSliceList) {
            item.onPause();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for(BaseSlice item:mSliceList) {
            item.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mSliceList != null && mSliceList.size() > 0){
            for(BaseSlice item:mSliceList) {
                item.onDestroy();
            }
        }
    }

    protected ConstraintSet getConstraintSet() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mRootView);

        return set;
    }

    protected void updateConstraintSet(ConstraintSet set) {
        set.applyTo(mRootView);
    }

    protected abstract @NonNull
    List<BaseSlice> createSlices();
    protected abstract void layoutSlice(BaseSlice slice, ConstraintSet set, int viewId, int index);
    protected abstract void onInit();
    protected abstract void onInited();
}
