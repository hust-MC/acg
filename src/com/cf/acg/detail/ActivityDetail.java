package com.cf.acg.detail;

import com.cf.acg.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ActivityDetail extends Activity
{

	private void init_wiget()
	{
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);

		init_wiget();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_detail, menu);
		return true;
	}

}
