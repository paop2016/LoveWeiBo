package wang.loveweibo.utils;

import wang.loveweibo.R;
import android.R.integer;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleBuilder {
	private ImageView imageView_left;
	private ImageView imageView_right;
	private TextView textView_left;
	private TextView textView_right;
	private TextView textView;
	public TitleBuilder(View view) {
		imageView_left = (ImageView) view.findViewById(R.id.titlebar_iv_left);
		imageView_right = (ImageView) view.findViewById(R.id.titlebar_iv_right);
		textView_left = (TextView) view.findViewById(R.id.titlebar_tv_left);
		textView_right = (TextView) view.findViewById(R.id.titlebar_tv_right);
		textView = (TextView) view.findViewById(R.id.titlebar_tv);
	}
	public TitleBuilder(Activity context) {
		imageView_left = (ImageView) context.findViewById(R.id.titlebar_iv_left);
		imageView_right = (ImageView) context.findViewById(R.id.titlebar_iv_right);
		textView_left = (TextView) context.findViewById(R.id.titlebar_tv_left);
		textView_right = (TextView) context.findViewById(R.id.titlebar_tv_right);
		textView = (TextView) context.findViewById(R.id.titlebar_tv);
	}
	public TitleBuilder setImageLeftRes(int resourceId) {
		imageView_left.setVisibility(View.VISIBLE);
		imageView_left.setImageResource(resourceId);
		return this;
	}
	public TitleBuilder setTextLeft(String str) {
		textView_left.setVisibility(View.VISIBLE);
		textView_left.setText(str);
		return this;
	}
	public TitleBuilder setOnClickListenerLeft(OnClickListener listener) {
		if(imageView_left.getVisibility() == View.VISIBLE) {
			imageView_left.setOnClickListener(listener);
		}
		if(textView_left.getVisibility() == View.VISIBLE) {
			textView_left.setOnClickListener(listener);
		}
		return this;
	}
	public TitleBuilder setTitle(String str) {
		textView.setText(str);
		return this;
	}
	public TitleBuilder setTitleBgRes(int resourceId) {
		textView.setBackgroundResource(resourceId);
		return this;
	}
	public TitleBuilder setImageRightRes(int resourceId) {
		imageView_right.setVisibility(View.VISIBLE);
		imageView_right.setImageResource(resourceId);
		return this;
	}
	public TitleBuilder setTextRight(String str) {
		textView_right.setVisibility(View.VISIBLE);
		textView_right.setText(str);
		return this;
	}
	public TitleBuilder setOnClickListenerRight(OnClickListener listener) {
		if(imageView_right.getVisibility() == View.VISIBLE) {
			imageView_right.setOnClickListener(listener);
		}
		if(textView_right.getVisibility() == View.VISIBLE) {
			textView_right.setOnClickListener(listener);
		}
		return this;
	}
	
}
