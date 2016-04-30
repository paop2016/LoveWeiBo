package wang.loveweibo.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import wang.loveweibo.utils.Logger;
import wang.loveweibo.utils.ToastUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class SimpleRequestListener implements RequestListener{
	private Context context;
	private ProgressDialog progressDialog;
	public SimpleRequestListener(Context context, ProgressDialog progressDialog) {
		this.context = context;
		this.progressDialog = progressDialog;
	}
	@Override
	public void onComplete(String response) {
		onAllDone();
//		Logger.showLog("REQUEST onComplete", response);
	}

	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS) {
		onAllDone();
		Logger.showLog("REQUEST onComplete4binary", responseOS.size() + "");
	}

	@Override
	public void onIOException(IOException e) {
		onAllDone();
		ToastUtils.showToast(context, e.getMessage(), Toast.LENGTH_SHORT);
		Logger.showLog("REQUEST onIOException", e.toString());
	}

	@Override
	public void onError(WeiboException e) {
		onAllDone();
		ToastUtils.showToast(context, e.getMessage(), Toast.LENGTH_SHORT);
		Logger.showLog("REQUEST onError", e.toString());
	}
	public void onAllDone(){
		if(progressDialog != null)
			progressDialog.dismiss();
	}
}
