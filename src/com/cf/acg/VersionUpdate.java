package com.cf.acg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.JsonReader;
import android.view.Menu;
import android.view.View;

public class VersionUpdate extends Activity implements DownloadInterface
{
	private File file = new File(MainActivity.logDir, "/version");

	private String nativeVersion;
	private String latestVersion;
	private LoadingProcess loadingProcess;

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			String message = null;

			loadingProcess.dismissDialog();

			if (nativeVersion.equals(latestVersion))
			{
				message = "当前已经是最新版";
			}
			else
			{
				message = "最新版本为:v" + latestVersion;
			}
			new AlertDialog.Builder(VersionUpdate.this).setTitle("版本检查")
					.setMessage(message).setNegativeButton("确定", null).show();
		}
	};

	@Override
	public void download()
	{
		String urlAddress = "http://acg.husteye.cn/api/check_update?platform=android";
		HttpThread.httpConnect(urlAddress, file);

		try
		{
			JsonReader reader = new JsonReader(new InputStreamReader(
					new FileInputStream(file)));

			reader.beginObject();
			while (reader.hasNext())
			{
				String field = reader.nextName();
				if (field.equals("android"))
				{
					latestVersion = reader.nextString().substring(0, 5);
				}
				else
				{
					reader.skipValue();
				}
			}
			reader.endObject();
			reader.close();

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_version_update);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.version_update, menu);
		return true;
	}

	public void onClick_checkUpdate(View view) throws NameNotFoundException
	{
		loadingProcess = new LoadingProcess(this);
		loadingProcess.startLoading("正在检查更新");

		PackageManager pm = getPackageManager();
		PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
		nativeVersion = pi.versionName;

		new HttpThread(this, handler).start();
	}
}
