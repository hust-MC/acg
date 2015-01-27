package com.cf.acg.fragment;

import java.util.List;

import com.cf.acg.R;
import com.cf.acg.adapter.ContentAdaper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentActivity extends Fragment implements ContentInterface
{
	private Activity activity;
	private ListView listView;
	private TextView textDate, textTime, textWeek, textEvent;

	private ContentAdaper adapter = new ContentAdaper();

	Content[] contents = new Content[6];
	Content content;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();

		return inflater.inflate(R.layout.fragment_activity, null);
	}

	private void init_widget()
	{
		/*
		 * 初始化控件
		 */
		listView = (ListView) activity.findViewById(R.id.list_activity);

		listView.setAdapter(adapter);
	}

	private void getDate()
	{
		content = new Content("01-27", "06:00", "星期二", "场地维护", "513", "正在进行");
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();

		getDate();

//		adapter.addContent(this, content);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List contentList, int position)
	{
		Content c = (Content) contentList.get(position);
		LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.activity_list, null);

		((TextView) linearLayout.findViewById(R.id.activity_date))
				.setText(c.date);
		((TextView) linearLayout.findViewById(R.id.activity_event))
				.setText(c.event);
		((TextView) linearLayout.findViewById(R.id.activity_time))
				.setText(c.time);
		((TextView) linearLayout.findViewById(R.id.activity_week))
				.setText(c.week);
		
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
		String date;
		String time;
		String week;
		String event;
		String place;
		String state;

		public Content(String date, String time, String week, String event,
				String place, String state)
		{
			this.date = date;
			this.time = time;
			this.week = week;
			this.event = event;
			this.place = place;
			this.state = state;
		}
	}

}
