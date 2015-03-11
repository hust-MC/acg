package com.cf.acg.detail;

import com.cf.acg.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PersonnalDetail extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_personnal);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personnal_detail, menu);
		return true;
	}

}
