package com.lee.project.event;

import android.content.Context;

public class SoftKeyboardUpEvent {
    public Context context;
    public int keyboardHeight;

    public SoftKeyboardUpEvent(Context context, int keyboardHeight) {
        this.context = context;
        this.keyboardHeight = keyboardHeight;
    }
}
