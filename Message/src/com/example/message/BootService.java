package com.example.message;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BootService extends Service

{

	public static final String TAG = "BootService";

	private ContentObserver mObserver;

	@Override
	public void onCreate()

	{

		Log.i(TAG, "onCreate().");

		super.onCreate();

		addSMSObserver();

	}

	public void addSMSObserver()

	{
		Log.i(TAG, "add a SMS observer. ");
		ContentResolver resolver = getContentResolver();
		Handler handler = new SMSHandler(this);
		mObserver = new SMSObserver(resolver, handler);
		/**
		 * 观察系统的短信息数据发生了变化。当监听到短信数据发生变化时，查询所有已发送的短信并且显示出来。
		 */
		resolver.registerContentObserver(SMS.CONTENT_URI_inbox, true, mObserver);
	}

	@Override
	public IBinder onBind(Intent intent)

	{
		return null;
	}

	@Override
	public void onDestroy()

	{
		Log.i(TAG, "onDestroy().");
		this.getContentResolver().unregisterContentObserver(mObserver);
		super.onDestroy();

	}

}
