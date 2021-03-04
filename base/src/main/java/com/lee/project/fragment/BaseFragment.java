package com.lee.project.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lee.project.utils.EventBusUtils;

/**
 * @author lixuce
 */
public abstract class BaseFragment extends Fragment {
    private boolean isFirstAdd = true;
    protected View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeAllViewsInLayout();
        }


        //在基类进行ButterKnife的控件绑定和解绑，子类无需再次书写
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isFirstAdd) {
            isFirstAdd = false;
            initFirst();
        }
        initData();
    }

    /**
     * 注册EventBus
     */
    protected void registerEvent() {
        EventBusUtils.register(this);
    }

    /**
     * fragment的布局文件
     * @return 布局文件
     */
    protected abstract int getLayoutId();

    /**
     * 封装的数据初始化，无需重写Fragment的{onViewCreated()}
     */
    protected abstract void initData();

    /**
     * 第一次初始化Fragment时才会调用
     */
    protected void initFirst() {
    }


    @Override
    public void onDestroy() {

        if (rootView != null) {
            rootView = null;
        }

        EventBusUtils.unregister(this);
        super.onDestroy();
    }

    /**
     * 判断当前fragment 以来的activity 是否已经销毁
     *
     * @return 是否销毁
     */
    public boolean isFinish() {
        if (getContext() == null) {
            return true;
        }
        if (getActivity() == null) {
            return true;
        } else {
            return getActivity().isDestroyed() || getActivity().isDestroyed();
        }
    }
}
