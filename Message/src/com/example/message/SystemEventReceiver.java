package com.example.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemEventReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))

		{

			// context.startService(new Intent(Globals.IMICHAT_SERVICE));

		}

		else if (intent.getAction().equals(Globals.ACTION_SEND_SMS))

		{

			MessageItem mItem = (MessageItem) intent
					.getSerializableExtra(Globals.EXTRA_SMS_DATA);

			if (mItem != null && mItem.getPhone() != null
					&& mItem.getBody() != null) {

			}

		}
	}
}
