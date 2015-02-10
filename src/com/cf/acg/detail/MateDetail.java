package com.cf.acg.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.Util.TimeFormat;
import com.cf.acg.detail.ActivityDetail.Content;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MateDetail extends DetailAbstract implements DownloadInterface
{
	TextView tv_resume;
	ImageView iv_photo;
	ListView listView;

	private final int INFO_NUM = 11;

	private Content content;
	private String[] infoItem = new String[INFO_NUM];

	private void init_variable()
	{
		id = getIntent().getExtras().getString("id");
		file = new File(detailFileDir, "/mate.txt");
	}

	private void init_widget()
	{
		tv_resume = (TextView) findViewById(R.id.detail_mate_resume);
		iv_photo = (ImageView) findViewById(R.id.detail_mate_image);
		listView = (ListView) findViewById(R.id.detail_mate_listview);
	}

	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		String uid = null;				// 学号
		String name = null;				// 用户名
		String type = null;				// 用户类型
		String sex = null;			// 性别
		String school = null;			// 专业
		String mobile = null; 			// 手机号
		String mobile_type = null; 		// 手机号类型
		String mobile_short = null;		// 手机短号
		String email = null;			// 电子邮件
		String qqnum = null;			// QQ号码
		String address = null;			// 住址
		String photo = null;			// 照片列表
		String introduce = null;		// 个人简介
		int register_time = 0;			// 注册时间
		int lastlogin_time = 0;			// 上次登陆时间

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();
			Log.d("MC", field);
			if (field.equals("uid"))
			{
				uid = reader.nextString();
				infoItem[1] = uid;
			}
			else if (field.equals("name"))
			{
				name = reader.nextString();
				infoItem[0] = name;
			}
			else if (field.equals("type"))
			{
				type = reader.nextString();
			}
			else if (field.equals("sex"))
			{
				sex = reader.nextString();
				infoItem[2] = sex;
			}
			else if (field.equals("school"))
			{
				school = reader.nextString();
				infoItem[3] = school;
			}
			else if (field.equals("mobile"))
			{
				mobile = reader.nextString();
				infoItem[4] = mobile;
			}
			else if (field.equals("mobile_type"))
			{
				mobile_type = reader.nextString();
				infoItem[5] = mobile_type;
			}
			else if (field.equals("mobile_short"))
			{
				mobile_short = reader.nextString();
				infoItem[6] = mobile_short;
			}
			else if (field.equals("email"))
			{
				email = reader.nextString();
				infoItem[8] = email;
			}
			else if (field.equals("qqnum"))
			{
				qqnum = reader.nextString();
				infoItem[7] = qqnum;
			}
			else if (field.equals("address"))
			{
				address = reader.nextString();
				infoItem[9] = address;
			}
			else if (field.equals("photo"))
			{
				photo = reader.nextString();
				Log.d("MC", photo + "123");
			}
			else if (field.equals("introduce"))
			{
				introduce = reader.nextString();
			}
			else if (field.equals("register_time"))
			{
				register_time = reader.nextInt();
			}
			else if (field.equals("lastlogin_time"))
			{
				lastlogin_time = reader.nextInt();
				infoItem[10] = new TimeFormat(lastlogin_time)
						.format("yyyy-MM-dd  HH:mm:ss");
			}
			else
			{
				reader.skipValue();
			}

		}
		reader.endObject();
		return new Content(uid, name, type, sex, school, mobile, mobile_type,
				mobile_short, email, qqnum, address, photo, introduce,
				register_time, lastlogin_time);
	}
	@Override
	protected void setData()
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < INFO_NUM; i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("title",
					(getResources().getStringArray(R.array.mate_detail_left))[i]);

			map.put("content", infoItem[i]);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.list_mate_detail, new String[]
				{ "title", "content" }, new int[]
				{ R.id.list_mate_detail_left, R.id.list_mate_detail_right });
		listView.setAdapter(adapter);

		// Bitmap bitmap = null;
		// HttpThread
		// .httpConnect("http://acg.husteye.cn/" + content.photo, bitmap);
		// iv_photo.setImageBitmap(bitmap);

	}

	@Override
	public void download()
	{
		final String urlAddress = "http://acg.husteye.cn/api/memberdetail?access_token="
				+ UserInfo.getToken() + "&member_uid=" + id;
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_mate);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		init_variable();
		init_widget();

		/*
		 * 启动下载线程并弹出下载框。 下载结束后进入父类（Detail Abstract）handler函数
		 */
		loadingProcess = new LoadingProcess(this);
		loadingProcess.startLoading();
		new HttpThread(this, handler).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.mate_detail, menu);
		return true;
	}

	class Content
	{
		String uid;				// 学号
		String name;			// 用户名
		String type;			// 用户类型
		String sex;			// 性别
		String school;			// 专业
		String mobile; 			// 手机号
		String mobile_type; 	// 手机号类型
		String mobile_short;	// 手机短号
		String email;			// 电子邮件
		String qqnum;			// QQ号码
		String address;			// 住址
		String photo;			// 照片列表
		String introduce;		// 个人简介
		int register_time;		// 注册时间
		int lastlogin_time;		// 上次登陆时间

		public Content(String uid, String name, String type, String sex,
				String school, String mobile, String mobile_type,
				String mobile_short, String email, String qqnum,
				String address, String photo, String introduce,
				int register_time, int lastlogin_time)
		{
			this.uid = uid;
			this.name = name;
			this.type = type;
			this.sex = sex;
			this.school = school;
			this.mobile = mobile;
			this.mobile_type = mobile_type;
			this.mobile_short = mobile_short;
			this.email = email;
			this.qqnum = qqnum;
			this.address = address;
			this.photo = photo;
			this.introduce = introduce;
			this.register_time = register_time;
			this.lastlogin_time = lastlogin_time;
		}

	}

}
