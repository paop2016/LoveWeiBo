package wang.loveweibo.activity;


import java.util.ArrayList;
import java.util.List;

import wang.loveweibo.BaseActivity;
import wang.loveweibo.R;
import wang.loveweibo.adapter.StatusCommentAdapter;
import wang.loveweibo.adapter.StatusGridImgsAdapter;
import wang.loveweibo.api.SimpleRequestListener;
import wang.loveweibo.entity.Comment;
import wang.loveweibo.entity.PicUrls;
import wang.loveweibo.entity.Status;
import wang.loveweibo.entity.User;
import wang.loveweibo.entity.response.CommentsResponse;
import wang.loveweibo.utils.DateUtils;
import wang.loveweibo.utils.ImageOpHelper;
import wang.loveweibo.utils.Logger;
import wang.loveweibo.utils.StringUtils;
import wang.loveweibo.utils.TitleBuilder;
import wang.loveweibo.utils.ToastUtils;
import wang.loveweibo.widget.WrapHeightGridView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class StatusDetailActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener{
	private static final int REQUEST_CODE_WRITE_COMMENT = 2333;
	// listView headerView - ΢����Ϣ
	private View status_detail_info;
	private ImageView iv_avatar;
	private TextView tv_subhead;
	private TextView tv_caption;
	private FrameLayout include_status_image;
	private WrapHeightGridView gv_images;
	private ImageView iv_image;
	private TextView tv_content;
	private LinearLayout include_retweeted_status;
	private TextView tv_retweeted_content;
	private FrameLayout fl_retweeted_imageview;
	private WrapHeightGridView gv_retweeted_images;
	private ImageView iv_retweeted_image;
	// listView headerView - ������б�����Ϊheader�Ĳ˵���
	private LinearLayout status_detail_tab;
	private RadioGroup rg_status_detail;
	private RadioButton rb_retweets;
	private RadioButton rb_comments;
	private RadioButton rb_likes;
	// shadow_tab - ���������Ĳ˵���
	private LinearLayout shadow_status_detail_tab;
	private RadioGroup shadow_rg_status_detail;
	private RadioButton shadow_rb_retweets;
	private RadioButton shadow_rb_comments;
	private RadioButton shadow_rb_likes;
	// bottom_control - �ײ�������,����ת��/����/����
	private LinearLayout ll_bottom_control;
	private LinearLayout ll_share_bottom;
	private TextView tv_share_bottom;
	private LinearLayout ll_comment_bottom;
	private TextView tv_comment_bottom;
	private LinearLayout ll_like_bottom;
	private TextView tv_like_bottom;
	// listView - ����ˢ�¿ؼ�
	private PullToRefreshListView plv_status_detail;
	// ����ҳ��΢����Ϣ
	private Status status;
	// �Ƿ���Ҫ���������۲���
	private boolean scroll2Comment;
	// ���۵�ǰ�Ѽ�������ҳ��
	private int curPage = 1;
	// footView - ���ظ���
	private View footView;
	private List<Comment> comments = new ArrayList<Comment>();
	private StatusCommentAdapter adapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_detail);
		//����
		status = (Status) getIntent().getSerializableExtra("status");
		scroll2Comment = getIntent().getBooleanExtra("scroll2Comment", false);
		Logger.showLog("jay", scroll2Comment+"");
		initView();
		setData();
		addFootView();
		loadComments(1);
	}


	private void initView() {
		//������
		initTitle();
		//΢������
		initDetailHead();
		//���ص�Tab
		initTab();
		initListView();
		
		//bottom_control
		initControlBar();
	}

	private void initTitle() {
		new TitleBuilder(this).setImageLeftRes(R.drawable.navigationbar_back_sel)
		.setTitle("微博正文").setOnClickListenerLeft(this);
	}
	private void  initDetailHead() {
		status_detail_info = View.inflate(this, R.layout.item_status, null);
		status_detail_info.setBackgroundResource(R.color.white);
		status_detail_info.findViewById(R.id.ll_control_status).setVisibility(View.GONE);
		iv_avatar = (ImageView) status_detail_info.findViewById(R.id.iv_avatar);
		tv_subhead = (TextView) status_detail_info.findViewById(R.id.tv_subhead);
		tv_caption = (TextView) status_detail_info.findViewById(R.id.tv_caption);
		tv_content = (TextView) status_detail_info.findViewById(R.id.tv_content);
		include_status_image = (FrameLayout) status_detail_info.findViewById(R.id.include_image_status);
		iv_image = (ImageView) status_detail_info.findViewById(R.id.iv_image);
		gv_images = (WrapHeightGridView) status_detail_info.findViewById(R.id.gv_image);
		//retweeted status
		include_retweeted_status = (LinearLayout) status_detail_info.findViewById(R.id.include_retweeted_status);
		tv_retweeted_content = (TextView) status_detail_info.findViewById(R.id.tv_retweeted_content);
		fl_retweeted_imageview = (FrameLayout) include_retweeted_status.findViewById(R.id.include_image_status);
		gv_retweeted_images = (WrapHeightGridView) include_retweeted_status.findViewById(R.id.gv_image);
		iv_retweeted_image = (ImageView) include_retweeted_status.findViewById(R.id.iv_image);
		
		//status_detail_tab
		status_detail_tab = (LinearLayout) View.inflate(this, R.layout.status_detail_tab, null);
		rg_status_detail = (RadioGroup) status_detail_tab.findViewById(R.id.rg_status_detail);
		rb_comments = (RadioButton) status_detail_tab.findViewById(R.id.rb_comments);
		rb_likes = (RadioButton) status_detail_tab.findViewById(R.id.rb_likes);
		rb_retweets = (RadioButton) status_detail_tab.findViewById(R.id.rb_retweets);
		rg_status_detail.setOnCheckedChangeListener(this);
	}
	private void initTab() {
		shadow_status_detail_tab = (LinearLayout) findViewById(R.id.status_detail_tab);
		shadow_rg_status_detail = (RadioGroup) findViewById(R.id.rg_status_detail);
		shadow_rb_comments = (RadioButton) findViewById(R.id.rb_comments);
		shadow_rb_likes = (RadioButton) findViewById(R.id.rb_likes);
		shadow_rb_retweets = (RadioButton) findViewById(R.id.rb_retweets);
		shadow_rg_status_detail.setOnCheckedChangeListener(this);
	}
	private void initControlBar() {
		ll_bottom_control = (LinearLayout) findViewById(R.id.status_detail_controlbal);
		ll_comment_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_comment_bottom);
		ll_like_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_like_bottom);
		ll_share_bottom = (LinearLayout) ll_bottom_control.findViewById(R.id.ll_share_button);
		ll_bottom_control.setBackgroundResource(R.color.white);
		ll_comment_bottom.setOnClickListener(this);
		ll_like_bottom.setOnClickListener(this);
		ll_share_bottom.setOnClickListener(this);
	}

	private void setData() {
		User user = status.getUser();
		imageLoader.displayImage(user.getProfile_image_url(), iv_avatar, ImageOpHelper.getAvatarOptions());
		tv_subhead.setText(user.getName());
		tv_caption.setText(DateUtils.getShortTime(status.getCreated_at()) + " 来自" + Html.fromHtml(status.getSource()));
		
		tv_content.setText(StringUtils.getWeiboContent(this, tv_content, status.getText()));
		setImages(status, include_status_image, iv_image, gv_images);
		Status retweetedStatus = status.getRetweeted_status();
		if(retweetedStatus != null) {
			include_retweeted_status.setVisibility(View.VISIBLE);
			String retweetedContent = "@" + retweetedStatus.getUser().getName() + ":" + retweetedStatus.getText();
			tv_retweeted_content.setText(StringUtils.getWeiboContent(this, tv_retweeted_content, retweetedContent));
			setImages(retweetedStatus, fl_retweeted_imageview, iv_retweeted_image, gv_retweeted_images);
		}
		//shadow_tab
		shadow_rb_retweets.setText("转发" + status.getReposts_count());
		shadow_rb_likes.setText("赞" + status.getAttitudes_count());
		shadow_rb_comments.setText("评论" + status.getComments_count());
		//tab
		rb_retweets.setText("转发" + status.getReposts_count());
		rb_likes.setText("赞" + status.getAttitudes_count());
		rb_comments.setText("评论" + status.getComments_count());
	}
	private void initListView() {
		plv_status_detail = (PullToRefreshListView) findViewById(R.id.plv_status_detail);
		adapter = new StatusCommentAdapter(this, comments);
		plv_status_detail.setAdapter(adapter);
		footView = View.inflate(this, R.layout.footview_loading, null);
		ListView lv = plv_status_detail.getRefreshableView();
		lv.addHeaderView(status_detail_info);
		lv.addHeaderView(status_detail_tab);
		plv_status_detail.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadComments(1);
			}
			
		});
		plv_status_detail.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadComments(curPage + 1);
			}
		});
		plv_status_detail.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				shadow_status_detail_tab.setVisibility(firstVisibleItem >= 2 ? View.VISIBLE : View.GONE);
			}
		});
	}

	private void setImages(Status status, ViewGroup imgContainer,
			ImageView iv_image, WrapHeightGridView gv_images) {
		String picUrl = status.getBmiddle_pic();
		ArrayList<PicUrls> picUrls = status.getPic_urls();
		if(picUrls != null && picUrls.size() > 1) {
			imgContainer.setVisibility(View.VISIBLE);
			iv_image.setVisibility(View.GONE);
			gv_images.setVisibility(View.VISIBLE);
			StatusGridImgsAdapter adapter = new StatusGridImgsAdapter(this, picUrls);
			gv_images.setAdapter(adapter);
		}else if(picUrl != null) {
			imgContainer.setVisibility(View.VISIBLE);
			iv_image.setVisibility(View.VISIBLE);
			gv_images.setVisibility(View.GONE);
			imageLoader.displayImage(picUrl, iv_image);
		}else {
			imgContainer.setVisibility(View.GONE);
		}
	}
	private void loadComments(final int page) {
		api.commentsShow(status.getId(), page, new SimpleRequestListener(this, null) {
			@Override
			public void onComplete(String response) {
				super.onComplete(response);
				if(page == 1)
					comments.clear();
				CommentsResponse commentsResponse = gson.fromJson(response, CommentsResponse.class);
				shadow_rb_comments.setText("评论" + commentsResponse.getTotal_number());
				rb_comments.setText("评论" + commentsResponse.getTotal_number());
				//�Լ�
				curPage = page;
				addData(commentsResponse);
				if(scroll2Comment) {
					plv_status_detail.getRefreshableView().setSelection(2);
					scroll2Comment = false;
				}
			}
			@Override
			public void onAllDone() {
				super.onAllDone();
				plv_status_detail.onRefreshComplete();
			}
		});
	}
	protected void addData(CommentsResponse commentsResponse) {
		Logger.showLog("jay", commentsResponse.getTotal_number() + "");
		Logger.showLog("jay", commentsResponse.getComments().size()+"");
		
		for (Comment comment : commentsResponse.getComments()) {
			if(!comments.contains(comment)) {
				comments.add(comment);
			}
		}
		adapter.notifyDataSetChanged();
		
		//-1 ���΢��Api�������۵��ӳ�
		if (comments.size() < commentsResponse.getTotal_number() - 1) {
			addFootView();
		} else {
			removeFootView();
		}
	}


	private void removeFootView() {
		ListView lv = plv_status_detail.getRefreshableView();
		if(lv.getFooterViewsCount() > 1)
			lv.removeFooterView(footView);
	}


	private void addFootView() {
		ListView lv = plv_status_detail.getRefreshableView();
		if(lv.getFooterViewsCount() == 1)
			lv.addFooterView(footView);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			finish();
			break;
		case R.id.ll_comment_bottom:
			Intent intent = new Intent(this, WriteCommentActivity.class);
			intent.putExtra("status", status);
			startActivityForResult(intent, REQUEST_CODE_WRITE_COMMENT);
			break;
		case R.id.ll_like_bottom:
			ToastUtils.showToast(this, "点个赞~", Toast.LENGTH_SHORT);
			break;
		case R.id.ll_share_button:
			Intent intent1 = new Intent(this, WriteStatusActivity.class);
			intent1.putExtra("status", status);
			startActivity(intent1);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_comments:
			rb_comments.setChecked(true);
			shadow_rb_comments.setChecked(true);
			break;
		case R.id.rb_retweets:
			rb_retweets.setChecked(true);
			shadow_rb_retweets.setChecked(true);
			break;
		case R.id.rb_likes:
			rb_likes.setChecked(true);
			shadow_rb_likes.setChecked(true);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_CANCELED)
			return;
		switch (requestCode) {
		case REQUEST_CODE_WRITE_COMMENT:
			boolean sentCommentSuccess = data.getBooleanExtra("sentCommentSuccess", false);
			if(sentCommentSuccess) {
				scroll2Comment = true;
				loadComments(1);
			}
			break;

		default:
			break;
		}
	}
}
