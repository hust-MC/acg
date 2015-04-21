package com.cf.acg.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.Util.JsonResolve;
import com.cf.acg.detail.DetailAbstract;
import com.cf.acg.detail.MateDetail;
import com.cf.acg.thread.DownloadInterface;

import android.content.Intent;
import android.os.Bundle;
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

public class FragmentMate extends FragmentAbstract implements DownloadInterface
{
	private ListView listView;

	public static File file = new File(fileDir, "/mate.txt");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		jsonResolve = new JsonResolve(this);
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_mate, null);
	}

	public void init_widget()
	{
		listView = (ListView) activity.findViewById(R.id.list_mate);

		listView.setAdapter(adapter);
		Home.setScrollEvent(listView);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent(activity, MateDetail.class);
				intent.putExtra("id", ((Content) list.get(position)).id);
				startActivityForResult(intent, DetailAbstract.REQUEST_CODE);
			}
		});
	}

	/*
	 * 解析Json数据的回调函数
	 */
	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		String name = null;
		String cornet = null;
		String phone = null;
		String id = null;

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
			else if (field.equals("mobile_short"))
			{
				cornet = reader.nextString();
			}
			else if (field.equals("mobile"))
			{
				phone = reader.nextString();
			}
			else
			{
				reader.skipValue();
			}
		}
		reader.endObject();

		return new Content(id, name, null, cornet, phone);

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
		fType = fMate;

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List<Object> contentList, View convertView, int position)
	{
		Content c = (Content) contentList.get(position);
		LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.list_mate, null);
		((TextView) linearLayout.findViewById(R.id.mate_name)).setText(c.name);
		((TextView) linearLayout.findViewById(R.id.mate_major))
				.setText(c.major);
		((TextView) linearLayout.findViewById(R.id.mate_cornet))
				.setText(c.cornet);
		((TextView) linearLayout.findViewById(R.id.mate_phone))
				.setText(c.phone);

		adapter.setLinearLayout(linearLayout);
	}
	@Override
	public void removeObj()
	{

	}

	@Override
	public void clear()
	{

	}

	class Content
	{
		String id;
		String name;
		String major;
		String cornet;
		String phone;

		public Content(String id, String name, String major, String cornet,
				String phone)
		{
			this.id = id;
			this.name = name;
			this.major = major;
			this.cornet = cornet;
			this.phone = phone;
		}
	}
}
