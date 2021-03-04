package com.lee.project.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;


import com.lee.project.R;
import com.lee.project.application.App;
import com.lee.project.config.Constant;
import com.lee.project.event.SoftKeyboardDownEvent;
import com.lee.project.event.SoftKeyboardUpEvent;
import com.lee.project.utils.AppUtils;
import com.lee.project.utils.EventBusUtils;
import com.lee.project.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * author:lixuce
 * createTime:18/7/19
 * desc:Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static String TAG = "";
    private ViewGroup rootView;

    //Activity结束时，上个页面的出现动画
    protected int activityCloseEnterAnimation;
    //Activity结束时，本页面的退出动画
    protected int activityCloseExitAnimation;
    //是否允许点击空白区域自动收起键盘
    public boolean isAllowAutoHideKeyboard = true;
    boolean isSoftKeyboardOpened = false;
    int navHeight;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } catch (Exception e) {

        }
        super.onCreate(savedInstanceState);

        rootView = (ViewGroup) findViewById(android.R.id.content);

        TAG = getLocalClassName();
        //有些机型，会存在先走Splash，后走App的情况，这样会导致App.context为空，此处进行判断
        if (App.context == null) {
            App.context = getApplicationContext();
        }


        setContentView(getLayoutId());
        //ButterKnife的控件绑定注册，不需要在子Activity再进行注册
        //默认状态栏色值
        setStatusBarStyle(Constant.BarStyle.TRAN_WHITE);

        initFinishAnimation();

        initData(savedInstanceState);


        if (registerEvent()) {
            EventBusUtils.register(this);
        }
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                final int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

                if (!isSoftKeyboardOpened && heightDiff > rootView.getRootView().getHeight() * 0.3) {
                    isSoftKeyboardOpened = true;
                    EventBus.getDefault().post(new SoftKeyboardUpEvent(BaseActivity.this, heightDiff - navHeight));
                } else if (isSoftKeyboardOpened && heightDiff < rootView.getRootView().getHeight() * 0.3) {
                    isSoftKeyboardOpened = false;
                    navHeight = heightDiff;
                    EventBus.getDefault().post(new SoftKeyboardDownEvent(BaseActivity.this));
                } else {
                    navHeight = heightDiff;
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //页面异常销毁时，不存储Fragment，等下次切回App时，会自动走onCreate()创建
        outState.putParcelable("android:support:fragments", null);
    }


    /**
     * 获取style中的退出动画
     * 注意！！！！！使用style配置Activity进出动画时，Activity B退出时，会使用上个页面的退出动画，导致动画不匹配。
     * 所以此处将Activity B的退出动画获取出来，强行使用Activity B的退出动画！！
     */
    protected void initFinishAnimation() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});

        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);

        activityStyle.recycle();

        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});

        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);

        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);

        activityStyle.recycle();
    }

    /**
     * 指定Layout布局
     *
     * @return layout的R文件地址
     */
    public abstract int getLayoutId();

    /**
     * 无需重写Activity的onCreate，数据初始化在此方法执行
     */
    protected abstract void initData(@Nullable Bundle savedInstanceState);


    public boolean registerEvent() {
        return false;
    }

    /**
     * 设置状态栏的颜色
     */
    public void setStatusBarStyle(int barStyle) {
        switch (barStyle) {
            case Constant.BarStyle.HIDE://直接全部隐藏
                StatusBarUtils.setStatusBarVisibility(this, false);
                break;
            case Constant.BarStyle.TRAN_BLACK://深色字体的透明状态栏
                StatusBarUtils.setStatusBarVisibility(this, true);
                StatusBarUtils.setStatusBarAlpha(this, 0);
                StatusBarUtils.setStatusBarLightMode(this, true);
                break;
            case Constant.BarStyle.TRAN_WHITE://浅色字体的透明状态栏
                StatusBarUtils.setStatusBarVisibility(this, true);
                StatusBarUtils.setStatusBarAlpha(this, 0);
                StatusBarUtils.setStatusBarLightMode(this, false);
                break;
            case Constant.BarStyle.WHITE:
                StatusBarUtils.setStatusBarVisibility(this, true);
                StatusBarUtils.setStatusBarColor(this, AppUtils.getColor(R.color.c_ffffff));
                StatusBarUtils.setStatusBarLightMode(this, true);
                break;
            case Constant.BarStyle.C_F8F8F8:
                StatusBarUtils.setStatusBarVisibility(this, true);
                StatusBarUtils.setStatusBarColor(this, AppUtils.getColor(R.color.c_f8f8f8));
                StatusBarUtils.setStatusBarLightMode(this, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        //注释掉activity本身的过渡动画
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    protected void onDestroy() {
        if (registerEvent()) {
            EventBusUtils.unregister(this);
        }

        super.onDestroy();
    }


    /**
     * 是否允许自动收起键盘
     *
     * @param isAllow true允许，默认为true
     */
    public void setAllowAutoHideKeyboard(boolean isAllow) {

        isAllowAutoHideKeyboard = isAllow;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN && isAllowAutoHideKeyboard) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return super.dispatchTouchEvent(ev);

            }
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        } catch (Exception e) {
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
