package com.cf.acg.fragment;

import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;
import com.cf.acg.adapter.ContentAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentArticle extends Fragment implements ContentInterface
{
	private Activity activity;
	private ListView listView;

	private ContentAdapter adaper = new ContentAdapter();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_article, null);
	}

	private void init_widget()
	{
		/*
		 * 初始化变量
		 */
		listView = (ListView) activity.findViewById(R.id.list_article);
		listView.setAdapter(adaper);

		Home.setScrollEvent(listView);
	}

	private void getData()
	{
		adaper.addContent(this, new Content("新闻通知", "音控管理系统新增统计功能"));
		adaper.addContent(this, new Content("新闻通知", "关于提醒短信的说明"));
		adaper.addContent(this, new Content("新闻通知", "新学期开始啦（2014年第一学期）"));
		adaper.addContent(this, new Content("新闻通知", "音控组管理系统使用说明"));
		adaper.addContent(this, new Content("会议记录", "音控组例会20140608"));
		adaper.addContent(this, new Content("技术文档", "2014招新培训资料"));
		adaper.addContent(this, new Content("技术文档", "设备说明书文档"));
		adaper.addContent(this, new Content("技术文档", "话筒使用八忌"));
		adaper.addContent(this, new Content("规章制度", "华中科技大学音控组管理细则"));
		adaper.addContent(this, new Content("技术文档", "本系统采用的技术"));

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();

		getData();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List<Object> contentList, int position)
	{

		LinearLayout linearLayout = (LinearLayout) activity.getLayoutInflater()
				.inflate(R.layout.list_article, null);

		Content c = (Content) contentList.get(position);

		((TextView) linearLayout.findViewById(R.id.article_category))
				.setText(c.category);
		((TextView) linearLayout.findViewById(R.id.article_title))
				.setText(c.title);
		
		adaper.setLinearLayout(linearLayout);

	}

	@Override
	public void removeObj()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub

	}

	class Content
	{
		String category;
		String title;

		public Content(String category, String title)
		{
			this.category = "[" + category + "]";
			this.title = title;
		}
	}
}
