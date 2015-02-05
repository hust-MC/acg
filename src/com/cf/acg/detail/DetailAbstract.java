package com.cf.acg.detail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.cf.acg.fragment.FragmentAbstract;

import android.app.Activity;
import android.util.JsonReader;

abstract public class DetailAbstract extends Activity
{
	protected String id;			// 详细文件的ID
	protected File file;			// Json 文件

	public abstract Object readContent(JsonReader reader) throws IOException;	// 解析Json文件中的
																				// 对象

	public static File detailFileDir = new File(						// Json文件存储目录
			FragmentAbstract.fileDir.getPath() + "/detail");

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
