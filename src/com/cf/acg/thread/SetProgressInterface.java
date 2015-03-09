package com.cf.acg.thread;

/*
 * 需要搭配DownloadInterface使用，用于实时更新下载进度条
 */
public interface SetProgressInterface		
{
	void setProgress(int progress);				//实时设置进度条

	void setMaxProgress(int max);				//设置进度条最大值
}
