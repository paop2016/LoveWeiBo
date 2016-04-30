package wang.loveweibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import wang.loveweibo.BaseActivity;
import wang.loveweibo.R;
import wang.loveweibo.constants.AccessTokenKeeper;
import wang.loveweibo.constants.WeiboConstants;

public class LoginActivity extends BaseActivity{
    private WeiboAuth mAuthInfo;
    
    /** ��װ�� "access_token"��"expires_in"��"refresh_token"�����ṩ�����ǵĹ�����  */
    private Oauth2AccessToken mAccessToken;
    /** ע�⣺SsoHandler ���� SDK ֧�� SSO ʱ��Ч */
    private SsoHandler mSsoHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        mAuthInfo = new WeiboAuth(this, WeiboConstants.APP_KEY, WeiboConstants.REDIRECT_URL, WeiboConstants.SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
        findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	mSsoHandler.authorize(new AuthListener());
            } 
        });
	}
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO ��Ȩ�ص�
        // ��Ҫ������ SSO ��½�� Activity ������д onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        
    }
    class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            // �� Bundle �н��� Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //�������ȡ�û������ �绰������Ϣ 
            if (mAccessToken.isSessionValid()) {
                // ���� Token �� SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                Toast.makeText(LoginActivity.this, "auth_success", Toast.LENGTH_SHORT).show();
                intent2Activity(MainActivity.class);
                finish();
            } else {
                String code = values.getString("code");
                String message = "auth_faied";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            showToast("cancel");
        }

        @Override
        public void onWeiboException(WeiboException e) {
            showToast("Auth exception : "+e.getMessage());
        }
    }
}
