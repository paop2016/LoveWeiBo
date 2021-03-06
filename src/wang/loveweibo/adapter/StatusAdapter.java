package wang.loveweibo.adapter;

import java.util.ArrayList;
import java.util.List;

import wang.loveweibo.R;
import wang.loveweibo.activity.StatusDetailActivity;
import wang.loveweibo.activity.WriteCommentActivity;
import wang.loveweibo.activity.WriteStatusActivity;
import wang.loveweibo.entity.PicUrls;
import wang.loveweibo.entity.Status;
import wang.loveweibo.entity.User;
import wang.loveweibo.utils.DateUtils;
import wang.loveweibo.utils.ImageOpHelper;
import wang.loveweibo.utils.Logger;
import wang.loveweibo.utils.StringUtils;
import wang.loveweibo.utils.ToastUtils;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
public class StatusAdapter extends BaseAdapter{
	private Context context;
	private List<Status> datas;
	private ImageLoader imageLoader;
	private boolean onTouchEvent;
	public StatusAdapter(Context context, List<Status> datas) {
		this.context = context;
		this.datas = datas;
		imageLoader = ImageLoader.getInstance();
	}
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Status getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_status, null);
			holder.ll_card_content = (LinearLayout) convertView.findViewById(R.id.ll_card_content);
			holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
