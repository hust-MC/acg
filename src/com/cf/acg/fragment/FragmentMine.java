package com.cf.acg.fragment;

import java.io.IOException;
import java.util.List;

import cn.jpush.android.util.ac;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FragmentMine extends FragmentAbstract implements DownloadInterface
{

	Class<String> resClass; 			// 定义用于反射的类

	private FragmentManager fragmentManager;
	private Fragment[] fragments;
	private String[] fragmentNames =
	{ "1", "2", "3", };
	private final int fragmentNum = fragmentNames.length;

	private LoadingProcess loadingProcess;
	private ButtonListener buttonListener;
	private ImageView iv_bt1, iv_bt2, iv_bt3;			// 底部导航栏四个按钮图片

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			loadingProcess.dismissDialog();
			((FragmentAbstract) msg.obj).setData();				// 设置相应类的数据
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_mine, null);
	}

	private void init_widget()
	{
		buttonListener = new ButtonListener();

		/*
		 * 底部四个导航按钮单击事件
		 */
		((LinearLayout) activity.findViewById(R.id.home_bt1))
				.setOnClickListener(buttonListener);
		((LinearLayout) activity.findViewById(R.id.home_bt2))
				.setOnClickListener(buttonListener);
		((LinearLayout) activity.findViewById(R.id.home_bt3))
				.setOnClickListener(buttonListener);

		/*
		 * 获取底部四个按钮的imageview
		 */
		iv_bt1 = (ImageView) activity.findViewById(R.id.iv_bt1);
		iv_bt2 = (ImageView) activity.findViewById(R.id.iv_bt2);
		iv_bt3 = (ImageView) activity.findViewById(R.id.iv_bt3);
	}
	// 通过反射获得fragment所在的R类子类
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

	// 反射获得fragment的ID
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

	/*
	 * 初始化所有fragment
	 */
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
		/*
		 * 设置显示相应ID的fragment
		 */
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		for (Fragment fragment : fragments)
		{
			fragmentTransaction.hide(fragment);
		}
		fragmentTransaction.show(fragments[id]).commit();

		/*
		 * 将相应的底部按钮变色
		 */
		switch (id)
		{
		case 0:
			iv_bt1.setImageResource(R.drawable.my_activity);
			iv_bt2.setImageResource(R.drawable.my_message_grey);
			iv_bt3.setImageResource(R.drawable.my_info_grey);
			break;
		case 1:
			iv_bt1.setImageResource(R.drawable.my_activity_grey);
			iv_bt2.setImageResource(R.drawable.my_message);
			iv_bt3.setImageResource(R.drawable.my_info_grey);
			break;
		case 2:
			iv_bt1.setImageResource(R.drawable.my_activity_grey);
			iv_bt2.setImageResource(R.drawable.my_message_grey);
			iv_bt3.setImageResource(R.drawable.my_info);

			break;
		}

		/*
		 * 如果没有显示过，则下载数据。 下载完成之后跳到本函数的handler中表示下载成功
		 */
		if (!((FragmentAbstract) fragments[id]).hasDownload)
		{
			loadingProcess = new LoadingProcess(activity);
			loadingProcess.startLoading();

			new HttpThread((DownloadInterface) fragments[id], handler).start();
			((FragmentAbstract) fragments[id]).hasDownload = true;
		}
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
			}
			showFragment(index);
		}
	}

	@Override
	public void addObj(List<Object> contentList, View convertView, int position)
	{
	}

	@Override
	public void removeObj()
	{
	}

	@Override
	public void download()
	{
	}

	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		return null;
	}
}
