package com.cf.acg.Util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

public class LoadingProcess
{
	Context context;
	Dialog dialog;

	public LoadingProcess(Context context)
	{
		this.context = context;
	}
	public void startLoading()
	{
		dialog = ProgressDialog.show(context, null, "正在下载数据，请稍候...", true,
				false);    // 进程弹窗
	}
	
	public void startLoading(String message)
	{
		dialog = ProgressDialog.show(context, null, message, true,
				false);    // 进程弹窗
	}
	
	public void dismissDialog()
	{
		dialog.dismiss();
	}
}
