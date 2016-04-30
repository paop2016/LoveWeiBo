package wang.loveweibo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wang.loveweibo.R;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StringUtils {
	public static SpannableString getWeiboContent(final Context context, TextView textView, String source) {
		String regexAt = "@[\u4e00-\u9fa5\\w]+";
		String regexTopic = "#[\u4e00-\u9fa5\\w]+#";  
		String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";
		String regexHttp = "[http]{4}:/[/\\w.]+"; 
//		String regex = "(" + regexAt + ")|(" + regexTopic + ")|(" + regexEmoji + ")";
		String regex = "(" + regexAt + ")|(" + regexTopic + ")|(" + regexEmoji + ")|(" + "   网页地址" + ")";
		
		String theSource = source.replaceAll(regexHttp, "   网页地址");
		SpannableString spannableString = new SpannableString(theSource);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(spannableString);
		if(matcher.find()) {
			textView.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}
		int count = 0;
		while(matcher.find()) {
			final String atStr = matcher.group(1);
			final String topicStr = matcher.group(2);
			String emojiStr = matcher.group(3);
			final String httpStr = matcher.group(4);
			if(atStr != null) {
				int start = matcher.start(1);
				MyClickableSpan clickableSpan = new MyClickableSpan(context){
					public void onClick(View widget) {
						ToastUtils.showToast(context, "艾特: "+atStr, Toast.LENGTH_SHORT);
					};
				};
				spannableString.setSpan(clickableSpan, start, start + atStr.length(), 
						SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			if(topicStr != null) {
				int start = matcher.start(2);
				MyClickableSpan clickableSpan = new MyClickableSpan(context){
					@Override
					public void onClick(View widget) {
						ToastUtils.showToast(context, "话题: " + topicStr, Toast.LENGTH_SHORT);
					}
				};
				spannableString.setSpan(clickableSpan, start, start + topicStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			if(httpStr != null) {
				count++;
				int start = matcher.start(4);
				Pattern p = Pattern.compile(regexHttp);
				final Matcher m = p.matcher(source);
				for(int i = count ; i > 0 ; i--){
					m.find();
				}
					MyClickableSpan clickableSpan = new MyClickableSpan(context){
						@Override
						public void onClick(View widget) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(m.group()));
							context.startActivity(intent);
						}
					};
					spannableString.setSpan(clickableSpan, start, start + httpStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
					Logger.showLog("jay", httpStr.length() + "");
			}
			
			if(emojiStr != null) {
				int start = matcher.start(3);
				
				int imgRes = EmotionUtils.getImgByName(emojiStr);
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
				
				if(bitmap != null) {
					int size = (int) textView.getTextSize();
					bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
					
					ImageSpan imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start + emojiStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
				
				}
			
			}
		}
		return spannableString;
	}
	static class MyClickableSpan extends ClickableSpan {
		private Context context;
		public MyClickableSpan(Context context) {
			this.context = context;
		}
		@Override
		public void onClick(View widget) {
		}
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(context.getResources().getColor(R.color.txt_at_blue));
			ds.setUnderlineText(false);
		}
	}
}
