package com.cf.acg.fragment;

import java.io.IOException;
import java.util.List;

import cn.jpush.android.util.ac;

import com.cf.acg.R;
import com.cf.acg.detail.PersonnalDetail;
import com.cf.acg.thread.DownloadInterface;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMinePage3 extends FragmentAbstract implements
		DownloadInterface
{
	private Activity activity;
	private LinearLayout personalInfo;
	private LinearLayout setting;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.mine_page3, null);
	}

	private void init_widget()
	{
		ButtonListener buttonListener = new ButtonListener();

		personalInfo = (LinearLayout) activity.findViewById(R.id.personal_info);
		setting = (LinearLayout) activity.findViewById(R.id.settting);

		personalInfo.setOnClickListener(buttonListener);
		setting.setOnClickListener(buttonListener);

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void download()
	{
	}

	@Override
	public void addObj(List<Object> contentList, View convertView, int position)
	{
	}

	@Override
	public void removeObj()
	{
	}

	@Override
	public void clear()
	{
	}

	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		return null;
	}

	class ButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.personal_info:
				startActivity(new Intent(activity, PersonnalDetail.class));
				break;

			case R.id.settting:
				Toast.makeText(activity, "功能待添加", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}
}
