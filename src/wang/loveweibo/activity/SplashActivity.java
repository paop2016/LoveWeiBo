package wang.loveweibo.activity;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import wang.loveweibo.BaseActivity;
import wang.loveweibo.R;
import wang.loveweibo.constants.AccessTokenKeeper;

public class SplashActivity extends BaseActivity{
	private static final long SPLASH_DUA_TIME = 500;
	private static final int WHAT_INTENT2MAIN = 0;
	private static final int WHAT_INTENT2LOGIN = 1;
	private Oauth2AccessToken accessToken;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_INTENT2MAIN:
				intent2Activity(MainActivity.class);
				finish();
				break;
			case WHAT_INTENT2LOGIN:
				intent2Activity(LoginActivity.class);
				finish();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setDuration(2000);
		aa.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				accessToken = AccessTokenKeeper.readAccessToken(SplashActivity.this);
				if(accessToken.isSessionValid()) {
					handler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUA_TIME);
				} else {
					handler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUA_TIME);
				}
			}
		});
		findViewById(R.id.iv_slogan).setAnimation(aa);
	}
}
