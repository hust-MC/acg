package com.cf.acg;

public class UserInfo
{
	public static String token = "8795023868593463";
	public static String uid;
	public static String name;

	public static String getToken()
	{
		return token;
	}
	public static void setToken(String token)
	{
		UserInfo.token = token;
	}

	public static String getUid()
	{
		return uid;
	}
	public static void setUid(String uid)
	{
		UserInfo.uid = uid;
	}

	public static String getName()
	{
		return name;
	}
	public static void setName(String name)
	{
		UserInfo.name = name;
	}
}
