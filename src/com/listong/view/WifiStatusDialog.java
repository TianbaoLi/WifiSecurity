package com.listong.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.listong.airnet.R;
import com.listong.util.WifiAdmin;

/**
 * Class Name: WifiStatusDialog.java<br>
 * Function:网络状态<br>
 * 
 * Modifications:<br>
 * 
 * @author ZYT DateTime 2014-5-14 上午11:41:55<br>
 * @version 1.0<br>
 * <br>
 */
public class WifiStatusDialog extends Dialog {

	// Wifi管理类
	private WifiAdmin mWifiAdmin;

	private Context context;

	private ScanResult scanResult;
	private String wifiName;
	private int level;
	private String securigyLevel;
	private String Mac;

	private TextView txtWifiName;
	private TextView txtConnStatus;
	private TextView txtSinglStrength;
	private TextView txtSecurityLevel;
	private TextView txtIpAddress;
	
	private TextView status_white;
	private TextView status_black;

	private TextView txtBtnDisConn;
	private TextView txtBtnCancel;
	private String SDPATH=Environment.getExternalStorageDirectory().getPath();
	int num=0;

	public WifiStatusDialog(Context context, int theme) {
		super(context, theme);
		this.mWifiAdmin = new WifiAdmin(context);
	}

	private WifiStatusDialog(Context context, int theme, String wifiName,
			int singlStren, String securityLevl,String mac) {
		super(context, theme);
		this.context = context;
		this.wifiName = wifiName;
		this.level = singlStren;
		this.securigyLevel = securityLevl;
		this.mWifiAdmin = new WifiAdmin(context);
		this.Mac=mac;
	}

	public WifiStatusDialog(Context context, int theme, ScanResult scanResult,
			OnNetworkChangeListener onNetworkChangeListener) {
		this(context, theme, scanResult.SSID, scanResult.level,
				scanResult.capabilities,scanResult.BSSID);
		this.scanResult = scanResult;
		this.mWifiAdmin = new WifiAdmin(context);
		this.onNetworkChangeListener = onNetworkChangeListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_wifi_status);
		setCanceledOnTouchOutside(false);

