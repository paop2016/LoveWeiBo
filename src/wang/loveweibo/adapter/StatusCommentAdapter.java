package wang.loveweibo.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import wang.loveweibo.R;
import wang.loveweibo.entity.Comment;
import wang.loveweibo.entity.User;
import wang.loveweibo.utils.DateUtils;
import wang.loveweibo.utils.ImageOpHelper;
import wang.loveweibo.utils.StringUtils;
import wang.loveweibo.utils.ToastUtils;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class StatusCommentAdapter extends BaseAdapter{
	private Context context;
	private List<Comment> comments;
	private ImageLoader imageLoader;
	public StatusCommentAdapter(Context context, List<Comment> comments) {
		this.context = context;
		this. comments = comments;
		imageLoader = ImageLoader.getInstance();
	}
	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_comment, null);
			holder.ll_comment = (LinearLayout) convertView.findViewById(R.id.ll_comments);
			holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
			holder.tv_subhead = (TextView) convertView.findViewById(R.id.tv_subhead);
			holder.tv_caption = (TextView) convertView.findViewById(R.id.tv_caption);
			holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		Comment comment = comments.get(position);
		User user = comment.getUser();
		
		imageLoader.displayImage(user.getAvatar_large(), holder.iv_avatar, ImageOpHelper.getAvatarOptions());
		holder.tv_subhead.setText(user.getName());
		holder.tv_subhead.setTextColor(context.getResources().getColor(R.color.txt_black));
		holder.tv_caption.setText(DateUtils.getShortTime(comment.getCreated_at()));
		holder.tv_comment.setText(StringUtils.getWeiboContent(context, holder.tv_comment, comment.getText()));
		holder.ll_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToastUtils.showToast(context, "代表月亮评论你~", Toast.LENGTH_SHORT);
			}
		});
		return convertView;
	}
	private class ViewHolder {
		public LinearLayout ll_comment;
		public ImageView iv_avatar;
		public TextView tv_subhead;
		public TextView tv_caption;
		public TextView tv_comment;
	}
}
