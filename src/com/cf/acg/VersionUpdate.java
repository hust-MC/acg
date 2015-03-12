package com.cf.acg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cf.acg.Util.DownloadApk;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.JsonReader;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class VersionUpdate extends AcgActivity implements DownloadInterface
{
	private File file = new File(MainActivity.logDir, "/version");

	private String nativeVersion;
	private String latestVersion;

	File fileAPK = new File(Environment.getExternalStorageDirectory()
			+ "/ACG/Download/音控组.apk");
	String urlAddressAPK = "http://acg.husteye.cn/api/download_app?platform=android";

	private ProgressBar bar;

	@Override
	public void afterDownload(Message msg)
	{
		String message = null;

		loadingProcess.dismissDialog();

		if (nativeVersion.equals(latestVersion))
		{
			message = "当前已经是最新版";
			new AlertDialog.Builder(VersionUpdate.this).setTitle("版本检查")
					.setMessage(message).setNegativeButton("确定", null).show();
		}
		else
		{
			message = "最新版本为:v" + latestVersion + "\n是否立即下载？";
			new AlertDialog.Builder(VersionUpdate.this)
					.setTitle("版本检查")
					.setMessage(message)
					.setPositiveButton("取消", null)
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									bar.setVisibility(View.VISIBLE);
									new DownloadApk(VersionUpdate.this,
											fileAPK, urlAddressAPK)
											.startDownload();
								}
							}).show();
		}
	}

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
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setDownloadProgress(int progress)
	{
		bar.setProgress(progress);
	}

	public void setMaxProgress(int max)
	{
		bar.setMax(max);
	}

	public void downloadAppSuccess()
	{
		Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(fileAPK),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public void init_widget()
	{
		bar = (ProgressBar) findViewById(R.id.updata_progress);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_version_update);

		init_widget();
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

		new HttpThread(this, acgHandler).start();
	}
}
