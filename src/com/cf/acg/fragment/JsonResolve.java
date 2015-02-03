package com.cf.acg.fragment;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cf.acg.fragment.FragmentAbstract;

import android.util.JsonReader;
import android.util.Log;

public class JsonResolve
{
	FragmentAbstract fragmentAbstract;

	public JsonResolve(FragmentAbstract fragmentAbstract)
	{
		this.fragmentAbstract = fragmentAbstract;
	}

	public List<Object> readJsonStream(InputStream in) throws IOException
	{
		Log.d("MC","jsonResolve___ readJsonStream");
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try
		{
			return readContentArray(reader);
		}

		finally
		{
			reader.close();
		}
	}

	public List readContentArray(JsonReader reader) throws IOException
	{
		List<Object> Contents = new ArrayList<Object>();

		reader.beginArray();
		while (reader.hasNext())
		{
			Contents.add(fragmentAbstract.readContent(reader));
		}
		reader.endArray();
		return Contents;
	}
}
