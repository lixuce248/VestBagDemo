package com.lee.project.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;


import com.lee.project.mvvm.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ViewModel fragment 基类
 *
 * @author lixuce
 */
public abstract class BaseVMFragment<VM extends BaseViewModel> extends BaseFragment {


    /**
     * 处理逻辑业务
     */

    protected abstract void processLogic();


    protected VM mViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createViewModel();
        processLogic();
    }


    private void createViewModel() {
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            if (getActivity() != null && isSameViewModel()) {
                mViewModel = (VM) ViewModelProviders.of(getActivity()).get(modelClass);
            } else {
                mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
            }

        }
    }

    /**
     * 同一个activity 下的fragment 是否共享同一个view model
     */

    protected boolean isSameViewModel() {
        return false;
    }

}
