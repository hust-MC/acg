package com.cf.acg.fragment;

import com.cf.acg.Home;
import com.cf.acg.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentHome extends Fragment
{
	private Activity activity;
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_home, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
//		listView = (ListView)activity.findViewById(R.id.list_home);
//		
//		Home.setScrollEvent(listView);
		
		super.onActivityCreated(savedInstanceState);
	}
}
