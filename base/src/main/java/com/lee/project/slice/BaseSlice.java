package com.lee.project.slice;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lee.project.activity.SliceActivity;
import com.lee.project.utils.EventBusUtils;


/**
 * view切片，降低Activity代码复杂度
 * Created by Shawn
 */
public abstract class BaseSlice<T extends SliceActivity> {

    private T mActivity;
    protected View mRootView;

    //设置碎片依赖的额ctivity
    public void setActivity(T activity) {
        mActivity = activity;
    }
    //获取碎片依赖的Activity
    protected T getActivity() {
        return mActivity;
    }

    //隐藏碎片 需要显示动画
    public void hide() {

        hide(true);
    }
    //隐藏碎片 是否需要显示动画
    public void hide(boolean showAnim) {
        if (getActivity().isFinishing() || getActivity().isDestroyed()) {
            return;
        }
        if (mRootView == null || mRootView.getVisibility() == View.GONE) {
            return;
        }

        if (getCloseAnim() != null && showAnim) {
            Animation animation = getCloseAnim();
            mRootView.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRootView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            mRootView.setVisibility(View.GONE);
        }
    }
    //显示碎片 需要显示动画
    public void show() {
        show(true);
    }

    //显示碎片 是否需要显示动画
    public void show(boolean showAnim) {
        if (mRootView == null || mRootView.getVisibility() == View.VISIBLE) {
            return;
        }

        mRootView.setVisibility(View.VISIBLE);
        if (getOpenAnim() != null && showAnim) {
            mRootView.startAnimation(getOpenAnim());
        }
    }
    //判断碎片是否显示
    public boolean isShow() {
        return mRootView != null && mRootView.getVisibility() == View.VISIBLE;
    }
    //碎片 的view
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        mRootView = inflater.inflate(getLayoutId(), container, false);


        return mRootView;
    }

    public void onViewCreated(View view) {
        initData();
    }

    public void registerEvent() {
        EventBusUtils.register(this);
    }

    public int getId() {
        return mRootView.getId();
    }
    //碎片的生命周期
    public void onResume() {
    }

    public void onPause() {

    }

    public void onNewIntent() {

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    public void onDestroy() {

        EventBusUtils.unregister(this);
    }

    public boolean isAddSlice() {
        return true;
    }
    //碎片的初始化工作可以放在这里
    protected abstract void initData();
    //设置碎片的布局文件
    protected abstract int getLayoutId();
    //设置碎片打开的动画  需要重写
    protected Animation getOpenAnim() {
        return null;
    }
    //设置碎片关闭的动画  需要重写
    protected Animation getCloseAnim() {
        return null;
    }
}
