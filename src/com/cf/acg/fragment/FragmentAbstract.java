package com.cf.acg.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.cf.acg.adapter.ContentAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Environment;

public abstract class FragmentAbstract extends Fragment
{
	static File fileDir = new File(Environment.getExternalStorageDirectory()
			+ "/ACG/temp/");

	protected final int fActivity = 1;
	protected final int fMate = 2;
	protected final int fRecord = 3;
	protected final int fArticle = 4;
	protected final int fMine = 5;

	protected Activity activity;
	public boolean hasDownload = false;
	public boolean downloadException = false;

	protected ContentAdapter adapter = new ContentAdapter();
	protected List<Object> list = new ArrayList<Object>();

	public abstract void addObj(List<Object> contentList, int position);
	public abstract void removeObj();
	public abstract void clear();
	public abstract void download();

	public void getHttpConnection(int fObj)
	{
		String urlAddress = null;
		File file = null;

		switch (fObj)
		{
		case fActivity:
		{
			urlAddress = "http://acg.husteye.cn/api/activitylist?access_token=9926841641313132";
			file = FragmentActivity.file;

			if (!file.exists())
			{
				fileDir.mkdirs();
				try
				{
					file.createNewFile();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			break;
		}
		}
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
			downloadException = false;
		} catch (MalformedURLException e)
		{
			downloadException = true;
		} catch (IOException e)
		{
			downloadException = true;
		}
	}

	public void setData()
	{
		for (Object o : list)
		{
			adapter.addContent(this, o);
		}
	}
}
