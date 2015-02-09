package com.cf.acg;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.cf.acg.fragment.*;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

public class Home extends Activity
{
	private static SlidingLayout slidingLayout;
	private static boolean firstBack = true;

	TextView textView;
	ListView menuList;

	Class<String> resClass; 			// 定义用于反射的类

	private FragmentManager fragmentManager;
	private Fragment[] fragments;
	private String[] fragmentNames =
	{ "home", "activity", "mate", "record", "article", "mine" };
	private final int fragmentNum = fragmentNames.length;

	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			((FragmentAbstract) msg.obj).setData();				// 设置相应类的数据
		}
	};

	private void init_widget()
	{
		/*
		 * 控件初始化
		 */
		menuList = (ListView) findViewById(R.id.menu_list);
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

		Home.setScrollEvent(findViewById(R.id.content));
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

	private void showFragment(int id)
	{
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		for (Fragment fragment : fragments)
		{
			fragmentTransaction.hide(fragment);
		}
		fragmentTransaction.show(fragments[id]).commit();

		if (!((FragmentAbstract) fragments[id]).hasDownload)
		{
			new HttpThread((DownloadInterface) fragments[id], handler).start();
			((FragmentAbstract) fragments[id]).hasDownload = true;
		}
	}

	private void handleFragment()
	{
		init_fragment();
		showFragment(1);
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

		setContentView(R.layout.home);

		MainActivity.activity.finish();

		init_widget();

		getResClass();

		handleFragment();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			Log.d("MC", "home");
			Toast.makeText(this, "213123", Toast.LENGTH_SHORT).show();
			return true;

		default:
			Log.d("MC", "default");
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (featureId)
		{
		case 0:
			SharedPreferences sp = getSharedPreferences("login",
					Context.MODE_PRIVATE);

			Editor editor = sp.edit();
			editor.clear();
			editor.commit();

			startActivity(new Intent(this, MainActivity.class));
			finish();
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
}
