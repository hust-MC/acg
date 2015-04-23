package com.cf.acg.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.MainActivity;
import com.cf.acg.R;
import com.cf.acg.RefreshLayout;
import com.cf.acg.UserInfo;
import com.cf.acg.RefreshLayout.OnLoadListener;
import com.cf.acg.Util.JsonResolve;
import com.cf.acg.adapter.ContentAdapter;
import com.cf.acg.detail.DetailAbstract;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public abstract class FragmentAbstract extends Fragment implements
		DownloadInterface
{
	public static File fileDir = new File(MainActivity.rootDir.getPath()
			+ "/Home/");

	protected RefreshLayout refreshableView;

	protected final int fActivity = 1;
	protected final int fMate = 2;
	protected final int fRecord = 3;
	protected final int fArticle = 4;
	protected final int fMine = 5;
	protected int fType;					// 用于每个类存放各自上述类型

	protected Activity activity;
	protected JsonResolve jsonResolve;

	protected int currentPage = 1;

	public boolean hasDownload = false;
	public boolean downloadException = false;

	protected ContentAdapter adapter = new ContentAdapter();
	protected List<Object> list = new ArrayList<Object>();

	public abstract void addObj(List<Object> contentList, View convertView,
			int position);
	public abstract void removeObj();						// 删除条目（待用）
	protected void clearListView()							// 清空listview
	{
		adapter.clearContentList();
	}

	public abstract Object readContent(JsonReader reader) throws IOException;

	/**
	 * 处理刷新的数据
	 */
	protected Handler handlerRefresh = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Log.d("MC", "refresh");
			Toast.makeText(FragmentAbstract.this.activity, "数据已更新",
					Toast.LENGTH_SHORT).show();
			clearListView();
			setData();
			refreshableView.setRefreshing(false);
		}
	};
	/**
	 * 处理上拉加载的数据
	 */
	protected Handler handlerLoadMore = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			Log.d("MC", "loadmore");
			setData();
			refreshableView.setLoading(false);
		}
	};

	protected void setRefreshListener()
	{

		/**
		 * 下拉刷新监听器
		 */
		refreshableView.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				currentPage = 1;
				new HttpThread(FragmentAbstract.this, handlerRefresh).start();
			}
		});
		refreshableView.setColorSchemeResources(android.R.color.holo_red_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_green_dark);

	}

	protected void setDownMoreListener()
	{
		/**
		 * 上拉加载监听器
		 */
		refreshableView.setOnLoadListener(new OnLoadListener()
		{
			@Override
			public void onLoad()
			{
				currentPage++;
				new HttpThread(FragmentAbstract.this, handlerLoadMore).start();
			}
		});
	}

	public void setRefreshLayoutEnable(boolean bool)
	{
		if (refreshableView != null)
		{
			refreshableView.setEnabled(bool);
		}
	}

	public void getHttpConnection(int fObj)
	{
		String urlAddress = null;
		File file = null;

		switch (fObj)
		{
		case fActivity:
			urlAddress = "http://acg.husteye.cn/api/activitylist?access_token="
					+ UserInfo.getToken() + "&pagenum=" + currentPage;
			file = FragmentActivity.file;
			break;

		case fArticle:
			urlAddress = "http://acg.husteye.cn/api/articlelist?access_token="
					+ UserInfo.getToken();
			file = FragmentArticle.file;
			break;

		case fMate:
			urlAddress = "http://acg.husteye.cn/api/memberlist?access_token="
					+ UserInfo.getToken();
			file = FragmentMate.file;
			break;

		case fRecord:
			urlAddress = "http://acg.husteye.cn/api/dutylist?access_token="
					+ UserInfo.getToken() + "&pagenum=" + currentPage;
			file = FragmentRecord.file;
			break;
		}
		HttpThread.httpConnect(urlAddress, file);
	}
	public void setData()
	{
		for (Object o : list)
		{
			adapter.addContent(this, o);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == DetailAbstract.RESULT_CODE_NO_NET)
		{
			(activity.findViewById(R.id.no_net)).setVisibility(View.VISIBLE);
		}
		else if (resultCode == DetailAbstract.RESULT_CODE_NET)
		{
			(activity.findViewById(R.id.no_net)).setVisibility(View.GONE);
		}
	}
}
