package com.cf.acg.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.detail.DetailAbstract;
import com.cf.acg.detail.ActivityDetail.Content;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.os.Bundle;
import android.text.Html;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ArticleDetail extends DetailAbstract implements DownloadInterface
{
	private TextView tv_tile, tv_details;
	private Content content;

	private void init_widget()
	{
		tv_details = (TextView) findViewById(R.id.art_details);
		tv_tile = (TextView) findViewById(R.id.art_title);
	}
	private void init_variable()
	{
		id = getIntent().getExtras().getString("id");
		file = new File(detailFileDir, "/article.txt");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_article);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		init_variable();
		init_widget();

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
		getMenuInflater().inflate(R.menu.article_detail, menu);
		return true;
	}

	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		String title = null;				// 文章标题
		String details = null;				// 文章内容

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();
			if (field.equals("title"))
			{
				title = reader.nextString();
			}
			else if (field.equals("content"))
			{
				details = reader.nextString();
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Content(title, details);
	}

	@Override
	protected void setData()
	{
		tv_tile.setText(content.title);
		tv_details.setText(Html.fromHtml(content.details));
	}

	@Override
	public void download()
	{
		final String urlAddress = "http://acg.husteye.cn/api/articledetail?access_token="
				+ UserInfo.getToken() + "&article_id=" + id;
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

	class Content
	{
		String title;				// 文章标题
		String details;				// 文章内容

		public Content(String title, String details)
		{
			this.title = title;
			this.details = details;
		}
	}

}
