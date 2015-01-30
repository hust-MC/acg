package com.cf.acg.fragment;

import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.adapter.ContentAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentMate extends FragmentAbstract
{
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_mate, null);
	}

	public void init_widget()
	{
		listView = (ListView) activity.findViewById(R.id.list_mate);

		listView.setAdapter(adapter);
		Home.setScrollEvent(listView);
	}

	@Override
	public void download()
	{
		list.add(new Content("蔡建伟", "材料", "616303", "18271396303"));
		list.add(new Content("陈丰", "光电", "61369", "15271810369"));
		list.add(new Content("陈佳伟", "机械制造", "", "18688748544"));
		list.add(new Content("陈章", "道桥", "620700", "15245784112"));
		list.add(new Content("陈媛筠", "财政学", "", "15271487969"));
		list.add(new Content("初婷婷", "光电", "", "15575450369"));
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
		Content c = (Content) contentList.get(position);
		LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.list_mate, null);
		((TextView) linearLayout.findViewById(R.id.mate_name)).setText(c.name);
		((TextView) linearLayout.findViewById(R.id.mate_major))
				.setText(c.major);
		((TextView) linearLayout.findViewById(R.id.mate_cornet))
				.setText(c.cornet);
		((TextView) linearLayout.findViewById(R.id.mate_phone))
				.setText(c.phone);

		adapter.setLinearLayout(linearLayout);
	}
	@Override
	public void removeObj()
	{

	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub

	}

	class Content
	{
		String name;
		String major;
		String cornet;
		String phone;

		public Content(String name, String major, String cornet, String phone)
		{
			this.name = name;
			this.major = major;
			this.cornet = cornet;
			this.phone = phone;
		}
	}

}
