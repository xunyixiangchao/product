package com.leyou.library.le_library.comm.network.comm.user;

import android.content.Context;

import com.ichsy.libs.core.comm.bus.url.UrlParser;
import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.leyou.library.le_library.comm.helper.BabyHelper;
import com.leyou.library.le_library.model.UserVo;

/**
 * Created by liuyuhang on 2016/11/23.
 */

public class TokenOperation {
    public static final String IS_LOGIN = "isLogin";


    public static void saveUser(Context context, UserVo body) {
        BaseProvider provider = new SharedPreferencesProvider();
        provider.getProvider(context).putCache(IS_LOGIN, body);
    }

    public static UserVo getUser(Context context) {
        BaseProvider provider = new SharedPreferencesProvider();
        return provider.getProvider(context).getCache(IS_LOGIN, UserVo.class);
    }

    /**
     * 获取用户头像
     *
     * @param context
     * @return
     */
    public static String getUserIcon(Context context) {
        return BabyHelper.getBabyImg(context);
    }

    public static boolean isLogin(Context context) {
        UserVo user = getUser(context);
        if (user == null) {
            return false;
        }
        return user.isLogin == 1;
    }

    public static void jumpLogin(Context context) {
        UrlParser.getInstance().parser(context, "leyou://native?native=com.capelabs.leyou.ui.activity.user.LoginActivity");
    }

    public static String getUserId(Context context) {
        UserVo user = getUser(context);
        if (user == null) {
            return null;
        }
        return user.user_id;
    }

    /*获取token*/
    public static String getToken(Context context) {
        UserVo user = getUser(context);
        if (user == null) {
            return null;
        }
        return user.token;
    }

}
