package com.cf.acg.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.MainActivity;
import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.JsonResolve;
import com.cf.acg.adapter.ContentAdapter;
import com.cf.acg.detail.DetailAbstract;
import com.cf.acg.thread.HttpThread;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

public abstract class FragmentAbstract extends Fragment
{
	public static File fileDir = new File(MainActivity.rootDir.getPath()
			+ "/Home/");

	protected final int fActivity = 1;
	protected final int fMate = 2;
	protected final int fRecord = 3;
	protected final int fArticle = 4;
	protected final int fMine = 5;
	protected int fType;					// 用于每个类存放各自上述类型

	protected Activity activity;
	protected JsonResolve jsonResolve;

	public boolean hasDownload = false;
	public boolean downloadException = false;

	protected ContentAdapter adapter = new ContentAdapter();
	protected List<Object> list = new ArrayList<Object>();				//

	public abstract void addObj(List<Object> contentList, int position);
	public abstract void removeObj();
	public abstract void clear();

	public abstract Object readContent(JsonReader reader) throws IOException;

	public void getHttpConnection(int fObj)
	{
		String urlAddress = null;
		File file = null;

		switch (fObj)
		{
		case fActivity:
		{
			urlAddress = "http://acg.husteye.cn/api/activitylist?access_token="
					+ UserInfo.getToken();
			file = FragmentActivity.file;

			break;
		}
		case fArticle:
		{
			urlAddress = "http://acg.husteye.cn/api/articlelist?access_token="
					+ UserInfo.getToken();
			file = FragmentArticle.file;

			break;
		}
		case fMate:
		{
			urlAddress = "http://acg.husteye.cn/api/memberlist?access_token="
					+ UserInfo.getToken();
			file = FragmentMate.file;

			break;
		}
		case fRecord:
		{
			urlAddress = "http://acg.husteye.cn/api/dutylist?access_token="
					+ UserInfo.getToken();
			file = FragmentRecord.file;

			break;
		}
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
