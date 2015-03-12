package com.cf.acg;

import com.cf.acg.Util.LoadingProcess;
import com.cf.acg.thread.HttpThread;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AcgActivity extends Activity
{
	protected LoadingProcess loadingProcess = null;

	protected void afterDownload(Message msg)
	{
	}

	protected Handler acgHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (loadingProcess != null)
			{
				loadingProcess.dismissDialog();
			}

			if (msg.what == 0x55)
			{
				Toast.makeText(AcgActivity.this, "网络连接错误，请检查网络状态",
						Toast.LENGTH_LONG).show();
			}
			afterDownload(msg);
		}
	};
}
