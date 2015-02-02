package com.cf.acg.fragment;

import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentActivity extends FragmentAbstract
{
	private ListView listView;
	FragmentActivity fragmentActivity = this;

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

		Home.setScrollEvent(listView);				// 设置滑动监听事件
	}

	@Override
	public void download()
	{
		getHttpConnection(fActivity);
		
		if(downloadException)
		{
			Toast.makeText(activity, "下载错误", Toast.LENGTH_SHORT).show();
		}
		
		list.add(new Content("01-27", "06:00", "星期二", "场地维护", "513", "正在进行"));
		list.add(new Content("01-27", "12:00", "星期二", "场地维护", "513", "正在进行"));
		list.add(new Content("01-28", "12:00", "星期三", "场地维护", "513", "正在进行"));
		list.add(new Content("02-01", "19:00", "星期日", "自助中心勤工助学部寒假培训", "305",
				"正在进行"));
		list.add(new Content("03-07", "19:00", "星期六", "瑞声科技（常州）有限公司", "305",
				"排班中"));
		list.add(new Content("03-08", "19:00", "星期日", "普联技术有限公司", "305", "排班中"));
		list.add(new Content("03-09", "09:30", "星期一", "大连大控股", "513", "排班中"));
		list.add(new Content("03-09", "19:00", "星期一", "上海汉德", "305", "排班中"));
		list.add(new Content("03-10", "14:30", "星期二", "武汉群硕软件开发有限公司", "305",
				"排班中"));
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
				.inflate(R.layout.list_activity, null);

		((TextView) linearLayout.findViewById(R.id.activity_date))
				.setText(c.date);
		((TextView) linearLayout.findViewById(R.id.activity_event))
				.setText(c.event);
		((TextView) linearLayout.findViewById(R.id.activity_time))
				.setText(c.time);
		((TextView) linearLayout.findViewById(R.id.activity_week))
				.setText(c.week);
		((TextView) linearLayout.findViewById(R.id.activity_place))
				.setText(c.place);
		TextView textState = ((TextView) linearLayout
				.findViewById(R.id.activity_state));
		textState.setText(c.state);
		textState
				.setBackgroundResource(c.state.equals("正在进行") ? R.drawable.activity_state_doing_back
						: R.drawable.activity_state_todo_back);

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

	static class Content
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
