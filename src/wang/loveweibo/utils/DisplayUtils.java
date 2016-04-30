package wang.loveweibo.utils;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayUtils {
	public static int dp2px(Activity activity, int dpValue) {
//		DisplayMetrics metrics = new DisplayMetrics();
//		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		float scale = (int) metrics.density;
//		return (int) (dpValues * scale + 0.5);
		float scale = activity.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	public static int px2dp(Context context, int pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	public static int px2sp(Context context, int pxValue) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / scale +0.5f);
	}
	public static int sp2px(Context context, int spValue) {
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scale +0.5f);
	}
	//‘≠∞Ê¥Ì¡À
	public static int sp2dp(Context context, int spValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (int) (spValue * metrics.scaledDensity / metrics.density + 0.5f); 
	}
	public static int dp2sp(Context context, int dpValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (int) (dpValue * metrics.density / metrics.scaledDensity + 0.5f); 
	}
	public static int getScreenWidthPixels(Activity activity) {
//		DisplayMetrics metrics = new DisplayMetrics();
//		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		return metrics.widthPixels;
		return activity.getResources().getDisplayMetrics().widthPixels;
	}
	public static int getScreenHeightPixels(Activity activity) {
//		DisplayMetrics metrics = new DisplayMetrics();
//		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		return metrics.heightPixels;
		return activity.getResources().getDisplayMetrics().heightPixels;
	}
	
}
