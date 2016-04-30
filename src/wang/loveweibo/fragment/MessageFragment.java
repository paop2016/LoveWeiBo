package wang.loveweibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import wang.loveweibo.BaseFragment;
import wang.loveweibo.R;
import wang.loveweibo.utils.TitleBuilder;
import wang.loveweibo.utils.ToastUtils;


public class MessageFragment extends BaseFragment{
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = View.inflate(activity, R.layout.frag_message, null);
		new TitleBuilder(view).setTextLeft("Left").setTitle("Right").setImageRightRes(R.drawable.com_sina_weibo_sdk_login_button_with_account_text).setOnClickListenerLeft(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(activity, "左", 2000);
			}
		}).setOnClickListenerRight(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(activity, "右", 2000);
			}
		});
		return view;
	}
}
