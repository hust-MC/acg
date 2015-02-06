package com.cf.acg.detail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.cf.acg.detail.ActivityDetail.Content;
import com.cf.acg.fragment.FragmentAbstract;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;

abstract public class DetailAbstract extends Activity
{
	protected String id;			// 详细文件的ID
	protected File file;			// Json 文件
	
	public abstract Object readContent(JsonReader reader) throws IOException;	// 解析Json文件中的对象
	protected abstract void setData();											//用于把下载到的数据显示到界面上

	public static File detailFileDir = new File(						// Json文件存储目录
			FragmentAbstract.fileDir.getPath() + "/detail");

	/*
	 * 用于处理Json文件下载之后的处理事件
	 */
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			setData();
		}
	};

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
}
