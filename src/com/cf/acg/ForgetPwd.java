package com.cf.acg;

import com.cf.acg.thread.DownloadInterface;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ForgetPwd extends Activity implements DownloadInterface
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.forget_pwd, menu);
		return true;
	}

	@Override
	public void download()
	{
		
	}

}
