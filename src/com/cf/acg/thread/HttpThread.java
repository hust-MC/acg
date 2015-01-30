package com.cf.acg.thread;

import com.cf.acg.fragment.FragmentAbstract;

import android.app.Fragment;
import android.os.Handler;
import android.os.Message;

public class HttpThread extends Thread
{
	FragmentAbstract fa;
	Handler handler;

	public HttpThread(FragmentAbstract fa, Handler handler)
	{
		this.fa = fa;
		this.handler = handler;
	}

	@Override
	public void run()
	{
		fa.download();
		Message message = handler.obtainMessage();
		message.obj = fa;
		message.sendToTarget();
	}
}
