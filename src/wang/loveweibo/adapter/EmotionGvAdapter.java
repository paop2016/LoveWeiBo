package wang.loveweibo.adapter;

import java.util.ArrayList;

import wang.loveweibo.R;
import wang.loveweibo.utils.EmotionUtils;
import android.content.Context;
import android.widget.AbsListView.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class EmotionGvAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<String> emotionNames;
	private int itemWidthHeight;
	public EmotionGvAdapter(Context context, int itemWidthHeight, ArrayList<String> emotionNames) {
		this.context = context;
		this.emotionNames = emotionNames;
		this.itemWidthHeight = itemWidthHeight;
	}
	@Override
	public int getCount() {
		return emotionNames.size() + 1;
	}
	@Override
	public String getItem(int position) {
		return emotionNames.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			ImageView iv = new ImageView(context);
			LayoutParams params = new LayoutParams(itemWidthHeight, itemWidthHeight);
			iv.setPadding(itemWidthHeight / 8, itemWidthHeight / 8, itemWidthHeight / 8, itemWidthHeight / 8);
			iv.setLayoutParams(params);
			convertView = iv;
		}
		if(position == getCount() - 1) {
			((ImageView)convertView).setImageResource(R.drawable.emotion_delete_icon);
		}else {
			((ImageView)convertView).setImageResource(EmotionUtils.getImgByName(getItem(position)));
		}
		return convertView;
	}
	
}
