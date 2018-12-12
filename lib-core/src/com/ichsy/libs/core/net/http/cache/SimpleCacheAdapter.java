package com.ichsy.libs.core.net.http.cache;

import android.content.Context;

import com.ichsy.libs.core.net.http.HttpRequestInterface;

public class SimpleCacheAdapter extends RequestCacheAdapter {

	public SimpleCacheAdapter(Context context, HttpRequestInterface httpHelper) {
		super(context, httpHelper);
	}

	@Override
	public String getCacheKey(String url, Object params) {
		return url;
	}

}
