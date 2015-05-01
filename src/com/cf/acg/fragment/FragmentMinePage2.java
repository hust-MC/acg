package com.cf.acg.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.UserInfo;
import com.cf.acg.Util.JsonResolve;
import com.cf.acg.Util.TimeFormat;
import com.cf.acg.detail.MessageDetail;
import com.cf.acg.thread.DownloadInterface;
import com.cf.acg.thread.HttpThread;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMinePage2 extends FragmentAbstract implements
		DownloadInterface
{
	private ListView listView;
	public static File file = new File(fileDir, "/mymessage.txt");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.mine_page2, null);
	}

	private void init_widget()
	{
		/*
		 * 初始化变量
		 */
		listView = (ListView) activity.findViewById(R.id.mine_message);
		listView.setAdapter(adapter);

		Home.setScrollEvent(listView);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Toast.makeText(activity, "功能待添加", Toast.LENGTH_SHORT).show();
				// Intent intent = new Intent(activity, MessageDetail.class);
				// intent.putExtra("id", ((Content) list.get(position)).id);
				// startActivity(intent);
			}
		});
	}

	@Override
	public Content readContent(JsonReader reader) throws IOException
	{
		String id = null; 				// 消息ID
		String subject = null;			// 消息主题
		int sendTime = 0;				// 发送时间
		String type = null;				// 消息类型

		reader.beginObject();
		while (reader.hasNext())
		{
			switch (reader.nextName())
			{
			case "id":
				id = reader.nextString();
				break;
			case "subject":
				subject = reader.nextString();
				break;
			case "sendtime":
				sendTime = reader.nextInt();
				break;
			case "type":
				type = reader.nextString();
				break;
			default:
				reader.skipValue();

			}
		}
		reader.endObject();
		return new Content(id, subject, sendTime, type);
	}
	@Override
	public void download()
	{
		String urlAddress = "http://acg.husteye.cn/api/mymessagelist?access_token="
				+ UserInfo.getToken();
		HttpThread.httpConnect(urlAddress, file);
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

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List<Object> contentList, View convertView, int position)
	{

		LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.list_article, null);

		Content c = (Content) contentList.get(position);

		TimeFormat tf = new TimeFormat(c.sendTime);

		((TextView) linearLayout.findViewById(R.id.article_category))
				.setText(tf.format("MM-dd"));
		((TextView) linearLayout.findViewById(R.id.article_title))
				.setText(c.subject);

		adapter.setLinearLayout(linearLayout);
	}

	@Override
	public void removeObj()
	{
	}

	class Content
	{
		String id; 				// 消息ID
		String subject;			// 消息主题
		int sendTime;		// 阅读时间
		String type;			// 消息类型

		public Content(String id, String subject, int sendTime, String type)
		{
			this.id = id;
			this.subject = subject;
			this.sendTime = sendTime;
			this.type = type;
		}
	}

}
