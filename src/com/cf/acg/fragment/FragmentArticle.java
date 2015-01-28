package com.cf.acg.fragment;

import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.adapter.ContentAdaper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentArticle extends Fragment implements ContentInterface
{
	private Activity activity;
	private ListView listView;

	private ContentAdaper adaper = new ContentAdaper();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_article, null);
	}

	private void init_widget()
	{
		/*
		 * 初始化变量
		 */
		listView = (ListView) activity.findViewById(R.id.list_article);
		listView.setAdapter(adaper);

		Home.setScrollEvent(listView);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();
		super.onActivityCreated(savedInstanceState);
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

}
