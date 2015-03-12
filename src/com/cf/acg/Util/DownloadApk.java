package com.cf.acg.Util;

import java.io.File;

import com.cf.acg.AcgActivity;
import com.cf.acg.VersionUpdate;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;
import com.cf.acg.thread.SetProgressInterface;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class DownloadApk implements DownloadInterface, SetProgressInterface
{
	File file;
	String urlAddress;
	Context context;

	public DownloadApk(Context context, File file, String urlAddress)
	{
		this.context = context;
		this.file = file;
		this.urlAddress = urlAddress;
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.obj == null)
			{
				Toast.makeText(context, "网络连接错误，请检查网络状态", Toast.LENGTH_LONG)
						.show();
			}
			((VersionUpdate) context).downloadAppSuccess();
		}
	};

	@Override
	public void download()
	{
		HttpThread.httpConnect(this, urlAddress, file);
	}

	public void startDownload()
	{
		new HttpThread(this, handler).start();
	}

	@Override
	public void setProgress(int progress)
	{
		((VersionUpdate) context).setDownloadProgress(progress);
	}

	@Override
	public void setMaxProgress(int max)
	{
		((VersionUpdate) context).setMaxProgress(max);
	}
}
