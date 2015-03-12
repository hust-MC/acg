package com.cf.acg.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.EditText;

public class PersonnalDetail extends DetailAbstract implements
		DownloadInterface
{
	private String[] phonetypename =
	{ "未知", "移动", "联通", "电信", "其他" };
	private EditText id, name, sex, major, phone, phoneType, cornet, qq, email,
			address, bank;
	private Content content;

	public void init_widget()
	{
		id = (EditText) findViewById(R.id.person_id);
		name = (EditText) findViewById(R.id.person_name);
		sex = (EditText) findViewById(R.id.person_sex);
		major = (EditText) findViewById(R.id.person_major);
		phone = (EditText) findViewById(R.id.person_phone);
		phoneType = (EditText) findViewById(R.id.person_type);
		cornet = (EditText) findViewById(R.id.person_cornet);
		qq = (EditText) findViewById(R.id.person_qq);
		email = (EditText) findViewById(R.id.person_email);
		address = (EditText) findViewById(R.id.person_address);
		bank = (EditText) findViewById(R.id.person_bank);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_personnal);

		init_widget();

		file = new File(detailFileDir, "personDetail");
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);		// 隐藏输入法

		/*
		 * 前台弹出加载框，后台启动线程，结束后进入父类handler
		 */
		loadingProcess = new LoadingProcess(this);
		loadingProcess.startLoading();
		new HttpThread(this, handler).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.personnal_detail, menu);
		return true;
	}

	@Override
	public void download()
	{
		final String urlAddress = "http://acg.husteye.cn/api/myinfo?access_token="
				+ UserInfo.getToken();
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
	public Object readContent(JsonReader reader) throws IOException
	{
		String id = null, name = null, sex = null, major = null, phone = null, cornet = null, qq = null, email = null, address = null, bank = null;
		int phoneType = 0;

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();
			if (field.equals("uid"))
			{
				id = reader.nextString();
			}
			else if (field.equals("name"))
			{
				name = reader.nextString();
			}
			else if (field.equals("sex"))
			{
				sex = reader.nextString();
			}
			else if (field.equals("school"))
			{
				major = reader.nextString();
			}
			else if (field.equals("mobile"))
			{
				phone = reader.nextString();
			}
			else if (field.equals("mobile_type"))
			{
				phoneType = reader.nextInt();
			}
			else if (field.equals("mobile_short"))
			{
				cornet = reader.nextString();
			}
			else if (field.equals("email"))
			{
				email = reader.nextString();
			}
			else if (field.equals("qqnum"))
			{
				qq = reader.nextString();
			}
			else if (field.equals("address"))
			{
				address = reader.nextString();
			}
			else if (field.equals("credit_card"))
			{
				bank = reader.nextString();
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Content(id, name, sex, major, phone, phoneType, cornet, qq,
				email, address, bank);
	}
	@Override
	protected void setData()
	{
		id.setText(content.id);
		name.setText(content.name);
		sex.setText(content.sex);
		major.setText(content.major);
		phone.setText(content.phone);
		phoneType.setText(phonetypename[content.phoneType]);
		cornet.setText(content.cornet);
		qq.setText(content.qq);
		email.setText(content.email);
		address.setText(content.address);
		bank.setText(content.bank);
	}

	class Content
	{
		String id, name, sex, major, phone, cornet, qq, email, address, bank;
		int phoneType;

		public Content(String id, String name, String sex, String major,
				String phone, int phoneType, String cornet, String qq,
				String email, String address, String bank)
		{
			this.id = id;
			this.name = name;
			this.sex = sex;
			this.major = major;
			this.phone = phone;
			this.phoneType = phoneType;
			this.cornet = cornet;
			this.qq = qq;
			this.email = email;
			this.address = address;
			this.bank = bank;
		}
	}
}
