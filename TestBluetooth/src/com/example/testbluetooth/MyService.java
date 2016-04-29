package com.example.testbluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {

	BluetoothAdapter bAdapter;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v("tt", "MyService onCreate");
		registScreenBroadcast();
		bAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	private void registScreenBroadcast() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		intentFilter.addAction(Intent.ACTION_USER_PRESENT);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mScreenBroadcast, intentFilter);
	}

	private BroadcastReceiver mScreenBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.v("tt", "action: " + action);
			if (action.equals(Intent.ACTION_SCREEN_ON)) {
				if (bAdapter.isEnabled()) {
					bAdapter.disable();
				} else {
					toast("bluetooth already closed");
					bAdapter.enable();
				}
			} else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
						BluetoothAdapter.ERROR);
				Log.v("tt", "state: " + state);
				if (state == BluetoothAdapter.STATE_ON) {
					toast("bluetooth is enable");
				} else if (state == BluetoothAdapter.STATE_OFF) {
					toast("bluetooth is closed, open bluetooth delay 5 second");
					mHandler.removeMessages(1);
					mHandler.sendEmptyMessageDelayed(1, 5000);
				}else if (state == BluetoothAdapter.STATE_TURNING_ON) {
					toast("open bluetooth...");
				}else if (state == BluetoothAdapter.STATE_TURNING_OFF) {
					toast("close bluetooth...");
				}
			}
		}
	};

	private void toast(String mess) {
		Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				toast("open bluetooth...");
				bAdapter.enable();
				break;
			case 2:
				break;
			case 3:
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mScreenBroadcast);
	}

}
