package com.cf.acg;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.util.ac;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity
{
	public static final String[] venueName =
	{ "未知", "305", "513", "东四" };
	public static final String[] activityStatusName =
	{ "未知", "排班中", "正在进行", "已结束", "已取消" };
	public static final String[] weekNum =
	{ "日", "一", "二", "三", "四", "五", "六" };

	public static Activity activity;				// 获取本activity的上下文，用于在登录之后关闭登陆界面

	private EditText input_id, input_pwd;
	private SharedPreferences sp;

	private void init_widget()
	{
		input_id = (EditText) findViewById(R.id.login_input_id);
		input_pwd = (EditText) findViewById(R.id.login_input_pwd);
	}

	private void getState()
	{
		sp = getSharedPreferences("login", Context.MODE_PRIVATE);

		input_id.setText(sp.getString("id", null));
		input_pwd.setText(sp.getString("pwd", null));
	}

	private boolean checkPwd()
	{
		if (input_id.getText().toString().equals("M201371888")
				&& input_pwd.getText().toString().equals("123456"))
		{
			startActivity(new Intent(this, Home.class));		// 登录成功
			return true;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		activity = this;

		init_widget();
		getState();				// 获取记住的登录状态
		checkPwd();				// 检查用户名、密码
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
							Log.d("MC", logs);
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
		if (checkPwd())
		{
			Editor editor = sp.edit();
			editor.putString("id", input_id.getText().toString());
			editor.putString("pwd", input_pwd.getText().toString());
			editor.commit();
		}
		else
		{
			Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
