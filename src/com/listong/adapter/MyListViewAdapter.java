package com.listong.adapter;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.listong.airnet.R;

public class MyListViewAdapter extends BaseAdapter {

	private List<ScanResult> datas;
	private Context context;
	// ȡ��WifiManager����
	private WifiManager mWifiManager;
	private WifiInfo connInfo;
	String[] name_list=new String [100];
	int count=0;
	int zon=0;
	int color_count=0;
	int []color={0xffff00ff,0xfff000ff,0xffff000f};
	ConnectivityManager cm;

	public void setDatas(List<ScanResult> datas) {
		this.datas = datas;
		connInfo = mWifiManager.getConnectionInfo();
		
		int i;
		for (i=0;i<100;i++)
			name_list[i]="";
	}

	public MyListViewAdapter(Context context, List<ScanResult> datas) {
		super();
		this.datas = datas;
		this.context = context;
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		connInfo = mWifiManager.getConnectionInfo();
		cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		List<ScanResult> scanResults = mWifiManager.getScanResults();
		count=0;
		for (ScanResult scanResult : scanResults) {
			name_list[count]=scanResult.SSID;
			count=count+1;
		}
		
	}

	@Override
	public int getCount() {
		if (datas == null) {
			return 0;
		}
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder tag = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.wifi_info_item, null);
			tag = new Holder();
			tag.txtWifiName = (TextView) convertView
					.findViewById(R.id.txt_wifi_name);
			tag.txtWifiDesc = (TextView) convertView
					.findViewById(R.id.txt_wifi_desc);
			tag.imgWifiLevelIco = (ImageView) convertView
					.findViewById(R.id.img_wifi_level_ico);
			convertView.setTag(tag);
		}

		// ��������
		Holder holder = (Holder) convertView.getTag();
		// Wifi ����
		
		
		
		int i;
		zon=0;
		for (i=0;i<count;i++)
			if (name_list[i].equals(datas.get(position).SSID))
				zon++;
		if (zon>=2)
		{
			holder.txtWifiName.setText(datas.get(position).SSID);
			holder.txtWifiName.setTextColor(color[color_count]);
			color_count=(color_count+1)%3;
		}
		else
			holder.txtWifiName.setText(datas.get(position).SSID);
		
			
		// Wifi ����
		String desc = "";
		String descOri = datas.get(position).capabilities;
		if (descOri.toUpperCase().contains("WPA-PSK")) {
			desc = "WPA";
		}
		if (descOri.toUpperCase().contains("WPA2-PSK")) {
			desc = "WPA2";
		}
		if (descOri.toUpperCase().contains("WPA-PSK")
				&& descOri.toUpperCase().contains("WPA2-PSK")) {
			desc = "WPA/WPA2";
		}

		if (TextUtils.isEmpty(desc)) {
			desc = "δ�ܱ���������";
		} else {
			desc = "ͨ�� " + desc + " ���б���";
		}

		// �Ƿ����ӣ�����ոնϿ����ӣ�connInfo.SSID==null
		connInfo = mWifiManager.getConnectionInfo();

		State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (wifi == State.CONNECTED) {
			WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
			String g1 = wifiInfo.getSSID();
			Log.e("g1============>", g1);
			Log.e("g2============>", datas.get(position).SSID);
			
			String g2 = "\""+datas.get(position).SSID+"\"";
			
			if (g2.endsWith(g1)) {
				desc = "������";
//				tag.txtWifiDesc.setTextColor(context.getResources().getColor(R.color.textdec));
			}
		}


		holder.txtWifiDesc.setText(desc);

		// �����ź�ǿ��
		int level = datas.get(position).level;
		int imgId = R.drawable.wifi05;
		if (Math.abs(level) > 100) {
			imgId = R.drawable.wifi05;
		} else if (Math.abs(level) > 80) {
			imgId = R.drawable.wifi04;
		} else if (Math.abs(level) > 70) {
			imgId = R.drawable.wifi04;
		} else if (Math.abs(level) > 60) {
			imgId = R.drawable.wifi03;
		} else if (Math.abs(level) > 50) {
			imgId = R.drawable.wifi02;
		} else {
			imgId = R.drawable.wifi01;
		}
		holder.imgWifiLevelIco.setImageResource(imgId);
		return convertView;
	}

	public static class Holder {
		public TextView txtWifiName;
		public TextView txtWifiDesc;
		public ImageView imgWifiLevelIco;
		public TextView txtWifiKey;
	}
}