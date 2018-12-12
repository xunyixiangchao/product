package com.ichsy.libs.core.comm.bus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 根据class业务开发的总线
 * Created by liuyuhang on 2017/7/26.
 */
@Deprecated
public class BusClassQueueService {
    private HashMap<String, List<? extends BusEventObserver>> messageQueue;//根据key去保存列表

    private Class<? extends BusEventObserver> testClass;

    public BusClassQueueService() {
        messageQueue = new HashMap<>();
    }

    public void addClass(String key, Class<? extends BusEventObserver> clazz) {
        testClass = clazz;
//
//        List<? extends BusEventObserver> eventList = messageQueue.get(key);
//        if (null == eventList) {
//            eventList = new ArrayList<>();
//            messageQueue.put(key, eventList);
//        }
//        eventList.add(clazz);

    }

    public void remove(String key, Class<? extends BusEventObserver> clazz) {
    }

    public void distributeEvent(String key, Serializable object) {
        try {
            BusEventObserver busEventObserver = testClass.newInstance();
            busEventObserver.onBusEvent(key, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
