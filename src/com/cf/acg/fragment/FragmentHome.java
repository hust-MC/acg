package com.cf.acg.fragment;

import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class FragmentHome extends FragmentAbstract
{


	Class<String> resClass; 			// 定义用于反射的类

	private FragmentManager fragmentManager;
	private Fragment[] fragments;
	private String[] fragmentNames =
	{ "1", "2", "3", "4", };
	private final int fragmentNum = fragmentNames.length;

	private Button bt1, bt2, bt3, bt4;				// 底部导航栏四个按钮
	private ButtonListener buttonListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_home, null);
	}

	private void init_widget()
	{
		buttonListener = new ButtonListener();

		Home.setScrollEvent(activity.findViewById(R.id.home_page1_text));
		Home.setScrollEvent(activity.findViewById(R.id.home_page2_text));
		Home.setScrollEvent(activity.findViewById(R.id.home_page3_text));
		Home.setScrollEvent(activity.findViewById(R.id.home_page4_text));

		((Button) activity.findViewById(R.id.home_bt1))
				.setOnClickListener(buttonListener);
		((Button) activity.findViewById(R.id.home_bt2))
				.setOnClickListener(buttonListener);
		((Button) activity.findViewById(R.id.home_bt3))
				.setOnClickListener(buttonListener);
		((Button) activity.findViewById(R.id.home_bt4))
				.setOnClickListener(buttonListener);
	}
	private void getResClass()
	{
		Class[] resourceClasses = R.class.getClasses();

		for (Class resource : resourceClasses)
		{
			try
			{
				resource.getField("fragment_home_page" + fragmentNames[0]);
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
					.findFragmentById(getResourceID("fragment_home_page"
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
		showFragment(0);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();

		getResClass();

		handleFragment();

		super.onActivityCreated(savedInstanceState);
	}

	class ButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			int index = 0;
			switch (v.getId())
			{
			case R.id.home_bt1:
				index = 0;
				break;
			case R.id.home_bt2:
				index = 1;
				break;
			case R.id.home_bt3:
				index = 2;
				break;
			case R.id.home_bt4:
				index = 3;
				break;
			}
			showFragment(index);
		}
	}

	@Override
	public void addObj(List<Object> contentList, int position)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObj()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void download()
	{
		// TODO Auto-generated method stub
		
	}
}
