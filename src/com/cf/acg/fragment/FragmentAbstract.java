package com.cf.acg.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cf.acg.adapter.ContentAdapter;

import android.app.Activity;
import android.app.Fragment;

public abstract class FragmentAbstract extends Fragment
{
	protected final int fActivity = 1;
	protected final int fMate = 2;
	protected final int fRecord = 3;
	protected final int fArticle = 4;
	protected final int fMine = 5;

	protected Activity activity;
	public boolean hasDownload = false;
	protected ContentAdapter adapter = new ContentAdapter();
	protected List<Object> list = new ArrayList<Object>();

	public abstract void addObj(List<Object> contentList, int position);
	public abstract void removeObj();
	public abstract void clear();

	public abstract void download();

	public void setData()
	{
		for (Object o : list)
		{
			adapter.addContent(this, o);
		}
	}

	class ContentAbstract
	{
		public ContentAbstract()
		{
			// TODO Auto-generated constructor stub
		}
	}
}
