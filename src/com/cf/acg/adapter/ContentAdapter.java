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

	public List<Object> getContentList()
	{
		return contentList;
	}

	/**
	 * 清空listview的内容
	 * 
	 * @author MC
	 */
	public void clearContentList()
	{
		try
		{
			Log.d("MC", "size: " + contentList.size() + "");
			contentList.clear();
			notifyDataSetChanged();					// 非常重要，否则会数组越界异常！！
			Log.d("MC", "size: " + contentList.size() + "");
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
		return contentList.size();
	}
	/*
	 * 用于返回将要加载的行
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		content.addObj(contentList, convertView, position);
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
