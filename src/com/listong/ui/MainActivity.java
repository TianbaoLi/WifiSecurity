package com.listong.ui;

import java.net.Inet4Address;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.listong.airnet.R;
import com.listong.util.WifiAdmin;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	private TextView allNetWork;
	private Button scan;
	private Button start;
	private Button stop;
	private Button check;
	private WifiAdmin mWifiAdmin;
	// 扫描结果列表
	private List<ScanResult> list;
	private ScanResult mScanResult;
	private StringBuffer sb = new StringBuffer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWifiAdmin = new WifiAdmin(MainActivity.this);
		init();
	}

	public void init() {
		allNetWork = (TextView) findViewById(R.id.allNetWork);
		scan = (Button) findViewById(R.id.scan);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		check = (Button) findViewById(R.id.check);
		scan.setOnClickListener(new MyListener());
		start.setOnClickListener(new MyListener());
		stop.setOnClickListener(new MyListener());
		check.setOnClickListener(new MyListener());
	}

	private class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.scan:// 扫描网络
				getAllNetWorkList();
				break;
			case R.id.start:// 打开Wifi
				mWifiAdmin.openWifi();
				Toast.makeText(MainActivity.this,
						"当前wifi状态为：" + mWifiAdmin.checkState(), 1).show();
				break;
			case R.id.stop:// 关闭Wifi
				mWifiAdmin.closeWifi();
				Toast.makeText(MainActivity.this,
						"当前wifi状态为：" + mWifiAdmin.checkState(), 1).show();
				break;
			case R.id.check:// Wifi状态
				Toast.makeText(MainActivity.this,
						"当前wifi状态为：" + mWifiAdmin.checkState(), 1).show();
				break;
			default:
				break;
			}
		}

	}

	public void getAllNetWorkList() {
		// 每次点击扫描之前清空上一次的扫描结果
		if (sb != null) {
			sb = new StringBuffer();
		}
		// 开始扫描网络
		mWifiAdmin.startScan();
		list = mWifiAdmin.getWifiList();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				// 得到扫描结果
				mScanResult = list.get(i);
				sb = sb.append(mScanResult.BSSID + "  ")
						.append(mScanResult.SSID + "   ")
						.append(mScanResult.capabilities + "   ")
						.append(mScanResult.frequency + "   ")
						.append(mScanResult.level + "\n\n");
			}
			allNetWork.setText("扫描到的wifi网络：\n" + sb.toString());
		}
	}

	public void list(View view) {
		startActivity(new Intent(MainActivity.this, WifiListActivity.class));
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
	}

	public void getCurNet(View view) {
		// 取得WifiManager对象
		WifiManager mWifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo connInfo = mWifiManager.getConnectionInfo();
		connInfo.getSSID();

		int ip = mWifiAdmin.getIpAddress();
		String ipStr = Integer.toHexString(ip);

		ipStr = ipIntToString(ip);

	}

	/**
	 * Function: 将int类型的IP转换成字符串形式的IP<br>
	 * 
	 * @author ZYT DateTime 2014-5-14 下午12:28:16<br>
	 * @param ip
	 * @return<br>
	 */
	private String ipIntToString(int ip) {
		try {
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (0xff & ip);
			bytes[1] = (byte) ((0xff00 & ip) >> 8);
			bytes[2] = (byte) ((0xff0000 & ip) >> 16);
			bytes[3] = (byte) ((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		} catch (Exception e) {
			return "";
		}
	}
}