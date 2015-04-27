package com.cf.acg.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.MainActivity;
import com.cf.acg.R;
import com.cf.acg.RefreshLayout;
import com.cf.acg.Util.JsonResolve;
import com.cf.acg.Util.TimeFormat;
import com.cf.acg.detail.ActivityDetail;
import com.cf.acg.detail.DetailAbstract;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentActivity extends FragmentAbstract
{
	private ListView listView;

	static File file = new File(fileDir, "/activity.txt");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		jsonResolve = new JsonResolve(this);
		return inflater.inflate(R.layout.fragment_activity, null);
	}
	private void init_widget()
	{
		/*
		 * 初始化控件
		 */
		refreshableView = (RefreshLayout) activity
				.findViewById(R.id.fragment_activity_refreshble);

		listView = (ListView) activity.findViewById(R.id.list_activity);
		listView.setAdapter(adapter);

		setRefreshListener();
		setDownMoreListener();

		Home.setScrollEvent(listView); // 设置滑动监听事件
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(activity, ActivityDetail.class);
				intent.putExtra("id",
						((Content) adapter.getContentList().get(position)).id);
				startActivityForResult(intent, DetailAbstract.REQUEST_CODE);
			}
		});
	}
	/*
	 * 定义Content对象成员的接收回调函数
	 */
	@Override
	public Content readContent(JsonReader reader) throws IOException
	{
		String id = null;
		String title = null;
		int start_time = 0;
		int venue = 0;
		int status = 0;

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();
			if (field.equals("id"))
			{
				id = reader.nextString();
			}
			else if (field.equals("start_time"))
			{
				start_time = reader.nextInt();
			}
			else if (field.equals("title"))
			{
				title = reader.nextString();
			}
			else if (field.equals("venue"))
			{
				venue = reader.nextInt();
			}
			else if (field.equals("status"))
			{
				status = reader.nextInt();
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Content(id, title, start_time, venue, status);
	}

	@Override
	public void download()
	{
		getHttpConnection(fActivity);				// 通用方法
		FileInputStream fis = null;
		if (downloadException)
		{
			Toast.makeText(activity, "下载错误", Toast.LENGTH_SHORT).show();
		}

		try
		{
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			list = jsonResolve.readJsonStream(fis);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List<Object> contentList, View convertView, int position)
	{
		ViewHolder viewHolder;
		Content c = (Content) contentList.get(position);
		TimeFormat tf = new TimeFormat(c.start_time);
		if (convertView == null)
		{
			convertView = (LinearLayout) activity.getLayoutInflater().inflate(
					R.layout.list_activity, null);

			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.activity_event);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.activity_date);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.activity_time);
			viewHolder.week = (TextView) convertView
					.findViewById(R.id.activity_week);
			viewHolder.place = (TextView) convertView
					.findViewById(R.id.activity_place);
			viewHolder.state = (TextView) convertView
					.findViewById(R.id.activity_state);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title.setText(c.title);

		viewHolder.date.setText(tf.format("MM-dd"));
		viewHolder.time.setText(tf.format("HH:mm"));
		viewHolder.week.setText("星期"
				+ MainActivity.weekNum[tf.getField(Calendar.DAY_OF_WEEK) - 1]);

		viewHolder.place.setText(MainActivity.venueName[c.venue]);

		viewHolder.state.setText(MainActivity.activityStatusName[c.status]);
		// textState
		// .setBackgroundResource(c.state.equals("正在进行") ?
		// R.drawable.activity_state_doing_back
		// : R.drawable.activity_state_todo_back);

		adapter.setLinearLayout((LinearLayout) convertView);
	}
	@Override
	public void removeObj()
	{
	}

	static class Content
	{
		String id;
		String title;
		String work_start_time;
		int start_time;
		String end_time;
		int venue;
		int status;
		String type;

		public Content(String id, String title, int start_time, int venue,
				int status)
		{
			this.id = id;
			this.title = title;
			this.start_time = start_time;
			this.venue = venue;
			this.status = status;
		}
	}

	class ViewHolder
	{
		TextView title, date, time, week, place, state;
	}

}
