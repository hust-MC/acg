package com.cf.acg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.fragment.*;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

public class Home extends AcgActivity implements DownloadInterface
{

	private static SlidingLayout slidingLayout;
	private static boolean firstBack = true;

	TextView textView;
	ListView menuList;
	ImageView iv_noNet;

	Class<String> resClass; 			// 定义用于反射的类

	private File file = new File(MainActivity.logDir, "logout");
	private FragmentManager fragmentManager;
	private Fragment[] fragments;
	private Content content;

	private String[] fragmentNames =
	{ "activity", "mate", "record", "article", "mine" };
	private final int fragmentNum = fragmentNames.length;

	@Override
	public void afterDownload(Message msg)
	{
		if (msg.what == 0x55)
		{
			iv_noNet.setVisibility(View.VISIBLE);
		}
		else if (msg.what == 0)
		{
			iv_noNet.setVisibility(View.GONE);
		}
		loadingProcess.dismissDialog();
		((FragmentAbstract) msg.obj).setData();				// 设置相应类的数据
	}

	Handler handlerLogout = new Handler()			// 用处理注销内容的handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			loadingProcess.dismissDialog();
			Toast.makeText(Home.this, content.message, Toast.LENGTH_SHORT)
					.show();
		}
	};

	private void init_widget()
	{
		/*
		 * 控件初始化
		 */
		menuList = (ListView) findViewById(R.id.menu_list);
		iv_noNet = (ImageView) findViewById(R.id.no_net);
		slidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);

		/*
		 * 处理ListView
		 */
		menuList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.menu_list_item, getResources().getStringArray(
						R.array.menu_array)));
		menuList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				slidingLayout.scrollToRightLayout();
				slidingLayout.setLeftLayoutVisible(false);
				showFragment(position);
			}
		});
	}

	private void getResClass()
	{
		Class[] resourceClasses = R.class.getClasses();

		for (Class resource : resourceClasses)
		{
			try
			{
				resource.getField("fragment_" + fragmentNames[0]);
				if (resource.getName().equals("com.cf.acg.R$id"))
				{
					resClass = resource;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	private int getResourceID(String name)
	{
		try
		{
			return resClass.getField(name).getInt(null);
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	private void init_fragment()
	{
		fragments = new Fragment[fragmentNum];
		fragmentManager = getFragmentManager();

		for (int i = 0; i < fragmentNum; i++)
		{
			fragments[i] = (Fragment) fragmentManager
					.findFragmentById(getResourceID("fragment_"
							+ fragmentNames[i]));
		}
	}

	/*
	 * 显示传入的id对应的fragment
	 */
	private void showFragment(int id)
	{
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		for (Fragment fragment : fragments)
		{
			fragmentTransaction.hide(fragment);
		}
		fragmentTransaction.show(fragments[id]).commit();

		/*
		 * 如果没有显示过，则下载数据。 下载完成之后跳到本函数的handler中表示下载成功
		 */
		if (!((FragmentAbstract) fragments[id]).hasDownload)
		{
			loadingProcess.startLoading();

			new HttpThread((DownloadInterface) fragments[id], acgHandler)
					.start();
			((FragmentAbstract) fragments[id]).hasDownload = true;
		}
	}

	private void handleFragment()
	{
		init_fragment();
		showFragment(0);
	}

	public static void setScrollEvent(View v)
	{
		slidingLayout.setScrollEvent(v);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		setContentView(R.layout.activity_home);

		loadingProcess = new LoadingProcess(this);

		MainActivity.activity.finish();

		init_widget();

		getResClass();

		handleFragment();
	}

	@Override
	public void download()
	{
		String urlAddress = "http://acg.husteye.cn/api/logout?access_token="
				+ UserInfo.getToken();
		HttpThread.httpConnect(urlAddress, file);

		String message = null;
		try
		{
			JsonReader reader = new JsonReader(new InputStreamReader(
					new FileInputStream(file)));

			reader.beginObject();

			while (reader.hasNext())
			{
				String field = reader.nextName();

				if (field.equals("message"))
				{
					message = reader.nextString();
				}
				else
				{
					reader.skipValue();
				}
			}
			reader.endObject();
			reader.close();

			content = new Content(message);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void getLogoutMessage()
	{
		new HttpThread(this, handlerLogout).start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (firstBack)
			{
				firstBack = false;
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();

				Timer exitTimer = new Timer();
				exitTimer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						firstBack = true;
					}
				}, 2000);
			}
			else
			{
				finish();
				System.exit(0);
			}
		}
		return true;
	}

	public void onClick_checkNet(View v)
	{
		startActivity(new Intent(
				android.provider.Settings.ACTION_WIRELESS_SETTINGS));

		iv_noNet.setVisibility(View.GONE);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getOrder())
		{
		case 0:				// 注销函数

			LoadingProcess loadingProcess = new LoadingProcess(this);
			loadingProcess.startLoading("正在为您注销");

			SharedPreferences sp = getSharedPreferences("login",
					Context.MODE_PRIVATE);

			Editor editor = sp.edit();
			editor.clear();
			editor.commit();

			getLogoutMessage();			// 向服务器提交注销信息
			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;

		case 1:
			startActivity(new Intent(this, VersionUpdate.class));
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	class Content
	{
		String message;

		public Content(String message)
		{
			this.message = message;
		}
	}
}
