package com.cf.acg.detail;

import java.io.File;

import com.cf.acg.R;
import com.cf.acg.detail.DetailAbstract;
import com.cf.acg.thread.DownloadInterface;

import android.os.Bundle;
import android.app.Activity;
import android.util.JsonReader;
import android.view.Menu;

public class ArticleDetail extends DetailAbstract implements DownloadInterface
{

	private void init_widget()
	{

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.article_detail, menu);
		return true;
	}

	@Override
	public void download()
	{

	}

	class Content
	{
		String title;
		String details;

		public Content(String title, String details)
		{
			this.title = title;
			this.details = details;
		}
	}

	@Override
	public Object readContent(JsonReader reader)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
