package com.cf.acg.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeFormat
{
	int time;
	Calendar calendar = Calendar.getInstance();

	public TimeFormat(int time)
	{
		this.time = time;

		calendar.setTimeInMillis(((long) time) * 1000);
		// calendar.roll(Calendar.HOUR_OF_DAY, 8);
	}

	public String format(String format)
	{

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}
	public int getField(int field)
	{
		return calendar.get(field);
	}
}
