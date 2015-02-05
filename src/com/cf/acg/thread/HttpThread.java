package com.cf.acg.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.cf.acg.fragment.FragmentAbstract;

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

	public static void httpConnect(String urlAddress, File file)
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

			FileOutputStream fos = new FileOutputStream(file);

			byte[] buf = new byte[4 * 1024];
			int num;
			while ((num = is.read(buf)) != -1)
			{
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
}