//			holder.rl_content = (RelativeLayout) convertView
//					.findViewById(R.id.rl_content);
			holder.tv_subhead = (TextView) convertView.findViewById(R.id.tv_subhead);
			holder.tv_caption = (TextView) convertView.findViewById(R.id.tv_caption);
			
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.include_status_image = (FrameLayout) convertView.findViewById(R.id.include_image_status);
			holder.gv_images = (GridView) holder.include_status_image.findViewById(R.id.gv_image);
			holder.iv_image = (ImageView) holder.include_status_image.findViewById(R.id.iv_image);
			
			holder.include_retweeted_status = (LinearLayout) convertView.findViewById(R.id.include_retweeted_status);
			holder.include_retweeted_status_image = (FrameLayout) holder.include_retweeted_status.findViewById(R.id.include_image_status);
			holder.tv_retweeted_content = (TextView) convertView.findViewById(R.id.tv_retweeted_content);
			holder.iv_retweeted_image = (ImageView) holder.include_retweeted_status.findViewById(R.id.iv_image);
			holder.gv_retweeted_images = (GridView) holder.include_retweeted_status.findViewById(R.id.gv_image);
			
			holder.ll_share_bottom = (LinearLayout) convertView.findViewById(R.id.ll_share_button);
			holder.ll_comment_bottom = (LinearLayout) convertView.findViewById(R.id.ll_comment_bottom);
			holder.ll_like_bottom = (LinearLayout) convertView.findViewById(R.id.ll_like_bottom);
			holder.iv_share_bottom = (ImageView) convertView.findViewById(R.id.iv_share_button);
			holder.tv_share_bottom = (TextView) convertView.findViewById(R.id.tv_share_button);
			holder.iv_comment_bottom = (ImageView) convertView.findViewById(R.id.iv_comment_bottom);
			holder.tv_comment_bottom = (TextView) convertView.findViewById(R.id.tv_comment_bottom);
			holder.iv_like_bottom = (ImageView) convertView.findViewById(R.id.iv_like_bottom);
			holder.tv_like_bottom = (TextView) convertView.findViewById(R.id.tv_like_bottom);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		//bind data
		final Status status = getItem(position);
		User user = status.getUser();
		imageLoader.displayImage(user.getAvatar_large(), holder.iv_avatar, ImageOpHelper.getAvatarOptions());
		holder.tv_subhead.setText(user.getName());
		if(TextUtils.isEmpty(status.getSource())) {
			holder.tv_caption.setText(DateUtils.getShortTime(status.getCreated_at()));
		}else
			holder.tv_caption.setText(DateUtils.getShortTime(status.getCreated_at()) + " 来自 " + Html.fromHtml(status.getSource()));
		holder.tv_content.setText(StringUtils.getWeiboContent(context, holder.tv_content, status.getText()));
		setImages(status, holder.include_status_image, holder.gv_images, holder.iv_image);
		//ת����
		
		final Status retweeted_status = status.getRetweeted_status();
		if(retweeted_status != null) {
			User reUser = retweeted_status.getUser();
			
			holder.include_retweeted_status.setVisibility(View.VISIBLE);
			if(reUser == null) {
				holder.tv_retweeted_content.setText("该微博已被删除");
			}else {
				String retweetedContent = "@" + reUser.getName() + ":" + retweeted_status.getText();
				holder.tv_retweeted_content.setText(StringUtils.getWeiboContent(
						context, holder.tv_retweeted_content, retweetedContent));
				setImages(retweeted_status, holder.include_retweeted_status_image,
						holder.gv_retweeted_images,  holder.iv_retweeted_image);
			}
		} else {
			holder.include_retweeted_status.setVisibility(View.GONE);
		}
		holder.tv_share_bottom.setText(status.getReposts_count() == 0 ? "转发" : status.getReposts_count()+"");
		holder.tv_comment_bottom.setText(status.getComments_count() == 0 ? "评论" : status.getComments_count()+"");
		holder.tv_like_bottom.setText(status.getAttitudes_count() == 0 ? "赞" : status.getAttitudes_count()+"");
		holder.ll_card_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, StatusDetailActivity.class);
				intent.putExtra("status", status);
				context.startActivity(intent);
			}
		});
		holder.include_retweeted_status.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, StatusDetailActivity.class);
				intent.putExtra("status", retweeted_status);
				context.startActivity(intent);
			}
		});
		holder.ll_comment_bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(status.getComments_count() > 0){
					Intent intent = new Intent(context, StatusDetailActivity.class);
					intent.putExtra("status", status);
					intent.putExtra("scroll2Comment", true);
					context.startActivity(intent);
				} else {
					Intent intent = new Intent(context, WriteCommentActivity.class);
					intent.putExtra("status", status);
					context.startActivity(intent);
				}
			}
		});
		
		holder.ll_share_bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, WriteStatusActivity.class);
				intent.putExtra("status", status);
				context.startActivity(intent);
			}
		});
		holder.ll_like_bottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(context, "点个赞~", Toast.LENGTH_SHORT);
			}
		});
		return convertView;
	}
	private void setImages(Status status, FrameLayout imgContainer,
			GridView gv_images, ImageView iv_image) {
		ArrayList<PicUrls> pic_urls = status.getPic_urls();
		String bmiddle_pic = status.getBmiddle_pic();
		if(pic_urls != null && pic_urls.size() > 1) {
			imgContainer.setVisibility(View.VISIBLE);
			iv_image.setVisibility(View.GONE);
			gv_images.setVisibility(View.VISIBLE);
			StatusGridImgsAdapter1 gvAdapter = new StatusGridImgsAdapter1(context, pic_urls);
			gv_images.setAdapter(gvAdapter);
		} else if(bmiddle_pic != null) {
			imgContainer.setVisibility(View.VISIBLE);
			gv_images.setVisibility(View.GONE);
			iv_image.setVisibility(View.VISIBLE);
			imageLoader.displayImage(bmiddle_pic, iv_image);
		} else {
			imgContainer.setVisibility(View.GONE);
		}
	}
	private class ViewHolder{
		//��������
		public LinearLayout ll_card_content;
		public ImageView iv_avatar;
		public RelativeLayout rl_content;
		public TextView tv_subhead;
		public TextView tv_caption;
		//ԭ����
		public TextView tv_content;
		public FrameLayout include_status_image;
		public GridView gv_images;
		public ImageView iv_image;
		//ת����
		public LinearLayout include_retweeted_status;
		public FrameLayout include_retweeted_status_image;
		public TextView tv_retweeted_content;
		public GridView gv_retweeted_images;
		public ImageView iv_retweeted_image;
		//������
		public LinearLayout ll_share_bottom;
		public ImageView iv_share_bottom;
		public TextView tv_share_bottom;
		public LinearLayout ll_comment_bottom;
		public ImageView iv_comment_bottom;
		public TextView tv_comment_bottom;
		public LinearLayout ll_like_bottom;
		public ImageView iv_like_bottom;
		public TextView tv_like_bottom;
		
	}
}
