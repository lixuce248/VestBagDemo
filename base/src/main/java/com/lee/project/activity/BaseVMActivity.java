package com.lee.project.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.lee.project.mvvm.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @author lixuce
 */

public abstract class BaseVMActivity<VM extends BaseViewModel> extends BaseActivity {


    //处理逻辑业务
    protected abstract void processLogic();


    protected VM mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        //创建我们的ViewModel。
        createViewModel();
        processLogic();

    }

    public void createViewModel() {
        if (mViewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }

            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
        }
    }
}
