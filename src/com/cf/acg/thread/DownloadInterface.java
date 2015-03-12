package com.cf.acg.thread;

public interface DownloadInterface
{
	void download();
	
	
	/*
	 * download异常则说明网络故障，则调用此函数
	 * 
	 *  共有3处调用此函数：
	 * 1、AcgActivity类
	 * 2、FragmentAbstract类
	 * 3、DownloadApk
	 */
	void noNet();
}
