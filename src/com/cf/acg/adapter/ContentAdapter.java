package com.cf.acg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cf.acg.fragment.FragmentAbstract;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ContentAdapter extends BaseAdapter
{
	private List<Object> contentList;				// 存储不同类的数据类
	FragmentAbstract content;

	private LinearLayout linearLayout;				// 返回用于显示的View对象

	/**
	 * 清空listview的内容
	 * 
	 * @author MC
	 */
	public void clearContentList()
	{
		try
		{
			contentList.clear();
		} catch (Exception e)
		{
			Log.d("MC", "exception");
		}
	}

	public LinearLayout getLinearLayout()
	{
		return linearLayout;
	}
	public void setLinearLayout(LinearLayout linearLayout)			// 传入listview所在的布局容器，
	// 以便getView函数使用
	{
		this.linearLayout = linearLayout;
	}

	public ContentAdapter()
	{
		contentList = new ArrayList<Object>();
	}

	// ==========================ListView 必备方法=============================
	/*
	 * 用于返回LiseView的数量
	 */
	@Override
	public int getCount()
	{
		Log.d("MC", "getCount");
		return contentList.size();
	}
	/*
	 * 用于返回将要加载的行
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Log.d("MC", "getView");
		content.addObj(contentList, convertView, position);
		return linearLayout;
	}

	@Override
	public Object getItem(int position)
	{
		Log.d("MC", "getItem");
		return null;
	}
	@Override
	public long getItemId(int position)
	{
		Log.d("MC", "getId");
		return 0;
	}
	// =====================================END====================================

	/*
	 * 为content Fragment类添加一行，并存储到contentList中
	 */
	public void addContent(FragmentAbstract content, Object object)
	{
		contentList.add(object);
		this.content = content;
		notifyDataSetChanged();
	}
}
