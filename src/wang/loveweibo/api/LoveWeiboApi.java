package wang.loveweibo.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import wang.loveweibo.constants.AccessTokenKeeper;
import wang.loveweibo.constants.URLs;
import android.R.integer;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;


public class LoveWeiboApi extends WeiboAPI{
	private Handler handler = new Handler(Looper.getMainLooper());
	public LoveWeiboApi(Oauth2AccessToken oauth2AccessToken) {
		super(oauth2AccessToken);
	}
	public LoveWeiboApi(Context context) {
		this(AccessTokenKeeper.readAccessToken(context));
	}
	@Override
	protected void request(String url, WeiboParameters params,
			String httpMethod, RequestListener listener) {
		super.request(url, params, httpMethod, listener);
	}
	public void requestInMainLooper(String url, WeiboParameters params, String httpMethod, final RequestListener listener) {
		request(url, params, httpMethod, new RequestListener() {
			
			@Override
			public void onIOException(final IOException e) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						listener.onIOException(e);
					}
				});
			}
			
			@Override
			public void onError(final WeiboException e) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						listener.onError(e);
					}
				});
			}
			
			@Override
			public void onComplete4binary(final ByteArrayOutputStream responseOS) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						listener.onComplete4binary(responseOS);
					}
				});
			}
			
			@Override
			public void onComplete(final String response) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						listener.onComplete(response);
					}
				});
			}
		});
	}
	public void statusesHome_timeline(int page, RequestListener listener) {
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("page", page);
		requestInMainLooper(URLs.statusesHome_timeline, parameters, HTTPMETHOD_GET, listener);
	}
	public void commentsShow(long id, long page, RequestListener listener) {
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("page", page);
		parameters.add("id", id);
		requestInMainLooper(URLs.commentsShow, parameters, WeiboAPI.HTTPMETHOD_GET, listener);
	}
	public void comentsCreate(long id, String comment, RequestListener listener) {
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("id", id);
		parameters.add("comment", comment);
		requestInMainLooper(URLs.commentsCreate, parameters, WeiboAPI.HTTPMETHOD_POST, listener);
	}
	public void sendStatus(String statusContent, String imgFilePath, long retweetedStatusId, RequestListener listener) {
		String url;
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("status", statusContent);
		if(retweetedStatusId > 0) {
			parameters.add("id", retweetedStatusId);
			url = URLs.statusesRepost;
		}
		else if(imgFilePath != null) {
			parameters.add("pic", imgFilePath);
			url = URLs.statusesUpload;
		}else {
			url = URLs.statusesUpdate;
		}
		requestInMainLooper(url, parameters, WeiboAPI.HTTPMETHOD_POST, listener);
	}
}
