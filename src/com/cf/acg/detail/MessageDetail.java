package com.cf.acg.detail;

import java.io.IOException;

import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.widget.TextView;

public class MessageDetail extends DetailAbstract implements DownloadInterface
{
	TextView tv_title, tv_detail;

	@Override
	public void download()
	{
		String urlAddress = "http://acg.husteye.cn/api/mymessagedetail?access_token="
				+ UserInfo.getToken() + "&message_id=" + messageID;
	}
	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		return null;
	}

	@Override
	protected void setData()
	{
		// TODO Auto-generated method stub

	}
	public void init_widget()
	{
		tv_title = (TextView) findViewById(R.id.message_title);
		tv_detail = (TextView) findViewById(R.id.message_details);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_message);

		init_widget();

		loadingProcess = new LoadingProcess(this);
		loadingProcess.startLoading();
		new HttpThread(this, acgHandler).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_detail, menu);
		return true;
	}

}
