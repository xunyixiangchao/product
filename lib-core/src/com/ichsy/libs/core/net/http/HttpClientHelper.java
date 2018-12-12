package com.ichsy.libs.core.net.http;

import android.content.Context;
import android.text.TextUtils;

import com.ichsy.libs.core.comm.exceptions.ExceptionUtil;
import com.ichsy.libs.core.comm.utils.LogUtils;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * httpClient实例
 * @author liuyuhang
 *
 */
public class HttpClientHelper {
	private static final String HTTPS_DEFAULT_PASSWORD = "pw123456";// https的默认密码
	
	private static SyncHttpClient mHttpClient;

	/**
	 * 获取一个单例的HttpClient
	 * 
	 * @return
	 */
	public static synchronized SyncHttpClient getSyncHttpClient() {
		if (null == mHttpClient) {
			mHttpClient = new SyncHttpClient() {

				@Override
				public String onRequestFailed(Throwable arg0, String arg1) {
					return null;
				}
			};

		}
		return mHttpClient;
	}


	/**
	 * 获取https ssl证书
	 * 
	 * @param context
	 * @param httpsCer
	 * @param httpsCerPassWord
	 * @return
	 */
	public static SSLSocketFactory getSocketFactory(Context context, String httpsCer, String httpsCerPassWord) {
		SSLSocketFactory sslFactory = null;
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream instream = context.getResources().getAssets().open(TextUtils.isEmpty(httpsCer) ? "https.bks" : httpsCer);
			keyStore.load(instream, (TextUtils.isEmpty(httpsCerPassWord)) ? HTTPS_DEFAULT_PASSWORD.toCharArray() : httpsCerPassWord.toCharArray());
			sslFactory = new SSLSocketFactory(keyStore);
			sslFactory.setHostnameVerifier(new X509HostnameVerifier() {

				@Override
				public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {

				}

				@Override
				public void verify(String arg0, X509Certificate arg1) throws SSLException {

				}

				@Override
				public void verify(String arg0, SSLSocket arg1) throws IOException {

				}

				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.i(HttpHelper.TAG, "response error ------------------\n" + ExceptionUtil.getException(e));

		}
		return sslFactory;
	}
}
