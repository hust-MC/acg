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

	static File file = new File(fileDir, "activity.txt");

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

	public List<Object> readJsonStream(InputStream in) throws IOException
	{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try
		{
			return readContentArray(reader);
		}

		finally
		{
			reader.close();
		}
	}

	public List readContentArray(JsonReader reader) throws IOException
	{
		List<Content> Contents = new ArrayList<Content>();

		reader.beginArray();
		while (reader.hasNext())
		{
			Contents.add(readContent(reader));
		}
		reader.endArray();
		return Contents;
	}

	public Content readContent(JsonReader reader) throws IOException
	{
		String id = null;
		String title = null;
		String start_time = null;
		String venue = null;
		String status = null;

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
				venue = reader.nextString();
			}
			else if (field.equals("status"))
			{
				status = reader.nextString();
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
		getHttpConnection(fActivity);
		FileInputStream fis = null;
		if (downloadException)
		{
			Toast.makeText(activity, "下载错误", Toast.LENGTH_SHORT).show();
		}

		try
		{
			fis = new FileInputStream(Environment.getExternalStorageDirectory()
					+ "/ACG/activity.txt");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			list = readJsonStream(fis);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// list.add(new Content("01-27", "06:00", "星期二", "场地维护", "513",
		// "正在进行"));
		// list.add(new Content("01-27", "12:00", "星期二", "场地维护", "513",
		// "正在进行"));
		// list.add(new Content("01-28", "12:00", "星期三", "场地维护", "513",
		// "正在进行"));
		// list.add(new Content("02-01", "19:00", "星期日", "自助中心勤工助学部寒假培训", "305",
		// "正在进行"));
		// list.add(new Content("03-07", "19:00", "星期六", "瑞声科技（常州）有限公司", "305",
		// "排班中"));
		// list.add(new Content("03-08", "19:00", "星期日", "普联技术有限公司", "305",
		// "排班中"));
		// list.add(new Content("03-09", "09:30", "星期一", "大连大控股", "513",
		// "排班中"));
		// list.add(new Content("03-09", "19:00", "星期一", "上海汉德", "305", "排班中"));
		// list.add(new Content("03-10", "14:30", "星期二", "武汉群硕软件开发有限公司", "305",
		// "排班中"));
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

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis((long) Integer.parseInt(c.start_time) * 1000);
		SimpleDateFormat date = new SimpleDateFormat("MM-dd");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");
		SimpleDateFormat week = new SimpleDateFormat("EEEE");

		((TextView) linearLayout.findViewById(R.id.activity_event))
				.setText(c.title);
		((TextView) linearLayout.findViewById(R.id.activity_date))
				.setText(calendar.get(Calendar.MONTH) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + "");
		((TextView) linearLayout.findViewById(R.id.activity_time))
				.setText(calendar.get(Calendar.HOUR_OF_DAY) + 8 + "");
		((TextView) linearLayout.findViewById(R.id.activity_week))
				.setText(Calendar.DAY_OF_WEEK + "");
		((TextView) linearLayout.findViewById(R.id.activity_place))
				.setText(c.venue);
		TextView textState = ((TextView) linearLayout
				.findViewById(R.id.activity_state));
		textState.setText(c.status);
		// textState
		// .setBackgroundResource(c.state.equals("正在进行") ?
		// R.drawable.activity_state_doing_back
		// : R.drawable.activity_state_todo_back);

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
		String id;
		String title;
		String work_start_time;
		String start_time;
		String end_time;
		String venue;
		String status;
		String type;
		Duties duties;

		class Duties
		{
			String uid;
			String name;
			String status;
		}

		public Content(String id, String title, String start_time,
				String venue, String status)
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
