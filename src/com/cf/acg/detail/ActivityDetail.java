package com.cf.acg.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.cf.acg.MainActivity;
import com.cf.acg.R;
import com.cf.acg.fragment.FragmentAbstract;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.JsonReader;
import android.view.Menu;
import android.widget.TextView;

public class ActivityDetail extends Activity
{
	private String id;
	private Content content;

	private TextView tv_workTime, tv_time, tv_venue, tv_status, tv_name,
			tv_remark;

	public static File detailFileDir = new File(
			FragmentAbstract.fileDir.getPath() + "/detail");
	private File file = new File(detailFileDir, "activity.txt");

	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			setData();
		}
	};

	private void init_widget()
	{
		tv_workTime = (TextView) findViewById(R.id.act_work_time);
		tv_time = (TextView) findViewById(R.id.act_time);
		tv_name = (TextView) findViewById(R.id.act_name);
		tv_remark = (TextView) findViewById(R.id.act_remark);
		tv_status = (TextView) findViewById(R.id.act_status);
		tv_venue = (TextView) findViewById(R.id.act_venue);
	}

	private void downloadDetail() throws IOException
	{
		final String urlAddress = "http://acg.husteye.cn/api/activitydetail?access_token=9926841641313132&activity_id="
				+ id;

		if (!detailFileDir.exists())
		{
			detailFileDir.mkdirs();
			file.createNewFile();
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				HttpThread.httpConnect(urlAddress, file);
				try
				{
					resolveDetail();
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
	public Content readContent(JsonReader reader) throws IOException
	{
		String title = null; 				// 活动标题
		String work_start_time = null; 		// 值班开始时间
		String start_time = null;			// 活动开始时间
		String remark = null;				// 活动备注
		int venue = 0;						// 活动场地
		int status = 0;						// 活动状态

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();
			if (field.equals("start_time"))
			{
				start_time = reader.nextString();
			}
			else if (field.equals("title"))
			{
				title = reader.nextString();
			}
			else if (field.equals("work_start_time"))
			{
				work_start_time = reader.nextString();
			}
			else if (field.equals("venue"))
			{
				venue = reader.nextInt();
			}
			else if (field.equals("status"))
			{
				status = reader.nextInt();
			}
			else if (field.equals("remark"))
			{
				remark = reader.nextString();
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Content(title, work_start_time, start_time, remark, venue,
				status);
	}

	public Content readJsonStream(InputStream in) throws IOException
	{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try
		{
			return readContent(reader);
		}

		finally
		{
			reader.close();
		}
	}

	// public List readContentArray(JsonReader reader) throws IOException
	// {
	// List<Object> Contents = new ArrayList<Object>();
	//
	// reader.beginArray();
	// while (reader.hasNext())
	// {
	// Contents.add(readContent(reader));
	// }
	// reader.endArray();
	// return Contents;
	// }
	private void resolveDetail() throws FileNotFoundException, IOException
	{
		content = readJsonStream(new FileInputStream(file));
	}

	private void setData()
	{
		tv_name.setText(content.title);
		tv_remark.setText(content.remark);
		tv_status.setText(MainActivity.activityStatusName[content.status]);
		tv_time.setText("活动时间：" + content.start_time);
		tv_workTime.setText("值班时间：" + content.work_start_time);
		tv_venue.setText(MainActivity.venueName[content.venue]);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);

		init_widget();

		id = getIntent().getExtras().getString("id");

		try
		{
			downloadDetail();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_detail, menu);
		return true;
	}

	class Content
	{
		public Content(String title, String work_start_time, String start_time,
				String remark, int venue, int status)
		{
			this.title = title;
			this.work_start_time = work_start_time;
			this.start_time = start_time;
			this.remark = remark;
			this.venue = venue;
			this.status = status;
		}

		String title; 				// 活动标题
		String work_start_time; 	// 值班开始时间
		String start_time;			// 活动开始时间
		String remark;				// 活动备注
		int venue;				// 活动场地
		int status;				// 活动状态
	}
}
