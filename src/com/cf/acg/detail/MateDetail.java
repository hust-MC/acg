package com.cf.acg.detail;

import java.io.File;
import java.io.IOException;

import com.cf.acg.R;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setData()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void download()
	{
		// TODO Auto-generated method stub

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
	}

}
