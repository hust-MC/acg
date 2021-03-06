package com.cf.acg.detail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cf.acg.MainActivity;
import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.Util.TimeFormat;
import com.cf.acg.fragment.FragmentAbstract;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Int2;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ActivityDetail extends DetailAbstract implements DownloadInterface
{
	private final String[] dutyStatus =
	{ "正在申请值班", "班长批准值班", "班长拒绝值班", "排班等待确认", "排班拒绝值班", "等待活动开始", "申请换班中",
			"换班成功", "活动取消", "活动进行中", "活动结束", "任务取消", "取消申请值班" };

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
		Content c = new Content();

		reader.beginObject();
		while (reader.hasNext())
		{
			switch (reader.nextName())
			{
			case "start_time":
				c.start_time = reader.nextInt();
				break;
			case "title":
				c.title = reader.nextString();
				break;
			case "work_start_time":
				c.work_start_time = reader.nextInt();
				break;
			case "venue":
				c.venue = reader.nextInt();
				break;
			case "status":
				c.status = reader.nextInt();
				break;
			case "remark":
				c.remark = reader.nextString();
				break;
			case "duties":
				reader.beginArray();						// 开始读取duties数组
				while (reader.hasNext())
				{
					Duties duties = new Duties();

					reader.beginObject();					// 读取第一个音控员对象
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
							duties.mobile = reader.nextString();
							break;
						case "mobile_short":
							duties.mobile_short = reader.nextString();
							break;
						case "status":
							duties.status = reader.nextInt();
							break;
						case "operations":
							reader.beginArray();
							Operations operations = new Operations();
							while (reader.hasNext())
							{
								reader.beginObject();
								while (reader.hasNext())
								{
									switch (reader.nextName())
									{
									case "name":
										operations.name = reader.nextString();
										break;
									case "title":
										operations.title = reader.nextString();
										break;
									case "color":
										operations.color = reader.nextString();
										break;
									case "content":
										operations.content = reader
												.nextString();
										break;
									case "require_input":
										operations.require = reader
												.nextBoolean();
										break;
									default:
										reader.skipValue();
										break;
									}
								}
								reader.endObject();
								duties.operationList.add(operations);
							}
							reader.endArray();
							break;
						default:
							reader.skipValue();
							break;
						}
					}
					reader.endObject();
					c.dutyList.add(duties);
				}
				reader.endArray();
				break;
			default:
				reader.skipValue();
				break;
			}
		}
		reader.endObject();
		return c;
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
		if (content.dutyList.size() > 0)
		{
			final LinearLayout layout = (LinearLayout) findViewById(R.id.staff);
			layout.removeAllViews();
			for (final Duties duties : content.dutyList)
			{
				LinearLayout staffLayout = (LinearLayout) getLayoutInflater()
						.inflate(R.layout.acg_staff, null);

				((TextView) staffLayout.findViewById(R.id.staff_name))
						.setText(duties.name);
				((TextView) staffLayout.findViewById(R.id.staff_phone))
						.setText(duties.mobile);
				((TextView) staffLayout.findViewById(R.id.staff_cornet))
						.setText(duties.mobile_short);
				((TextView) staffLayout.findViewById(R.id.staff_state))
						.setText(dutyStatus[duties.status - 1]);
				if (duties.operationList.size() > 0)
				{
					LayoutParams wrapParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					LinearLayout operationLayout = new LinearLayout(this);
					operationLayout.setLayoutParams(wrapParams);
					operationLayout.setOrientation(LinearLayout.HORIZONTAL);
					for (final Operations operations : duties.operationList)
					{
						Button button = new Button(this);
						button.setLayoutParams(new LayoutParams(
								LayoutParams.WRAP_CONTENT, 80));
						button.setText(operations.title);
						button.setGravity(Gravity.CENTER);
						button.setTextSize(15);
						button.setTextColor(android.graphics.Color.WHITE);
						button.setBackgroundResource(R.drawable.duty_opt_bt);

						button.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								LinearLayout dialogLayout = new LinearLayout(
										ActivityDetail.this);
								TextView content = null;
								EditText require = null;
								if (operations.content != null)
								{
									/*
									 * 添加content和require布局容器
									 */
									LayoutParams params = new LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT);
									dialogLayout.setLayoutParams(params);
									dialogLayout
											.setOrientation(LinearLayout.VERTICAL);
									dialogLayout.setPadding(100, 50, 100, 0);

									/*
									 * 添加content提示信息
									 */
									content = new TextView(ActivityDetail.this);

									content.setText(operations.content);
									content.setTextSize(18);
									dialogLayout.addView(content);

									if (operations.require)
									{
										/*
										 * 添加require输入框
										 */
										require = new EditText(
												ActivityDetail.this);
										dialogLayout.addView(require);
									}

								}
								new AlertDialog.Builder(ActivityDetail.this)
										.setTitle(operations.title)
										.setView(dialogLayout)
										.setNegativeButton("取消", null)
										.setPositiveButton(
												"确认",
												new AlertDialogPostiveButton(
														duties.id,
														operations.name,
														require)).show();
							}
						});
						operationLayout.addView(button);
					}
					staffLayout.addView(operationLayout);
				}
				layout.addView(staffLayout);
			}
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

	/**
	 * 任务操作对话框，确认按钮事件
	 * 
	 * @author M C
	 * 
	 */
	class AlertDialogPostiveButton implements DialogInterface.OnClickListener,
			DownloadInterface
	{

		String duty_id;
		String operation;
		EditText reason;

		public AlertDialogPostiveButton(String duty_id, String operation,
				EditText reason)
		{
			this.duty_id = duty_id;
			this.operation = operation;
			this.reason = reason;
		}

		File optFile = new File(FragmentAbstract.fileDir.getPath(), "/opt");
		Handler dutyOptHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				String tmp;
				BufferedReader fileBuffer = null;
				StringBuffer jsonStr = new StringBuffer();
				try
				{
					fileBuffer = new BufferedReader(new FileReader(optFile));
					while (((tmp = fileBuffer.readLine()) != null))
					{
						jsonStr.append(tmp);
					}
					JSONObject jsonObject = new JSONObject(jsonStr.toString());
					Toast.makeText(
							ActivityDetail.this,
							jsonObject.has("success") ? jsonObject
									.getString("message") : jsonObject
									.getString("content"), Toast.LENGTH_SHORT)
							.show();
					/*
					 * 重启当前activity
					 */
					ActivityDetail.this.finish();
					startActivity(getIntent());
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		};

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			new HttpThread(this, dutyOptHandler).start();
		}

		@Override
		public void download()
		{
			String urlAddress = "http://acg.husteye.cn/api/activityoperation?access_token="
					+ UserInfo.getToken()
					+ "&duty_id="
					+ duty_id
					+ "&operation="
					+ operation
					+ ((reason != null) ? ("&reason=" + reason.getText()
							.toString()) : "");

			HttpThread.httpConnect(urlAddress, optFile);
		}
	}

	class Content
	{
		String title; 											// 活动标题
		String remark;											// 活动备注
		int work_start_time; 									// 值班开始时间
		int start_time;											// 活动开始时间
		int venue;												// 活动场地
		int status;												// 活动状态
		List<Duties> dutyList = new ArrayList<>();				// 组员职责

	}

	class Duties
	{
		String id; 					// 任务ID
		String uid; 				// 用户学号
		String name;				// 用户姓名
		String mobile; 				// 手机号
		String mobile_type; 		// 手机号类型
		String mobile_short;		// 手机短号
		int status;					// 任务状态
		List<Operations> operationList = new ArrayList<>();		// 活动操作
	}

	class Operations
	{
		String name; 			// 操作名称
		String title;			// 操作显示名称
		String color; 			// 显示颜色
		String content;			// 提示框信息
		boolean require; 		// 是否需要填写附加信息
	}
}
