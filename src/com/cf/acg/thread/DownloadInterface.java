package com.cf.acg.thread;

/**
 * 接口名：DownloadInterface 功能：用于配合httpThread的下载
 * 用法说明：当主线程调用httpThread.start()启动线程后，
 * 后回调自己的download方法，完成下载。通常在这里面会调用readContent回调函数用于解析Json数据
 * 
 * @author MC
 */
public interface DownloadInterface
{
	/**
	 * DownloadInterface接口函数，用于回调DownloadInterface接口对象的下载函数，
	 * 在自身的download函数中完成下载任务
	 * @author MC
	 */
	void download();
}
