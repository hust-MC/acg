package com.cf.acg.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.cf.acg.detail.DetailAbstract;
import com.cf.acg.fragment.FragmentAbstract;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class HttpThread extends Thread
{
	DownloadInterface downloadClass;
	SetProgressInterface setProgressInterface;
	Handler handler;
	static public boolean hasNet = true;		// 标志网络状态

	static int progress = 0;					// 显示下载进度

	public HttpThread(DownloadInterface downloadClass, Handler handler)
	{
		this.downloadClass = downloadClass;
		this.handler = handler;
	}

	@Override
	public void run()
	{
		downloadClass.download();
		Message message = handler.obtainMessage();
		if (!hasNet)
		{
			message.what = 0x55;			// 标志错误
		}
		else
		{
			message.what = 0;
		}
		message.obj = downloadClass;
		message.sendToTarget();
	}

	public static Bitmap httpConnect(String urlAddress)
	{
		try
		{
			URL url = new URL(urlAddress);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			InputStream is = httpURLConnection.getInputStream();

			return BitmapFactory.decodeStream(is);
		} catch (IOException e)
		{
		}
		return null;
	}

	/*
	 * 带进度条显示的下载函数
	 */
	public static void httpConnect(SetProgressInterface setProgressInterface,
			String urlAddress, File file)
	{
		try
		{
			URL url = new URL(urlAddress);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			InputStream is = httpURLConnection.getInputStream();

			if (setProgressInterface != null)
			{
				setProgressInterface.setMaxProgress(httpURLConnection
						.getContentLength());
			}

			if (!file.exists())
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file, false);

			byte[] buf = new byte[4 * 1024];
			int num;

			while ((num = is.read(buf)) != -1)		// 实时调整下载进度条
			{
				progress += num;
				if (setProgressInterface != null)
				{
					setProgressInterface.setProgress(progress);
				}

				fos.write(buf, 0, num);
			}
			fos.flush();
			fos.close();
			hasNet = true;					// 设置网络状态为正常
		} catch (MalformedURLException e)
		{
		} catch (IOException e)
		{
			hasNet = false;					// 标志网络异常
		}
	}
	/*
	 * 不带进度条显示的下载函数
	 */
	public static void httpConnect(String urlAddress, File file)
	{
		httpConnect(null, urlAddress, file);
	}

	/*
	 * 无网络的时候调用此函数
	 */
	public static void showNoNetDialog(Context context)
	{
		Toast.makeText(context, "请检查网络连接状态", Toast.LENGTH_LONG).show();
	}
}
