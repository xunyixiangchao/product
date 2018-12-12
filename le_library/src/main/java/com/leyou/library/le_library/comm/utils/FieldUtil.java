package com.leyou.library.le_library.comm.utils;

import android.text.TextUtils;

/**
 * Created by ss on 2018/5/7.
 */

public class FieldUtil {

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        if (s.equals("")) {
            return true;
        }
        if (s.equals("null")) {
            return true;
        }
        return false;
    }
    public static String hintPhone(String phone){
        if(!TextUtils.isEmpty(phone)){
            return  phone.substring(0,3)+"****"+phone.substring(phone.length()-4,phone.length());
        }else {
            return "";
        }
    }
}
