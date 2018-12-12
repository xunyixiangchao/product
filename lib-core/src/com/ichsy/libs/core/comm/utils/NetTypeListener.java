package com.ichsy.libs.core.comm.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ichsy.libs.core.comm.bus.BusManager;

/**
 * 监听网络改变的工具类
 * Created by liuyuhang on 16/5/18.
 */
public class NetTypeListener {
    public static final String EVENT_NET_CHANGED = "bus_net_type_changed";

    private static NetTypeListener instance;

    private boolean isRegister = false;

    public static NetTypeListener get() {
        if (null == instance) {
            instance = new NetTypeListener();
        }
        return instance;
    }

    public void start(Context context) {
        if (!isRegister) {
            IntentFilter mFilter = new IntentFilter();
            mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(myNetReceiver, mFilter);
            isRegister = true;
        }

    }

    public void stop(Context context) {
        if (isRegister && null != myNetReceiver) {
            context.unregisterReceiver(myNetReceiver);
        }
    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    BusManager.getInstance().postEvent(EVENT_NET_CHANGED, name);


                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络
                    }
                } else {
                    ////////网络断开

                }
            }

        }
    };
}
