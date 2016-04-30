package wang.loveweibo.adapter;

import java.util.ArrayList;
import java.util.List;

import wang.loveweibo.R;
import wang.loveweibo.entity.PicUrls;
import wang.loveweibo.entity.Status;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class StatusGridImgsAdapter1 extends BaseAdapter{
	Context context;
	ImageLoader imageLoader;
	ArrayList<PicUrls> datas;
	public StatusGridImgsAdapter1(Context context, ArrayList<PicUrls> datas) {
		this.context = context;
		this.datas = datas;
		imageLoader = ImageLoader.getInstance();
	}
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder; 
		if(convertView == null) {
			convertView = View.inflate(context, R.layout.item_grid_image, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		GridView gridView = (GridView) parent;
		int horizontalSpacing = gridView.getHorizontalSpacing();
		int numColumns = gridView.getNumColumns();
		int width = (gridView.getWidth() - (numColumns - 1) * horizontalSpacing
				- gridView.getPaddingLeft() - gridView.getPaddingRight()) / numColumns;
		LayoutParams params = new LayoutParams(width, width);
		holder.imageView.setLayoutParams(params);
		PicUrls picUrls = (PicUrls) getItem(position);
		imageLoader.displayImage(picUrls.getBmiddle_pic(), holder.imageView);
		return convertView;
	}
	
	public static class ViewHolder{
		public ImageView imageView;
	}
}
