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

import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		Duties duties = null;

		reader.beginObject();
		while (reader.hasNext())
		{
			switch (reader.nextName())
			{
			case "start_time":
				start_time = reader.nextInt();
				break;
			case "title":
				title = reader.nextString();
				break;
			case "work_start_time":
				work_start_time = reader.nextInt();
				break;
			case "venue":
				venue = reader.nextInt();
				break;
			case "status":
				status = reader.nextInt();
				break;
			case "remark":
				remark = reader.nextString();
				break;
			case "duties":
				duties = new Duties();
				reader.beginArray();					// 开始读取duties数组
				while (reader.hasNext())
				{
					reader.beginObject();				// 读取第一个音控员对象
					while (reader.hasNext())
					{
						switch (reader.nextName())
						{
						case "id":
							duties.id = reader.nextString();
							break;
						case "uid":
							duties.uid = reader.nextString();
							break;
						case "name":
							duties.name = reader.nextString();
							break;
						case "mobile":
							duties.name = reader.nextString();
							break;
						case "short":
							duties.mobile_short = reader.nextString();
							break;
						case "status":
							duties.status = reader.nextString();
							break;
						default:
							reader.skipValue();
							break;
						}
					}
					reader.endObject();
				}
				reader.endArray();
				break;
			default:
				reader.skipValue();
				break;
			}
		}
		reader.endObject();

		return new Content(title, remark, work_start_time, start_time, venue,
				status, duties);
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

		/**
		 * 设置音控员栏目
		 */
		if (content.duties.id != null)
		{
			LinearLayout layout = (LinearLayout) findViewById(R.id.staff);
			LinearLayout staffLayout = (LinearLayout) getLayoutInflater()
					.inflate(R.layout.acg_staff, null);

			// layout.removeAllViews();
			// ((TextView) staffLayout.findViewById(R.id.staff_name))
			// .setText(content.duties.name);
			// ((TextView) staffLayout.findViewById(R.id.staff_phone))
			// .setText(content.duties.mobile);
			// ((TextView) staffLayout.findViewById(R.id.staff_cornet))
			// .setText(content.duties.mobile_short);
			// ((TextView) staffLayout.findViewById(R.id.staff_state))
			// .setText(content.duties.status);

			layout.addView(staffLayout);
		}
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
		String title; 				// 活动标题
		String remark;				// 活动备注
		int work_start_time; 		// 值班开始时间
		int start_time;				// 活动开始时间
		int venue;					// 活动场地
		int status;					// 活动状态
		Duties duties;				// 组员职责

		public Content(String title, String remark, int work_start_time,
				int start_time, int venue, int status, Duties duties)
		{
			this.title = title;
			this.remark = remark;
			this.work_start_time = work_start_time;
			this.start_time = start_time;
			this.venue = venue;
			this.status = status;
			this.duties = duties;
		}
	}

	class Duties
	{
		String id; 			// 任务ID
		String uid; 		// 用户学号
		String name;		// 用户姓名
		String mobile; 		// 手机号
		String mobile_type; // 手机号类型
		String mobile_short;// 手机短号
		String status;// 任务状态
	}
}
