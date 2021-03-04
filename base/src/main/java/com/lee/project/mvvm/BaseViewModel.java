package com.lee.project.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {

    public MutableLiveData<Integer> loadState = new MutableLiveData<>();
    public static final int START = 1;
    public static final int END = 2;
    public static final int ERROR = 3;


    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
