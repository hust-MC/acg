package com.cf.acg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.fragment.FragmentAbstract;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements DownloadInterface
{
	public static final String[] venueName =
	{ "未知", "305", "513", "东四" };
	public static final String[] activityStatusName =
	{ "未知", "排班中", "正在进行", "已结束", "已取消" };
	public static final String[] weekNum =
	{ "日", "一", "二", "三", "四", "五", "六" };

	public static Activity activity;				// 获取本activity的上下文，用于在登录之后关闭登陆界面
	public static File logDir = new File(
			Environment.getExternalStorageDirectory() + "/ACG/Log/");

	private EditText input_id, input_pwd;
	private SharedPreferences sp = getSharedPreferences("login",
			Context.MODE_PRIVATE);
	private Content_login content;
	
	
	private File file = new File(logDir, "login");
	private LoadingProcess loadingProcess;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			loadingProcess.dismissDialog();
			if (content.error)
			{
				Toast.makeText(MainActivity.this, content.message,
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				Editor editor = sp.edit();
				editor.putString("id", input_id.getText().toString());
				editor.putString("pwd", input_pwd.getText().toString());
				editor.commit();
				startActivity(new Intent(MainActivity.this, Home.class));
			}
		}
	};

	private void init_widget()
	{
		input_id = (EditText) findViewById(R.id.login_input_id);
		input_pwd = (EditText) findViewById(R.id.login_input_pwd);
	}

	@Override
	public void download()
	{
		String message = null;
		boolean error = false;

		final String urlAddress = "http://acg.husteye.cn//api/login?username="
				+ input_id.getText().toString() + "&password="
				+ input_pwd.getText().toString();

		HttpThread.httpConnect(urlAddress, file);

		JsonReader reader;
		try
		{
			reader = new JsonReader(new InputStreamReader(new FileInputStream(
					file)));

			reader.beginObject();
			while (reader.hasNext())
			{
				String field = reader.nextName();

				if (field.equals("access_token"))
				{
					UserInfo.setToken(reader.nextString());
				}
				else if (field.equals("name"))
				{
					UserInfo.setName(reader.nextString());
				}
				else if (field.equals("uid"))
				{
					UserInfo.setUid(reader.nextString());
				}
				else if (field.equals("message"))
				{
					message = reader.nextString();
					error = true;
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

		content = new Content_login(message, error);
	}

	private boolean getState()
	{

		input_id.setText(sp.getString("id", null));
		input_pwd.setText(sp.getString("pwd", null));

		if (input_id.getText().toString() == null
				|| input_id.getText().toString().isEmpty())
		{
			return false;
		}
		return true;
	}

	private void checkPwd()
	{
		new HttpThread(this, handler).start();		// 开启线程回调download方法
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		loadingProcess = new LoadingProcess(this);
		loadingProcess.startLoading("正在验证身份，请稍后");

		setContentView(R.layout.login);

		activity = this;

		init_widget();
		if (getState())				// 获取记住的登录状态
		{
			checkPwd();				// 检查用户名、密码
		}
	}
	public void onClick_login(View view)
	{
		JPushInterface.setAlias(this, input_id.getText().toString(),
				new TagAliasCallback()
				{
					@Override
					public void gotResult(int code, String alias,
							Set<String> tags)
					{
						String logs;
						switch (code)
						{
						case 0:
							logs = "Set tag and alias success";
							break;

						case 6002:
							logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
							Log.d("MC", logs);
							break;

						default:
							logs = "Failed with errorCode = " + code;
							Log.e("MC", logs);
						}
					}
				});

		checkPwd();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class Content_login
	{
		String message;
		boolean error;

		public Content_login(String message, boolean error)
		{
			this.message = message;
			this.error = error;
		}
	}
}
