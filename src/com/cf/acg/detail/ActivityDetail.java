package com.cf.acg.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import com.cf.acg.MainActivity;
import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.Util.TimeFormat;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.app.Dialog;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDetail extends DetailAbstract implements DownloadInterface
{
	private Content content;

	private TextView tv_workTime, tv_time, tv_venue, tv_status, tv_name,
			tv_remark;

	private void init_widget()
	{
		tv_workTime = (TextView) findViewById(R.id.act_work_time);
		tv_time = (TextView) findViewById(R.id.act_time);
		tv_name = (TextView) findViewById(R.id.act_name);
		tv_remark = (TextView) findViewById(R.id.act_remark);
		tv_status = (TextView) findViewById(R.id.act_status);
		tv_venue = (TextView) findViewById(R.id.act_venue);
	}

	@Override
	public void download()
	{
		final String urlAddress = "http://acg.husteye.cn/api/activitydetail?access_token="
				+ UserInfo.getToken() + "&activity_id=" + id;
		HttpThread.httpConnect(urlAddress, file);
		try
		{
			content = (Content) readJsonStream(new FileInputStream(file));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	@Override
	public Content readContent(JsonReader reader) throws IOException
	{
		String title = null; 				// 活动标题
		int work_start_time = 0; 			// 值班开始时间
		int start_time = 0;					// 活动开始时间
		String remark = null;				// 活动备注
		int venue = 0;						// 活动场地
		int status = 0;						// 活动状态

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();
			if (field.equals("start_time"))
			{
				start_time = reader.nextInt();
			}
			else if (field.equals("title"))
			{
				title = reader.nextString();
			}
			else if (field.equals("work_start_time"))
			{
				work_start_time = reader.nextInt();
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

	@Override
	protected void setData()
	{
		TimeFormat tf_startTime = new TimeFormat(content.start_time);
		TimeFormat tf_workTime = new TimeFormat(content.work_start_time);

		tv_name.setText(content.title);
		tv_remark.setText(content.remark);
		tv_status.setText(MainActivity.activityStatusName[content.status]);
		tv_time.setText("活动时间："
				+ tf_startTime.format("yyyy年MM月dd日")
				+ " 星期"
				+ MainActivity.weekNum[tf_startTime
						.getField(Calendar.DAY_OF_WEEK) - 1]
				+ tf_startTime.format(" HH:mm"));
		tv_workTime.setText("值班时间："
				+ tf_workTime.format("yyyy年MM月dd日")
				+ " 星期"
				+ MainActivity.weekNum[tf_workTime
						.getField(Calendar.DAY_OF_WEEK) - 1]
				+ tf_workTime.format(" HH:mm"));
		tv_venue.setText(MainActivity.venueName[content.venue]);
	}

	private void init_variable()
	{
		id = getIntent().getExtras().getString("id");
		file = new File(detailFileDir, "/activity.txt");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		init_widget();

		init_variable();

		/*
		 * 启动下载线程并弹出下载框。 下载结束后进入父类（Detail Abstract）acgHandler函数
		 */
		loadingProcess = new LoadingProcess(this);
		loadingProcess.startLoading();
		new HttpThread(this, acgHandler).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_detail, menu);
		return true;
	}

	class Content
	{
		public Content(String title, int work_start_time, int start_time,
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
		String remark;				// 活动备注
		int work_start_time; 		// 值班开始时间
		int start_time;				// 活动开始时间
		int venue;					// 活动场地
		int status;					// 活动状态
	}


}
