package wang.loveweibo;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import wang.loveweibo.api.LoveWeiboApi;
import wang.loveweibo.constants.CommenConstants;
import wang.loveweibo.utils.Logger;
import wang.loveweibo.utils.ToastUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity{
	protected String TAG;
	protected SharedPreferences sp;
	protected ImageLoader imageLoader;
	protected Gson gson;
	protected LoveWeiboApi api;
	//»±…ŸBaseApplication
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TAG = this.getClass().getSimpleName();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		sp = getSharedPreferences(CommenConstants.SP_NAME, MODE_PRIVATE);
//		BaseApplication.getInstance().addActivity(this);
		
		gson = new Gson();
		api = new LoveWeiboApi(this);
		imageLoader = ImageLoader.getInstance();
	}
	protected void intent2Activity(Class<? extends Activity> tarActivity) {
		Intent intent = new Intent(this, tarActivity);
		startActivity(intent);
	}
	protected void showToast(String msg) {
		ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
	}
	protected void showLog(String msg) {
		Logger.showLog(TAG, msg);
	}
}
