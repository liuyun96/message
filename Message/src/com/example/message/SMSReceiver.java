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
import android.telephony.SmsMessage;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SMSReceiver extends BroadcastReceiver {

	private int tag = 1;
	String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 处理短信通知
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
				String content = message.getMessageBody();// 得到短信内容
				String sender = message.getOriginatingAddress();// 得到发件人号码
				if (sender.equals("10086")) {
					this.abortBroadcast();
				} else {
					addNotification(context, content, sender);
					tag++;
				}
			}
		}
	}

	private void addNotification(Context context, String msg, String sender) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		// Notification.FLAG_INSISTENT; //让声音、振动无限循环，直到用户响应
		// Notification.FLAG_AUTO_CANCEL; //通知被点击后，自动消失
		// Notification.FLAG_NO_CLEAR; //点击'Clear'时，不清楚该通知(QQ的通知无法清除，就是用的这个)
		Notification noti = new Notification.Builder(context)
				.setContentTitle(sender).setContentText(msg).setTicker("MSG")
				.setContentIntent(pendingIntent).setSmallIcon(R.drawable.icon)
				.build();
		manager.notify(tag, noti);
	}
}
