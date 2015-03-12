package com.cf.acg;

import com.cf.acg.thread.HttpThread;

import android.app.Activity;

public class AcgActivity extends Activity
{
	/*
	 * 当无网络的时候调用此函数
	 */
	public void noNet()
	{
		HttpThread.showNoNetDialog(this);
	}
}
