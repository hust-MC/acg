package com.cf.acg.detail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.cf.acg.AcgActivity;
import com.cf.acg.fragment.FragmentAbstract;

import android.os.Message;
import android.util.JsonReader;
import android.view.MenuItem;

/**
 * 类名：DetailAbstract 功能：用于完成所有细节显示有关的activity的任务（注：本类需要和Download一起使用） 用法说明：
 * 1、主线程开启httpThread线程； 2、在下载线程中会调用readConent方法，用于解析Json文件。
 * 3、结束之后自动回调本类的afterDownload方法；
 * 4、若下载不成功会返回下载失败的错误码，主线程显示无网络。若下载成功，则调用子类的setData方法 ，将readContent解析的内容设置到相应控件中
 * 
 * @author M~C
 */
abstract public class DetailAbstract extends AcgActivity
{
	public static final int REQUEST_CODE = 1;				// 用于在没网络的情况下detail结束时返回
	public static final int RESULT_CODE_NO_NET = 2;
	public static final int RESULT_CODE_NET = 3;

	protected String id;			// 详细文件的ID
	protected File file;			// Json 文件

	protected boolean closeDialogAftDownload = true;				// 是否需要关闭对话框

	public abstract Object readContent(JsonReader reader) throws IOException;	// 解析Json文件中的对象
	protected abstract void setData();											// 用于把下载到的数据显示到界面上

	public static File detailFileDir = new File(						// Json文件存储目录
			FragmentAbstract.fileDir.getPath() + "/detail");

	/*
	 * 用于处理Json文件下载之后的处理事件
	 */
	@Override
	public void afterDownload(Message msg)
	{
		if (closeDialogAftDownload)
		{
			loadingProcess.dismissDialog();
		}

		if (msg.what == 0x55)
		{
			setResult(RESULT_CODE_NO_NET);
			finish();

		}
		else
		{
			setResult(RESULT_CODE_NET);
			setData();
		}
	}

	/*
	 * 读取Json流
	 */
	public Object readJsonStream(InputStream in) throws IOException
	{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try
		{
			return readContent(reader);
		} finally
		{
			reader.close();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
