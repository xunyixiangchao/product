package com.leyou.library.le_library.comm.operation;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.ichsy.libs.core.dao.BaseProvider;
import com.ichsy.libs.core.dao.SharedPreferencesProvider;
import com.ichsy.libs.core.net.http.HttpContext;
import com.ichsy.libs.core.net.http.RequestListener;
import com.ichsy.libs.core.net.http.RequestOptions;
import com.leyou.library.le_library.comm.network.LeHttpHelper;
import com.leyou.library.le_library.comm.network.comm.user.TokenOperation;
import com.leyou.library.le_library.config.LeConstant;
import com.leyou.library.le_library.model.response.OssConfigResponse;


/**
 * Created by lis on 2017/3/6.
 */

public class O2oUploadOperation {
	public interface OnUploadListener {
		void uploadSuccess(String fileName);

		void uploadFailure(String filePath);
	}

	public static void getOssConfig(final Context context) {
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.setRequestType("post");
		LeHttpHelper httpHelper = new LeHttpHelper(context, requestOptions);
		httpHelper.post(LeConstant.API.URL_BASE_O2O+LeConstant.API.URL_O2O_GET_OOS_CONFIG, null, OssConfigResponse.class, new RequestListener() {
			@Override
			public void onHttpRequestComplete(String url, HttpContext httpContext) {
				super.onHttpRequestComplete(url, httpContext);
				OssConfigResponse response = httpContext.getResponseObject();
				if (response != null) {
					BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
					provider.putCache("OosConfig", response);
				}
			}
		});
	}

	public static OssConfigResponse getOssConfigResponse(Context context) {
		BaseProvider.Provider provider = new SharedPreferencesProvider().getProvider(context);
		return provider.getCache("OosConfig", OssConfigResponse.class);
	}

	public static String getFileName(String filePath) {
		return filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
	}

	public static String getExtension(String filePath) {
		return filePath.substring(filePath.lastIndexOf("."), filePath.length());
	}

	public static void upload(Context context, final String filePath, final OnUploadListener uploadListener) {
		OssConfigResponse response = getOssConfigResponse(context);
		final String fileName = TokenOperation.getUserId(context) +"_"+
		                        String.valueOf(System.currentTimeMillis()) + getExtension(filePath);
		if (response != null) {
			// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
			OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(response.access_key_id, response.access_key_secret);
			ClientConfiguration conf = new ClientConfiguration();
			conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
			conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
			conf.setMaxConcurrentRequest(10); // 最大并发请求书，默认5个
			conf.setMaxErrorRetry(2); // 失败后最大重试次数，默 认2次
			OSS oss = new OSSClient(context.getApplicationContext(), response.endpoint, credentialProvider, conf);
			// 异步断点上传，不设置记录保存路径，只在本次上传内做断点续传
			PutObjectRequest put = new PutObjectRequest(response.bucket, fileName, filePath);
			// 异步上传时可以设置进度回调
//			put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//				@Override
//				public void onProgress(PutObjectRequest putObjectRequest, long l, long l1) {
//
//				}
//			});

			OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
				@Override
				public void onSuccess(PutObjectRequest request, PutObjectResult result) {
					Log.d("PutObject", "onSuccess");
					uploadListener.uploadSuccess(fileName);
				}

				@Override
				public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
					Log.d("PutObject", "onFailure");
					uploadListener.uploadFailure(filePath);
					// 请求异常
					if (clientException != null) {
						// 本地异常如网络异常等
						clientException.printStackTrace();
					}
					if (serviceException != null) {
						// 服务异常
						Log.e("ErrorCode", serviceException.getErrorCode());
						Log.e("RequestId", serviceException.getRequestId());
						Log.e("HostId", serviceException.getHostId());
						Log.e("RawMessage", serviceException.getRawMessage());
					}
				}
			});
			//task.waitUntilFinished();
		} else {
			getOssConfig(context);
			uploadListener.uploadFailure(filePath);
		}
	}

}
