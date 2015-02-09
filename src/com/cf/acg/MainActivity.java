package com.cf.acg;

import cn.jpush.android.api.JPushInterface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity
{
	public static final String[] venueName =
	{ "未知", "305", "513", "东四" };
	public static final String[] activityStatusName =
	{ "未知", "排班中", "正在进行", "已结束", "已取消" };
	public static final String[] weekNum =
	{ "日", "一", "二", "三", "四", "五", "六" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

	public void onClick_login(View view)
	{
		startActivity(new Intent(this, Home.class));		// 登陆成功
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
