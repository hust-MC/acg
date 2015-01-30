package com.cf.acg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cf.acg.fragment.FragmentAbstract;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ContentAdapter extends BaseAdapter
{
	private List<Object> contentList;
	FragmentAbstract content;

	private LinearLayout linearLayout;				// 返回用于显示的View对象

	public LinearLayout getLinearLayout()
	{
		return linearLayout;
	}
	public void setLinearLayout(LinearLayout linearLayout)
	{
		this.linearLayout = linearLayout;
	}

	public ContentAdapter()
	{
		contentList = new ArrayList();
	}

	@Override
	public int getCount()
	{
		return contentList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		content.addObj(contentList, position);
		return linearLayout;
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

	public void addContent(FragmentAbstract content, Object object)
	{
		contentList.add(object);
		this.content = content;
		notifyDataSetChanged();
	}

}
