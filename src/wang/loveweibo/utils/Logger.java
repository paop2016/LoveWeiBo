package wang.loveweibo.utils;

import wang.loveweibo.constants.CommenConstants;
import android.util.Log;

public class Logger {
	public static void showLog(String TAG, String msg) {
		showLog(TAG, msg, Log.INFO);
	}
	public static void showLog(String TAG, String msg, int level) {
		if(!CommenConstants.isShowLog) {
			return;
		}
		switch (level) {
		case Log.VERBOSE:
			Log.v(TAG, msg);
			break;
		case Log.DEBUG:
			Log.d(TAG, msg);
			break;
		case Log.INFO:
			Log.i(TAG, msg);
			break;
		case Log.WARN:
			Log.w(TAG, msg);
			break;
		case Log.ERROR:
			Log.e(TAG, msg);
			break;
		default:
			Log.i(TAG, msg);
			break;
		}
	}
}
