package com.ichsy.libs.core.comm.hotfix;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ichsy.libs.core.comm.exceptions.ExceptionUtil;
import com.ichsy.libs.core.comm.logwatch.LogWatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 解析服务端返回的hotfix
 * 
 * @author liuyuhang
 *
 */
public class HotFixParser {
	private String version;
	private String channel;
	private String model;
	private String netType;
	private String uid;// 用户的id
	private int dVersionCode;// 设备的系统版本

	public HotFixParser(String version, String channel, String model, String netType, String uid, int dVersionCode) {
		this.version = version;
		this.channel = channel;
		this.model = model;
		this.netType = netType;
		this.uid = uid;
		this.dVersionCode = dVersionCode;
	}

//	public HashMap<String, String> parserFile(String path) {
//		return parser(FileUtil.getString(path));
//	}

	public HashMap<String, String> parser(String json) {
		if (TextUtils.isEmpty(json)) {
			LogWatcher.getInstance().putMessage("热修复文件读取失败，原因：'json' is null");
			return null;
		}
		LogWatcher.getInstance().putMessage("热修复的json\n" + json);

		HotFixResponse hotFixResponse = new Gson().fromJson(json, HotFixResponse.class);
		return parser(hotFixResponse);
	}

	public HashMap<String, String> parser(HotFixResponse hotFixResponse) {
		return parser(hotFixResponse.hotFixList);
	}

	public HashMap<String, String> parser(List<HotFixVo> hotFixList) {
		LogWatcher.getInstance().putMessage("验证hotFixList size: " + hotFixList.size());
		HashMap<String, String> hotfixMap = new HashMap<String, String>();// 最终针对机器返回的hotfixMap
		try {
			// Iterator<HotFixVo> iterator =
			// hotFixResponse.hotFixList.iterator();
			
			LogWatcher.getInstance().putMessage("device.version: " + version,
					"device.channel: " + channel,
					"device.model: " + model,
					"device.netType: " + netType,
					"device.uid: " + uid,
					"device.dVersionCode: " + dVersionCode
					);
			for (HotFixVo hotFixVo : hotFixList) {
				BugFixVerify fixVerify = new BugFixVerify();
				LogWatcher.getInstance().putMessage("验证.version: " + above(hotFixVo.version, version),
						"验证.channel: " + above(hotFixVo.channel, channel),
						"验证.model: " + above(hotFixVo.model, model),
						"验证.netType: " + above(hotFixVo.network, netType),
						"验证.uid: " + above(hotFixVo.uid, uid),
						"验证.dVersionCode: " + verfyVersion(hotFixVo.dversioncode, dVersionCode));
				fixVerify.put(above(hotFixVo.version, version));
				fixVerify.put(above(hotFixVo.channel, channel));
				fixVerify.put(above(hotFixVo.model, model));
				fixVerify.put(above(hotFixVo.network, netType));
				fixVerify.put(above(hotFixVo.uid, uid));
				fixVerify.put(verfyVersion(hotFixVo.dversioncode, dVersionCode));

				if (fixVerify.isVerify()) {
					hotfixMap.putAll(hotFixVo.hotfixMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogWatcher.getInstance().putMessage("热修复文件读取失败，原因：" + ExceptionUtil.getException(e));
		}
		return hotfixMap;
	}

	/**
	 * 判断是否包含此手机
	 * 
	 * @param hotfix
	 * @param current
	 * @return
	 */
	private boolean above(String[] hotfix, String current) {
		if (hotfix == null || hotfix.length == 0)
			return true;// 什么都不返回表示包含这个机型
		if (TextUtils.isEmpty(current))
			return false;// 如果当前机型数据没有获取到，表示验证失败，本次热修复不包含本机型
		for (String aHotfix : hotfix) {
			int have = current.trim().toLowerCase(Locale.CHINA).indexOf(aHotfix.trim().toLowerCase(Locale.CHINA));
			if (have >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证设备的版本是否通过
	 * 
	 * @return
	 */
	private boolean verfyVersion(String hotfix, int current) {
		if (TextUtils.isEmpty(hotfix)) {
//			LogWatcher.getInstance().putMessage("return true hotfix is empty: " + hotfix);
			return true;
		}
		// hotfix为0-X、X-X、X-99;
		String ragion[] = hotfix.split("-");
		int startVersion = Integer.parseInt(ragion[0]);
		int endVersion = Integer.parseInt(ragion[1]);
		//			LogWatcher.getInstance().putMessage("current: " + current);
//			LogWatcher.getInstance().putMessage("startVersion: " + startVersion);
//			LogWatcher.getInstance().putMessage("endVersion: " + endVersion);
//			LogWatcher.getInstance().putMessage("return true current >= startVersion: ");
//			LogWatcher.getInstance().putMessage("return true current <= endVersion: ");
		return current >= startVersion && current <= endVersion;
		
//		// 分三种情况判断
//		if (startVersion == 0) {// 0-X
//			if (current > startVersion) {
//				return false;
//			} else {
//				return true;
//			}
//		} else if (endVersion == 0) {// X-0
//			if (current >= endVersion) {
//				return true;
//			} else {
//				return false;
//			}
//		} else {
//
//		}
	}

	private class BugFixVerify {
		private boolean verfiy = true;

		void put(boolean above) {
			if (!above) {
				verfiy = above;
			}
		}

		boolean isVerify() {
			return verfiy;
		}
	}
}