		initView();
		setListener();
	}

	private void setListener() {

		txtBtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("txtBtnCancel");
				WifiStatusDialog.this.dismiss();
			}
		});

		txtBtnDisConn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 断开连接
				int netId = mWifiAdmin.getConnNetId();
				mWifiAdmin.disConnectionWifi(netId);
				WifiStatusDialog.this.dismiss();
				onNetworkChangeListener.onNetWorkDisConnect();
			}
		});
		
		status_white.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				num=0;
				File file = new File(SDPATH + "//" + "white_list1.txt");
				
				if (!file.exists()) { 
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					}
				try { 
					FileWriter fw = new FileWriter(SDPATH + "//" + "white_list1.txt",true); 
					File f = new File(SDPATH + "//" + "white_list1.txt"); 
					fw.write(Mac+"\t\n");  
					fw.flush();
					fw.close(); 
					} catch (Exception e){}
				
				
				int i,j;
				char[] s1=Mac.toCharArray();
				try{
				FileReader fr = new FileReader(SDPATH + "//" + "white_list1.txt");
				BufferedReader br = new BufferedReader(fr);
			    String strline =null;
			    while ((strline=br.readLine())!=null)
			    if (strline!=null)
			    {
			    	int check=0;
			    	char[] s2=strline.toCharArray();
			    	j=0;
			    	while (s1[0]!=s2[j] && j<=10)
			    		j++;
			    	for (i=0;i<17;i++,j++)
			    		if (s1[i]!=s2[j])
			    		{
			    			check=1;
			    			break;
			    		}
			    			
			    	if (check==0)
			    		num++;
			    }
			    
			    fr.close();
			    br.close();
				}

			      catch(FileNotFoundException e) {

			                  e.printStackTrace();

			            }

			            catch(IOException e) {

			                  e.printStackTrace();

			            }
				
				
				if (num%2==1)
					status_white.setText("从白名单中删除");
				else
					status_white.setText("加入白名单");
			}
		});
		
		status_black.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				num=0;
				File file = new File(SDPATH + "//" + "black_list1.txt");
				
				if (!file.exists()) { 
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					}
				try { 
					FileWriter fw = new FileWriter(SDPATH + "//" + "black_list1.txt",true); 
					File f = new File(SDPATH + "//" + "black_list1.txt"); 
					fw.write(Mac+"\t\n");  
					fw.flush();
					fw.close(); 
					} catch (Exception e){}
				
				
				int i,j;
				char[] s1=Mac.toCharArray();
				try{
				FileReader fr = new FileReader(SDPATH + "//" + "black_list1.txt");
				BufferedReader br = new BufferedReader(fr);
			    String strline =null;
			    while ((strline=br.readLine())!=null)
			    if (strline!=null)
			    {
			    	int check=0;
			    	char[] s2=strline.toCharArray();
			    	j=0;
			    	while (s1[0]!=s2[j] && j<=10)
			    		j++;
			    	for (i=0;i<17;i++,j++)
			    		if (s1[i]!=s2[j])
			    		{
			    			check=1;
			    			break;
			    		}
			    			
			    	if (check==0)
			    		num++;
			    }
			    
			    fr.close();
			    br.close();
				}

			      catch(FileNotFoundException e) {

			                  e.printStackTrace();

			            }

			            catch(IOException e) {

			                  e.printStackTrace();

			            }
				
				
				if (num%2==1)
					status_black.setText("从黑名单中删除");
				else
					status_black.setText("加入黑名单");
			}
		});
	}

	private void initView() {
		txtWifiName = (TextView) findViewById(R.id.txt_wifi_name);
		txtConnStatus = (TextView) findViewById(R.id.txt_conn_status);
		txtSinglStrength = (TextView) findViewById(R.id.txt_signal_strength);
		txtSecurityLevel = (TextView) findViewById(R.id.txt_security_level);
		txtIpAddress = (TextView) findViewById(R.id.txt_ip_address);

		txtBtnCancel = (TextView) findViewById(R.id.txt_btn_cancel);
		txtBtnDisConn = (TextView) findViewById(R.id.txt_btn_disconnect);
		status_white=(TextView) findViewById(R.id.status_white);
		status_black=(TextView) findViewById(R.id.status_black);
		
		int i,j;
		num=0;
		char[] s1=Mac.toCharArray();
		try{
		FileReader fr = new FileReader(SDPATH + "//" + "white_list1.txt");
		BufferedReader br = new BufferedReader(fr);
	    String strline =null;
	    while ((strline=br.readLine())!=null)
	    if (strline!=null)
	    {
	    	int check=0;
	    	char[] s2=strline.toCharArray();
	    	j=0;
	    	while (s1[0]!=s2[j] && j<=10)
	    		j++;
	    	for (i=0;i<17;i++,j++)
	    		if (s1[i]!=s2[j])
	    		{
	    			check=1;
	    			break;
	    		}
	    			
	    	if (check==0)
	    		num++;
	    }
	    
	    fr.close();
	    br.close();
		}

	      catch(FileNotFoundException e) {

	                  e.printStackTrace();

	            }

	            catch(IOException e) {

	                  e.printStackTrace();

	            }
		
		
		if (num%2==1)
			status_white.setText("从白名单中删除");
		else
			status_white.setText("加入白名单");
		
		num=0;
		try{
			FileReader fr = new FileReader(SDPATH + "//" + "black_list1.txt");
			BufferedReader br = new BufferedReader(fr);
		    String strline =null;
		    while ((strline=br.readLine())!=null)
		    if (strline!=null)
		    {
		    	int check=0;
		    	char[] s2=strline.toCharArray();
		    	j=0;
		    	while (s1[0]!=s2[j] && j<=10)
		    		j++;
		    	for (i=0;i<17;i++,j++)
		    		if (s1[i]!=s2[j])
		    		{
		    			check=1;
		    			break;
		    		}
		    			
		    	if (check==0)
		    		num++;
		    }
		    
		    fr.close();
		    br.close();
			}

		      catch(FileNotFoundException e) {

		                  e.printStackTrace();

		            }

		            catch(IOException e) {

		                  e.printStackTrace();

		            }
			
			
			if (num%2==1)
				status_black.setText("从黑名单中删除");
			else
				status_black.setText("加入黑名单");
			

		txtWifiName.setText(wifiName);
		txtConnStatus.setText("已连接");
		txtSinglStrength.setText(WifiAdmin.singlLevToStr(level));
		txtSecurityLevel.setText(securigyLevel);
		txtIpAddress
				.setText(mWifiAdmin.ipIntToString(mWifiAdmin.getIpAddress()));

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Point size = new Point();
		wm.getDefaultDisplay().getSize(size);

		super.show();
		getWindow().setLayout((int) (size.x * 9 / 10),
				LayoutParams.WRAP_CONTENT);
	}

	private void showShortToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	private OnNetworkChangeListener onNetworkChangeListener;

}
