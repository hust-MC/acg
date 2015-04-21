package com.cf.acg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

public class MainActivity extends AcgActivity implements DownloadInterface
{
	public static File rootDir = new File(
			Environment.getExternalStorageDirectory() + "/ACG");
	public static File logDir = new File(rootDir.getPath() + "/LOG/");

	public static final String[] venueName =
	{ "未知", "305", "513", "东四" };
	public static final String[] activityStatusName =
	{ "未知", "排班中", "正在进行", "已结束", "已取消" };
	public static final String[] weekNum =
	{ "日", "一", "二", "三", "四", "五", "六" };

	public static Activity activity;				// 获取本activity的上下文，用于在登录之后关闭登陆界面

	private EditText input_id, input_pwd;
	private SharedPreferences sp;
	private Content_login content;
	private Editor editor;
	private String id;					// 用于保存用户的ID和密码
	private String pwd;
	private boolean hasDialogShow;		// 标记是否打开对话框
	private WhichToDownload whichToDownload = WhichToDownload.LOGIN;			// 选择下载内容

	private File file = new File(logDir, "login");

	@Override
	protected void onDownloadFinished(Message msg)
	{
		switch (whichToDownload)
		{
		case LOGIN:
			if (hasDialogShow)
			{
				loadingProcess.dismissDialog();
				hasDialogShow = false;
			}
			if (content.error)
			{
				Toast.makeText(MainActivity.this, content.message,
						Toast.LENGTH_SHORT).show();
			}
			else
			{
				editor = sp.edit();
				editor.putString("id", id);
				editor.putString("pwd", pwd);
				editor.commit();
				startActivity(new Intent(MainActivity.this, Home.class));
			}
			break;

		case FORGET_PWD:

			break;

		case NEW_USER:

			break;
		}
	}

	private void init_widget()
	{
		input_id = (EditText) findViewById(R.id.login_input_id);
		input_pwd = (EditText) findViewById(R.id.login_input_pwd);
	}

	@Override
	public void download()
	{
		final String urlAddress;
		switch (whichToDownload)
		{
		case LOGIN:
			String message = null;
			boolean error = false;

			urlAddress = "http://acg.husteye.cn/api/login?username=" + id
					+ "&password=" + pwd;

			HttpThread.httpConnect(urlAddress, file);

			JsonReader reader;
			try
			{
				reader = new JsonReader(new InputStreamReader(
						new FileInputStream(file)));

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

			break;

		case FORGET_PWD:

			break;

		case NEW_USER:
			break;
		}

	}
	private void checkPwd()
	{
		new HttpThread(this, acgHandler).start();		// 开启线程回调download方法
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("login", Context.MODE_PRIVATE);
		id = sp.getString("id", null);
		pwd = sp.getString("pwd", null);
		activity = this;

		if (id != null && !id.isEmpty())				// 准备自动登录
		{
			loadingProcess = new LoadingProcess(this);
			loadingProcess.startLoading("正在验证身份，请稍后");
			hasDialogShow = true;

			checkPwd();
		}
		else
		// 不自动登录，则显示登录界面
		{
			setContentView(R.layout.activity_login);
			init_widget();
		}
	}

	/*
	 * 登录按钮
	 */
	public void onClick_login(View view)
	{
		id = input_id.getText().toString();
		pwd = input_pwd.getText().toString();

		whichToDownload = WhichToDownload.LOGIN;

		JPushInterface.setAlias(this, id, new TagAliasCallback()
		{
			@Override
			public void gotResult(int code, String alias, Set<String> tags)
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

	/*
	 * 忘记密码事件处理
	 */
	public void onClick_forgetPwd(View view)
	{
		startActivity(new Intent(this, ForgetPwd.class));
	}

	/*
	 * 注册事件处理
	 */
	public void onClick_newUser(View view)
	{
		startActivity(new Intent(this, NewUser.class));
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
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

	enum WhichToDownload
	{
		LOGIN, FORGET_PWD, NEW_USER
	}
}
