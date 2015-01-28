package com.cf.acg.fragment;

import java.util.ArrayList;
import java.util.List;

import com.cf.acg.Home;
import com.cf.acg.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListView;

public class FragmentHome extends Fragment
{
	private Activity activity;
	private ListView listView;

	private ViewPager viewPager;

	private List<String> titleList = new ArrayList<String>();

	private List<View> viewList = new ArrayList<View>();
	private View view1, view2, view3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		return inflater.inflate(R.layout.fragment_home, null);
	}

	private void init_pages()
	{
		titleList.add("123");
		titleList.add("234");
		titleList.add("345");

		LayoutInflater lf = activity.getLayoutInflater();
		view1 = lf.inflate(R.layout.home_pager1, null);
		view2 = lf.inflate(R.layout.home_pager2, null);
		view3 = lf.inflate(R.layout.home_pager3, null);

		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
	}

	private void init_widget()
	{
		viewPager = (ViewPager) activity.findViewById(R.id.view_pager);

		viewPager.setAdapter(new MyViewPagerAdapter(viewList));
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		init_pages();
		init_widget();

		super.onActivityCreated(savedInstanceState);
	}

	class MyViewPagerAdapter extends PagerAdapter
	{
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews)
		{
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public CharSequence getPageTitle(int position)
		{

			return titleList.get(position);// 直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{	// 这个方法用来实例化页卡
			container.addView(mListViews.get(position));// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount()
		{
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;// 官方提示这样写
		}
	}
}
