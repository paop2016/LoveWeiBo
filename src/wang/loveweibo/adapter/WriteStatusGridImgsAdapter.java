package wang.loveweibo.adapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import wang.loveweibo.R;
import wang.loveweibo.utils.Logger;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WriteStatusGridImgsAdapter extends BaseAdapter{
	Context context;
	ArrayList<Uri> datas;
	public WriteStatusGridImgsAdapter(Context context, ArrayList<Uri> datas) {
		this.context = context;
		this.datas = datas;
	}
	@Override
	public int getCount() {
		return datas.size() > 0 ? datas.size()+1 : 0;
	}

	@Override
	public Uri getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public void notifyDataSetChanged() {
		flag = false;
		super.notifyDataSetChanged();
	}
	boolean flag = false;
	BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Logger.showLog("jay", position+"");
		ViewHolder holder;
		if(convertView == null) {
			convertView = View.inflate(context, R.layout.item_grid_image, null);
			holder = new ViewHolder();
			holder.iv_delete_image = (ImageView) convertView.findViewById(R.id.iv_delete_image);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			GridView gv = (GridView) parent;
			int horizontalSpacing = gv.getHorizontalSpacing();
			int width = (gv.getWidth() - horizontalSpacing * 2 - gv.getPaddingLeft() - gv.getPaddingRight()) / 3;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
			holder.iv_image.setLayoutParams(params);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position == 0 && !flag){
			flag = true;
			return convertView;
		}
		
		if(position < getCount() - 1) {
			Uri uri = getItem(position);
			Bitmap bitmap = null;
			try {
				//第一种方法
			    bitmapOptions.inSampleSize = 4;  
				bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);
				Logger.showLog("jay", bitmap.getWidth()+"");
				//第二种方法
//				Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
//				Logger.showLog("jay", bitmap1.getWidth()+"");
//				Matrix m = new Matrix();
//				m.postScale(0.25f,0.25f);
//				bitmap = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), m, false);
//				Logger.showLog("jay", bitmap.getWidth()+"");
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.iv_image.setImageBitmap(bitmap);
			holder.iv_delete_image.setVisibility(View.VISIBLE);
			holder.iv_delete_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					datas.remove(position);
					notifyDataSetChanged();
				}
			});
		} else {
			holder.iv_image.setImageResource(R.drawable.compose_pic_add_more);
			holder.iv_delete_image.setVisibility(View.GONE);
		}
		return convertView;
	}
	private class ViewHolder {
		ImageView iv_image;
		ImageView iv_delete_image;
	}

}
