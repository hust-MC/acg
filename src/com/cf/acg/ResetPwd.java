package com.cf.acg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

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

public class ResetPwd extends AcgActivity implements DownloadInterface
{
	private EditText input_pwd, input_pwd1, input_vertification;
	private String vertification, pwd, pwd1, id;
	private String result;				// 接收显示的提示信息
	private boolean resetSuccess;		// 标志是否重置成功

	private File file = new File(MainActivity.logDir, "reset");

	@Override
	public void onDownloadFinished(Message msg)
	{
		Toast.makeText(ResetPwd.this, result, Toast.LENGTH_SHORT).show();
		if (resetSuccess)
		{
			Intent intent = new Intent(ResetPwd.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public void download()
	{
		String urlAddress = "http://acg.husteye.cn/api/resetpassword?username="
				+ id + "&reset_password_token=" + vertification + "&password="
				+ pwd;

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
					resetSuccess = true;
				}
				else if (field.equals("error"))
				{
					reader.nextString();
					resetSuccess = false;
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
			reader.endObject();
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
		input_pwd = (EditText) findViewById(R.id.reset_input_pwd);
		input_pwd1 = (EditText) findViewById(R.id.reset_input_pwd1);
		input_vertification = (EditText) findViewById(R.id.reset_input_vertification);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset);

		init_widget();
		id = getIntent().getExtras().getString("id");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.reset_pwd, menu);
		return true;
	}

	public void onClick_resetPwd(View view)
	{
		vertification = input_vertification.getText().toString();
		pwd = input_pwd.getText().toString();
		pwd1 = input_pwd1.getText().toString();

		if (pwd.equals(pwd1))
		{
			new HttpThread(this, acgHandler).start();
		}
		else
		{
			Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
		}
	}
}
