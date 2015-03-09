package com.cf.acg;

import com.cf.acg.Util.JsonResolve;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

public class NewUser extends Activity implements DownloadInterface
{
	private EditText input_id, input_pwd, input_name, input_email, input_phone,
			input_reqcode;
	private String id, pwd, name, email, phone, reqcode;
	private LoadingProcess loadingProcess = new LoadingProcess(this);
	private File file = new File(MainActivity.logDir, "newUser");

	private boolean registerSuccess = false; 		// 标志注册是否成功
	private String registerMessageArray = ""; 	// 注册结果提示数组
	private String registerMessage = "";			// 注册结果提示

	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			loadingProcess.dismissDialog();
			Toast.makeText(NewUser.this, registerMessage, Toast.LENGTH_SHORT)
					.show();
			registerMessage = "";

			if (registerSuccess)
			{
				finish();
			}
		}
	};

	@Override
	public void download()
	{
		String urlAddress = "http://acg.husteye.cn/api/register?username=" + id
				+ "&password=" + pwd + "&name=" + name + "&email=" + email
				+ "&mobile=" + phone + "&reqcode=" + reqcode;

		Log.d("MC", urlAddress);
		HttpThread.httpConnect(urlAddress, file);

		try
		{ /*
		 * 读取外层对象
		 */
			JsonReader reader = new JsonReader(new InputStreamReader(
					new FileInputStream(file)));

			reader.beginObject();
			while (reader.hasNext())
			{
				String field = reader.nextName();

				if (field.equals("success"))
				{
					registerSuccess = true;
					reader.nextBoolean();
				}
				else if (field.equals("error"))
				{
					registerSuccess = false;
					reader.nextString();
				}
				else if (field.equals("message"))
				{
					registerMessageArray = reader.nextString();
				}
			}
			reader.endObject();

			if (registerSuccess)
			{
				registerMessage = registerMessageArray;
			}
			else
			{
				/*
				 * 读取提示信息数组内层对象
				 */
				reader = new JsonReader(new StringReader(registerMessageArray));
				reader.beginArray();
				while (reader.hasNext())
				{
					registerMessage += reader.nextString() + "\n";
				}
				reader.endArray();
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void init_widget()
	{
		input_id = (EditText) findViewById(R.id.new_input_id);
		input_pwd = (EditText) findViewById(R.id.new_input_pwd);
		input_name = (EditText) findViewById(R.id.new_input_name);
		input_email = (EditText) findViewById(R.id.new_input_email);
		input_phone = (EditText) findViewById(R.id.new_input_phone);
		input_reqcode = (EditText) findViewById(R.id.new_input_reqcode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);

		init_widget();
	}

	public void onClick_Confirm(View view)
	{
		id = input_id.getText().toString();
		pwd = input_pwd.getText().toString();
		name = input_name.getText().toString();
		email = input_email.getText().toString();
		phone = input_phone.getText().toString();
		reqcode = input_reqcode.getText().toString();

		loadingProcess.startLoading("正在发送注册请求，请稍候。。。");
		new HttpThread(this, handler).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.new_user, menu);
		return true;
	}

}
