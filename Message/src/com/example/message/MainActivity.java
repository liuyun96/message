package com.example.message;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int keyBackClickCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent intent = getIntent();
		if (intent.getStringExtra("msg") != null
				&& intent.getStringExtra("sender") != null) {
			TextView msgView = (TextView) findViewById(R.id.msg);
			TextView senderView = (TextView) findViewById(R.id.sender);
			msgView.setText(intent.getStringExtra("msg"));
			senderView.setText(intent.getStringExtra("sender"));
		}
		Intent i = new Intent(this, BootService.class);
		startService(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * 退出应用
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				// showToast(this.getApplication().getApplicationContext(), "");
				Toast.makeText(this, "再点一下退出", Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 3000);
				break;
			case 1:
				finish();
			default:
				break;
			}
		}
		return true;
	}

}
