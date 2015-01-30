package com.cf.acg.fragment;

import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.adapter.ContentAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentRecord extends FragmentAbstract
{
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_record, null);
	}

	public void init_widget()
	{
		listView = (ListView) activity.findViewById(R.id.list_record);

		listView.setAdapter(adapter);
		Home.setScrollEvent(listView);
	}

	@Override
	public void download()
	{
		list.add(new Content("2015年01月23日  星期五  13:00-18:00", "513",
				"辅导员职业能力大赛"));
		list.add(new Content("2015年01月23日  星期五  09:00-12:30", "513",
				"辅导员职业能力大赛"));
		list.add(new Content("2015年01月22日  星期四  13:00-18:00", "513", "爱的力量"));
		list.add(new Content("2015年01月21日  星期三  18:00-22:00", "513", "爱的力量"));
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
				.inflate(R.layout.list_record, null);

		((TextView) linearLayout.findViewById(R.id.record_time))
				.setText(c.time);
		((TextView) linearLayout.findViewById(R.id.record_place))
				.setText(c.place);
		((TextView) linearLayout.findViewById(R.id.record_event))
				.setText(c.event);

		adapter.setLinearLayout(linearLayout);
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

	class Content
	{
		String time;
		String place;
		String event;

		public Content(String time, String place, String event)
		{
			super();
			this.time = time;
			this.place = "地点：" + place;
			this.event = "活动：" + event;
		}

	}

}
