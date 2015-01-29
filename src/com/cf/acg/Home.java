package com.cf.acg;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Home extends Activity
{
	private static SlidingLayout slidingLayout;
	TextView textView;
	ListView menuList;

	Class<String> resClass; 			// 定义用于反射的类

	private FragmentManager fragmentManager;
	private Fragment[] fragments;
	private String[] fragmentNames =
	{ "home", "activity", "mate", "record", "article", "mine" };
	private final int fragmentNum = fragmentNames.length;

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
				SlidingLayout.leftLayout.setVisibility(View.GONE);
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

	private void showFragment(int id)
	{
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		for (Fragment fragment : fragments)
		{
			fragmentTransaction.hide(fragment);
		}
		fragmentTransaction.show(fragments[id]).commit();
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
		setContentView(R.layout.home);

		init_widget();

		getResClass();

		handleFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
}
