package com.cf.acg.fragment;

import java.io.IOException;
import java.util.List;

import com.cf.acg.R;
import com.cf.acg.thread.DownloadInterface;

import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMine extends FragmentAbstract implements DownloadInterface
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_mine, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void addObj(List<Object> contentList, int position)
	{
		// TODO Auto-generated method stub

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

	@Override
	public void download()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object readContent(JsonReader reader) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}
}
