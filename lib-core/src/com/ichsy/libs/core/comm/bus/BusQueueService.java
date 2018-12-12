package com.ichsy.libs.core.comm.bus;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.utils.LogUtils;
import com.ichsy.libs.core.comm.utils.ObjectUtils;
import com.ichsy.libs.core.comm.utils.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 负责处理和分发总线事务的服务类
 * Created by liuyuhang on 16/5/10.
 */
public class BusQueueService {

    private HashMap<String, List<BusEventObject>> mMessageQueue;//消息队列

    private static final int MAX_HANDLER_SIZE = 30;

    public BusQueueService() {
        mMessageQueue = new HashMap<>();
    }

    public void addQueue(String key, BusEventObject eventObject) {
        if (null == eventObject) return;

        List<BusEventObject> observers = mMessageQueue.get(key);

        if (observers == null) {
            observers = new ArrayList<>();
            mMessageQueue.put(key, observers);
        }
        observers.add(eventObject);

        LogUtils.i("busevent", String.format("add event to bus,key is:%s; targetClass is :%s;", key, eventObject.observer));

        //为了防止内存溢出，handler再太多的情况下会进行自动清理
        if (observers.size() >= MAX_HANDLER_SIZE) {
            observers.remove(0);
        }
    }

    public void removeQueue(String key, BusEventObserver observer) {
        if (null == observer) return;
        if (null == mMessageQueue) return;

        if (mMessageQueue.containsKey(key)) {
            List<BusEventObject> eventObjects = mMessageQueue.get(key);

            for (BusEventObject eventObject : eventObjects) {
                if (eventObject.observer == observer) {
                    eventObjects.remove(eventObject);
                    break;
                }
            }

            //如果当前事件已经没有注册的地方，可以释放掉该key的所有内存
            if (eventObjects.size() == 0) {
                removeQueueForKey(key);
            }

            LogUtils.i("busevent", String.format("remove event to bus, key: %s; targetClass is:%s", key, observer));
        }
    }

    public void removeQueueForKey(String key) {
        if (null == mMessageQueue) return;
        mMessageQueue.remove(key);
    }

    /**
     * 根据类名，删除注册的总线
     *
     * @param clazz
     */
    public void removeQueueByClassName(Object clazz) {
        for (Map.Entry<String, List<BusEventObject>> next : mMessageQueue.entrySet()) {
            String busKey = next.getKey();
            List<BusEventObject> busEventObjectList = next.getValue();

            Iterator<BusEventObject> busEventObjectIterator = busEventObjectList.iterator();
            while (busEventObjectIterator.hasNext()) {
                BusEventObject busEventObject = busEventObjectIterator.next();
                if (!TextUtils.isEmpty(busEventObject.classNameTarget) && busEventObject.classNameTarget.equals(clazz.getClass().getName())) {
                    LogUtils.i("busevent", "removeQueueByClassName, key is: " + busKey + " activityTag is: " + busEventObject.classNameTarget);
                    busEventObjectIterator.remove();
                }
            }

        }
    }

    /**
     * 分发消息
     *
     * @param key
     * @param message
     */
    public void distributeEvent(Context receiveContext, final String key, final Object message) {
        if (mMessageQueue.size() == 0) return;

        List<BusEventObject> observers = mMessageQueue.get(key);
        if (observers == null || observers.isEmpty()) {
            return;
        }

        //给订阅者发送信息到倒着发送，保证最后注册的订阅者优先收到订阅消息
        for (int i = observers.size() - 1; i >= 0; i--) {
            BusEventObject eventObject = observers.get(i);

            if (ObjectUtils.isNotNull(receiveContext)) {
                if (!ObjectUtils.equals(eventObject.classNameTarget, receiveContext.getClass().getName())) {
                    continue;
                }
            }

//            eventObject.classNameTag

            final BusEventObserver observer = eventObject.observer;

//            Handler handler =new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    LogUtils.i("lyh", "runnable run");
//                    observer.onBusEvent(key, message);
//                }
//            };
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    observer.onBusEvent(key, message);
                }
            };

            if (eventObject.onMainThread) {
                ThreadPoolUtil.runOnMainThread(runnable);
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(runnable);
//                handler.sendEmptyMessage(0);
//                observer.onBusEvent(key, message);
            } else {
                ThreadPoolUtil.getInstance().fetchData(runnable);
            }
        }


    }
}
