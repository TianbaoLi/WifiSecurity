package com.listong.view;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.IOException; 

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Sampler.Value;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.listong.airnet.R;
import com.listong.util.WifiAdmin;
import com.listong.util.WifiConnect.WifiCipherType;

/**
 * Class Name: WifiConnDialog.java<br>
 * Function:Wifi连接对话框<br>
 * 
 * Modifications:<br>
 * 
 * @author ZYT DateTime 2014-5-14 下午2:23:37<br>
 * @version 1.0<br>
 * <br>
 */
public class WifiConnDialog extends Dialog {

	private Context context;

	private ScanResult scanResult;
	private String wifiName;
	private int level;
	private String Mac;
	private String securigyLevel;

	private TextView txtWifiName;
	private TextView txtSinglStrength;
	private TextView txtSecurityLevel;
	private EditText edtPassword;
	private CheckBox cbxShowPass;

	private TextView Btnwhite;
	private TextView Btnblack;
	
	private TextView txtBtnConn;
	private TextView txtBtnCancel;
	
	private String SDPATH=Environment.getExternalStorageDirectory().getPath();
	int num=0;


	public WifiConnDialog(Context context, int theme) {
		super(context, theme);
	}

	private WifiConnDialog(Context context, int theme, String wifiName,
			int singlStren, String securityLevl,String mac) {
		super(context, theme);
		this.context = context;
		this.wifiName = wifiName;
		this.level = singlStren;
		this.securigyLevel = securityLevl;
		this.Mac=mac;
	}

	public WifiConnDialog(Context context, int theme, ScanResult scanResult,
			OnNetworkChangeListener onNetworkChangeListener) {
		this(context, theme, scanResult.SSID, scanResult.level,
				scanResult.capabilities,scanResult.BSSID);
		this.scanResult = scanResult;
		this.onNetworkChangeListener = onNetworkChangeListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_wifi_conn);
		setCanceledOnTouchOutside(false);
		
		initView();
		setListener();
	}

	private void setListener() {

		edtPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isEmpty(s)) {
					txtBtnConn.setEnabled(false);
					cbxShowPass.setEnabled(false);

				} else {
					
					int i,j;
					num=0;
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
					
					if (num%2==0)
					   txtBtnConn.setEnabled(true);
					else
					{
						txtBtnConn.setEnabled(false);
						txtBtnConn.setText("无法连接");
					}
					cbxShowPass.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		cbxShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// 文本正常显示
					edtPassword
							.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					Editable etable = edtPassword.getText();
					Selection.setSelection(etable, etable.length());

				} else {
					// 文本以密码形式显示
					edtPassword.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					// 下面两行代码实现: 输入框光标一直在输入文本后面
					Editable etable = edtPassword.getText();
					Selection.setSelection(etable, etable.length());

				}
			}
		});

		txtBtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("txtBtnCancel");
				WifiConnDialog.this.dismiss();
			}
		});

		txtBtnConn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				WifiCipherType type = null;
				if (scanResult.capabilities.toUpperCase().contains("WPA")) {
					type = WifiCipherType.WIFICIPHER_WPA;
				} else if (scanResult.capabilities.toUpperCase()
						.contains("WEP")) {
					type = WifiCipherType.WIFICIPHER_WEP;
				} else {
					type = WifiCipherType.WIFICIPHER_NOPASS;
				}

				// 连接网络
				WifiAdmin mWifiAdmin = new WifiAdmin(context);
				boolean bRet = mWifiAdmin.connect(scanResult.SSID, edtPassword
						.getText().toString().trim(), type);
				if (bRet) {
					showShortToast("连接成功");
					onNetworkChangeListener.onNetWorkConnect();
				} else {
					showShortToast("连接失败");
					onNetworkChangeListener.onNetWorkConnect();
				}
				WifiConnDialog.this.dismiss();
			}
		});
		
		Btnwhite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				
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
				    Btnwhite.setText("从白名单中删除");
				else
					Btnwhite.setText("加入白名单");
				
				} 


		});
		
		Btnblack.setOnClickListener(new View.OnClickListener() {
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
				    Btnblack.setText("从黑名单中删除");
				else
					Btnblack.setText("加入黑名单");
				
				}
		});

	}

	private void initView() {
		txtWifiName = (TextView) findViewById(R.id.txt_wifi_name);
		txtSinglStrength = (TextView) findViewById(R.id.txt_signal_strength);
		txtSecurityLevel = (TextView) findViewById(R.id.txt_security_level);
		edtPassword = (EditText) findViewById(R.id.edt_password);
		cbxShowPass = (CheckBox) findViewById(R.id.cbx_show_pass);
		txtBtnCancel = (TextView) findViewById(R.id.txt_btn_cancel);
		txtBtnConn = (TextView) findViewById(R.id.txt_btn_connect);
		Btnwhite=(TextView) findViewById(R.id.button_white);
		Btnblack=(TextView) findViewById(R.id.button_black);
		
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
		    Btnwhite.setText("从白名单中删除");
		else
			Btnwhite.setText("加入白名单");
		
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
			    Btnblack.setText("从黑名单中删除");
			else
				Btnblack.setText("加入黑名单");
			
		
		
		txtWifiName.setText(wifiName);
		txtSinglStrength.setText(WifiAdmin.singlLevToStr(level));
		txtSecurityLevel.setText(securigyLevel);

		txtBtnConn.setEnabled(false);
		cbxShowPass.setEnabled(false);

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
