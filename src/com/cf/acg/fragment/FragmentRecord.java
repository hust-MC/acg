package com.cf.acg.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.MainActivity;
import com.cf.acg.R;
import com.cf.acg.RefreshLayout;
import com.cf.acg.Util.JsonResolve;
import com.cf.acg.Util.TimeFormat;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentRecord extends FragmentAbstract implements
		DownloadInterface
{
	private ListView listView;

	public static File file = new File(fileDir, "/record.txt");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		jsonResolve = new JsonResolve(this);

		fType = fRecord;
		return inflater.inflate(R.layout.fragment_record, null);
	}

	private void init_widget()
	{
		/*
		 * 初始化控件
		 */
		refreshableView = (RefreshLayout) activity
				.findViewById(R.id.fragment_record_refreshble);

		listView = (ListView) activity.findViewById(R.id.list_record);
		listView.setAdapter(adapter);
		setRefreshListener();
		setDownMoreListener();

		Home.setScrollEvent(listView);
	}
	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		int start_time = 0;
		int end_time = 0;
		String venue = null;
		String title = null;
		String ID = null;

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();
			if (field.equals("id"))
			{
				ID = reader.nextString();
			}
			else if (field.equals("title"))
			{
				title = reader.nextString();
			}
			else if (field.equals("work_start_time"))
			{
				start_time = reader.nextInt();
			}
			else if (field.equals("end_time"))
			{
				end_time = reader.nextInt();
			}
			else if (field.equals("venue"))
			{
				venue = reader.nextString();
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Content(ID, start_time, end_time, venue, title);
	}
	@Override
	public void download()
	{
		getHttpConnection(fRecord);				// 通用方法
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
			Log.d("MC", list.size() + "");
			list = jsonResolve.readJsonStream(fis);
			Log.d("MC", "list FInish");
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

		TimeFormat tf_startTime = new TimeFormat(c.start_time);
		TimeFormat tf_endTime = new TimeFormat(c.end_time);

		if (convertView == null)
		{
			convertView = (LinearLayout) activity.getLayoutInflater().inflate(
					R.layout.list_record, null);

			viewHolder = new ViewHolder();
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.record_time);
			viewHolder.place = (TextView) convertView
					.findViewById(R.id.record_place);
			viewHolder.event = (TextView) convertView
					.findViewById(R.id.record_event);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.time.setText(tf_startTime.format("yyyy年MM月dd日")
				+ "  星期"
				+ MainActivity.weekNum[tf_startTime
						.getField(Calendar.DAY_OF_WEEK) - 1] + "  "
				+ tf_startTime.format("HH:mm") + "-"
				+ tf_endTime.format("HH:mm"));
		viewHolder.place.setText("地点："
				+ MainActivity.venueName[Integer.parseInt(c.venue)]);
		viewHolder.event.setText("活动：" + c.title);

		adapter.setLinearLayout((LinearLayout) convertView);
	}
	@Override
	public void removeObj()
	{
	}

	class Content
	{
		int start_time;
		int end_time;
		String venue;
		String title;
		String ID;

		public Content(String ID, int start_time, int end_time, String venue,
				String title)
		{
			this.ID = ID;
			this.start_time = start_time;
			this.end_time = end_time;
			this.venue = venue;
			this.title = title;
		}
	}

	class ViewHolder
	{
		TextView time, place, event;
	}
}
