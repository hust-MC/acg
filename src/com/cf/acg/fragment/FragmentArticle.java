package com.cf.acg.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.RefreshLayout;
import com.cf.acg.Util.JsonResolve;
import com.cf.acg.detail.ArticleDetail;
import com.cf.acg.detail.DetailAbstract;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentArticle extends FragmentAbstract implements
		DownloadInterface
{
	private ListView listView;
	public static File file = new File(fileDir, "/article.txt");

	private String[] article_category =
	{ "未知", "新闻通知", "经验分享", "会议记录", "规章制度", "技术文档", "其他文章" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_article, null);
	}

	private void init_widget()
	{

		listView = (ListView) activity.findViewById(R.id.list_article);
		listView.setAdapter(adapter);

		Home.setScrollEvent(listView);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(activity, ArticleDetail.class);
				intent.putExtra("id", ((Content) list.get(position)).id);
				startActivityForResult(intent, DetailAbstract.REQUEST_CODE);
			}
		});
	}
	@Override
	public Content readContent(JsonReader reader) throws IOException
	{
		String title = null;
		String category = null;
		String ID = null;

		reader.beginObject();
		while (reader.hasNext())
		{
			String field = reader.nextName();

			if (field.equals("id"))
			{
				ID = reader.nextString();
			}
			else if (field.equals("title"))
			{
				title = reader.nextString();
			}
			else if (field.equals("cate_id"))
			{
				category = article_category[reader.nextInt()];
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();
		return new Content(ID, category, title);
	}
	@Override
	public void download()
	{
		getHttpConnection(fType);				// 通用方法
		FileInputStream fis = null;
		if (downloadException)
		{
			Toast.makeText(activity, "下载错误", Toast.LENGTH_SHORT).show();
		}

		try
		{
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		try
		{
			list = jsonResolve.readJsonStream(fis);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();

		jsonResolve = new JsonResolve(this);
		fType = fArticle;

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List<Object> contentList, View convertView, int position)
	{
		LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.list_article, null);

		Content c = (Content) contentList.get(position);

		((TextView) linearLayout.findViewById(R.id.article_category))
				.setText(c.category);
		((TextView) linearLayout.findViewById(R.id.article_title))
				.setText(c.title);

		adapter.setLinearLayout(linearLayout);
	}

	@Override
	public void removeObj()
	{
	}

	class Content
	{
		String id;
		String category;
		String title;

		public Content(String id, String category, String title)
		{
			this.id = id;
			this.category = "[" + category + "]";
			this.title = title;
		}
	}

}
