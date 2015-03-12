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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class HttpThread extends Thread
{
	DownloadInterface downloadClass;
	SetProgressInterface setProgressInterface;
	Handler handler;

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
		} catch (MalformedURLException e)
		{
		} catch (IOException e)
		{
		}
	}

	/*
	 * 不带进度条显示的下载函数
	 */
	public static void httpConnect(String urlAddress, File file)
	{
		httpConnect(null, urlAddress, file);
	}
}
