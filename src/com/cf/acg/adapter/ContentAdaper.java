package com.cf.acg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cf.acg.fragment.ContentInterface;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContentAdaper extends BaseAdapter
{
	List<?> contentList;

	public ContentAdaper()
	{
		contentList = new ArrayList();
	}

	@Override
	public int getCount()
	{
		return contentList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void addList(ContentInterface obj)
	{
		obj.addObj();
	}

}
