package com.lee.project.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: lixuce
 * Date  : 2019-09-29/17:33
 * E-Mail: 15011221378@163.com
 * -----------
 */
public class EventBusUtils {

    public static void register(Object object){
        if(!EventBus.getDefault().isRegistered(object)){
            EventBus.getDefault().register(object);
        }
    }

    public static void unregister(Object object){
        if(EventBus.getDefault().isRegistered(object)){
            EventBus.getDefault().unregister(object);
        }
    }
}
