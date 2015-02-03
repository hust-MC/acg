package com.cf.acg.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.cf.acg.Home;
import com.cf.acg.R;

import android.os.Bundle;
import android.os.Environment;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentActivity extends FragmentAbstract
{
	List<Content> listContent;
	private ListView listView;
	FragmentActivity fragmentActivity = this;

	static File file = new File(fileDir, "/activity.txt");

	private String[] venueName =
	{ "未知", "305", "513", "东四" };
	private String[] activityStatusName =
	{ "未知", "排班中", "正在进行", "已结束", "已取消" };
	private String[] weekNum =
	{ "日", "一", "二", "三", "四", "五", "六" };

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
	public Content readContent(JsonReader reader) throws IOException
	{
		String id = null;
		String title = null;
		String start_time = null;
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
				start_time = reader.nextString();
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
		Log.d("MC", "f_a");
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

		jsonResolve = new JsonResolve(this);
		
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List<Object> contentList, int position)
	{
		Content c = (Content) contentList.get(position);
		LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.list_activity, null);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis((long) Integer.parseInt(c.start_time) * 1000);
		calendar.roll(Calendar.HOUR_OF_DAY, 8);

		SimpleDateFormat sdf_date = new SimpleDateFormat("MM-dd");
		SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");

		((TextView) linearLayout.findViewById(R.id.activity_event))
				.setText(c.title);

		((TextView) linearLayout.findViewById(R.id.activity_date))
				.setText(sdf_date.format(calendar.getTime()));
		((TextView) linearLayout.findViewById(R.id.activity_time))
				.setText(sdf_time.format(calendar.getTime()) + "");
		((TextView) linearLayout.findViewById(R.id.activity_week)).setText("星期"
				+ weekNum[calendar.get(Calendar.DAY_OF_WEEK) - 1]);

		((TextView) linearLayout.findViewById(R.id.activity_place))
				.setText(venueName[c.venue]);

		TextView textState = ((TextView) linearLayout
				.findViewById(R.id.activity_state));
		textState.setText(activityStatusName[c.status]);
		// textState
		// .setBackgroundResource(c.state.equals("正在进行") ?
		// R.drawable.activity_state_doing_back
		// : R.drawable.activity_state_todo_back);

		adapter.setLinearLayout(linearLayout);
	}

	@Override
	public void removeObj()
	{
	}

	@Override
	public void clear()
	{
	}

	static class Content
	{
		String id;
		String title;
		String work_start_time;
		String start_time;
		String end_time;
		int venue;
		int status;
		String type;
		Duties duties;

		class Duties
		{
			String uid;
			String name;
			String status;
		}

		public Content(String id, String title, String start_time, int venue,
				int status)
		{
			super();
			this.id = id;
			this.title = title;
			this.start_time = start_time;
			this.venue = venue;
			this.status = status;
		}
	}

}
