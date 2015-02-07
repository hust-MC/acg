package com.cf.acg.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.cf.acg.R;
import com.cf.acg.detail.ActivityDetail.Content;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
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
				sex = reader.nextBoolean();
			}
			else if (field.equals("school"))
			{
				school = reader.nextString();
			}
			else if (field.equals("mobile"))
			{
				mobile = reader.nextString();
			}
			else if (field.equals("mobile_type"))
			{
				mobile_type = reader.nextString();
			}
			else if (field.equals("mobile_short"))
			{
				mobile_short = reader.nextString();
			}
			else if (field.equals("email"))
			{
				email = reader.nextString();
			}
			else if (field.equals("qqnum"))
			{
				qqnum = reader.nextString();
			}
			else if (field.equals("address"))
			{
				address = reader.nextString();
			}
			else if (field.equals("photo"))
			{
				photo = reader.nextString();
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
			}
			else
			{
				reader.skipValue();
			}
			reader.endObject();
		}
		return new Content(uid, name, type, sex, school, mobile, mobile_type,
				mobile_short, email, qqnum, address, photo, introduce,
				register_time, lastlogin_time);
	}
	@Override
	protected void setData()
	{

	}

	@Override
	public void download()
	{
		final String urlAddress = "http://acg.husteye.cn/api/matedetail?access_token=9926841641313132&mate_id="
				+ id;
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
		setContentView(R.layout.mate_detail);

		init_variable();
		init_widget();

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
