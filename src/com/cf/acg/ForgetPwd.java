package com.cf.acg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.JsonReader;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPwd extends Activity implements DownloadInterface
{
	EditText input_id, input_phone;
	String id, phone;
	private File file = new File(MainActivity.logDir, "forget");
	private boolean verificateSuccess;
	private String result;
	private LoadingProcess loadingProcess = new LoadingProcess(this);

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			loadingProcess.dismissDialog();
			Toast.makeText(ForgetPwd.this, result, Toast.LENGTH_SHORT).show();
			if (verificateSuccess)
			{
				Intent intent = new Intent(ForgetPwd.this, ResetPwd.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		}
	};

	private void init_widget()
	{
		input_id = (EditText) findViewById(R.id.forget_input_id);
		input_phone = (EditText) findViewById(R.id.forget_input_phone);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);

		init_widget();
	}

	@Override
	public void download()
	{
		String urlAddress = "http://acg.husteye.cn/api/forgetpassword?username="
				+ id + "&mobile=" + phone;
		HttpThread.httpConnect(urlAddress, file);

		try
		{
			JsonReader reader = new JsonReader(new InputStreamReader(
					new FileInputStream(file)));
			reader.beginObject();

			while (reader.hasNext())
			{
				String field = reader.nextName();
				if (field.equals("success"))
				{
					reader.nextBoolean();
					verificateSuccess = true;
				}
				else if (field.equals("error"))
				{
					reader.nextString();
					verificateSuccess = false;
				}
				else if (field.equals("message"))
				{
					result = reader.nextString();
				}
				else
				{
					reader.skipValue();
				}
			}

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void onClick_getVerification(View view)
	{
		id = input_id.getText().toString();
		phone = input_phone.getText().toString();

		loadingProcess.startLoading("正在获取验证码，请稍后");

		new HttpThread(this, handler).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.forget_pwd, menu);
		return true;
	}
}
