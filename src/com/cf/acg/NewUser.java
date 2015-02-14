package com.cf.acg;

import com.cf.acg.thread.DownloadInterface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;

public class NewUser extends Activity implements DownloadInterface
{
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
	};

	@Override
	public void download()
	{
		// TODO Auto-generated method stub

	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.new_user, menu);
		return true;
	}

}
