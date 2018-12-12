package com.ichsy.libs.core.net.http.cache;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.net.http.HttpRequestInterface;

/**
 * 如果有缓存不会进行新的请求，默认缓存时间是5秒
 * 
 * @author liuyuhang
 *
 */
public class NoRequestCacheAdapter extends RequestCacheAdapter {

	public NoRequestCacheAdapter(Context context, HttpRequestInterface httpHelper) {
		super(context, httpHelper);
	}

	@Override
	public String getCacheKey(String url, Object params) {
		return url;
	}

	/**
	 * 如果没有超时，会只展示缓存数据，重写{@link #getCacheTime()}来修改缓存时间，单位毫秒
	 */
	@Override
	protected RequestCacheObject onCacheRead(Context context, RequestCacheObject cacheObject) {
		if (System.currentTimeMillis() - cacheObject.timeStamp >= getCacheTime())
			return deleteCache(context, cacheObject);
		return super.onCacheRead(context, cacheObject);
	}

	@Override
	public boolean verfiyHttpRequest(String cache) {
		return TextUtils.isEmpty(cache);
	}

	/**
	 * 复写可以更改缓存的保存时间
	 * 
	 * @return 缓存的有效时间，单位ms
	 */
	public int getCacheTime() {
		return 5000;
	}

}
