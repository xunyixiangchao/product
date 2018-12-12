package com.leyou.library.le_library.comm.bus;

import com.ichsy.libs.core.comm.bus.BusManager;

/**
 * Created by liuyuhang on 2016/12/20.
 */

public class ShoppingBus {

    public void openShoppingCart() {
        BusManager.getInstance().postEvent("", null);
    }

    public void addShoppingCart() {
        BusManager.getInstance().postEvent("", null);
    }
}
