package com.cf.acg.fragment;

import com.cf.acg.R;
import com.cf.acg.adapter.ContentAdaper;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentActivity extends Fragment implements ContentInterface
{
	private Activity activity;
	private ListView listView;
	private ContentAdaper adaper = new ContentAdaper();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_activity, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_widget();
		super.onActivityCreated(savedInstanceState);
	}

	private void init_widget()
	{
		listView = (ListView) activity.findViewById(R.id.list_activity);
	}

	@Override
	public void addObj()
	{
		//  Auto-generated method stub

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
		
	}
}
