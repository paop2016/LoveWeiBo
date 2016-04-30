package wang.loveweibo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import wang.loveweibo.BaseActivity;
import wang.loveweibo.R;
import wang.loveweibo.adapter.EmotionGvAdapter;
import wang.loveweibo.adapter.EmotionPagerAdapter;
import wang.loveweibo.adapter.WriteStatusGridImgsAdapter;
import wang.loveweibo.api.SimpleRequestListener;
import wang.loveweibo.entity.Status;
import wang.loveweibo.utils.DisplayUtils;
import wang.loveweibo.utils.EmotionUtils;
import wang.loveweibo.utils.ImageOpHelper;
import wang.loveweibo.utils.ImageUtils;
import wang.loveweibo.utils.StringUtils;
import wang.loveweibo.utils.TitleBuilder;
import wang.loveweibo.utils.ToastUtils;
import wang.loveweibo.widget.WrapHeightGridView;

public class WriteStatusActivity extends BaseActivity implements OnClickListener, OnItemClickListener{
	private EditText et_write_status;
	private WrapHeightGridView gv_write_status;
	
	private View include_retweeted_status_card;
	private ImageView iv_retweeted_card;
	private TextView tv_retweeted_card_user;
	private TextView tv_retweeted_card_content;
	
	private ImageView iv_image;
	private ImageView iv_at;
	private ImageView iv_topic;
	private ImageView iv_emoji;
	private ImageView iv_add;
	
	private Status retweeted_status;
	private Status cardStatus;
	private ViewPager vp_emotion_dashboard;
	private LinearLayout ll_emotion_dashboard;
	private EmotionPagerAdapter emotionPagerAdapter;
	
	ProgressDialog progressDialog;
	ArrayList<Uri> imgUris = new ArrayList<Uri>();
	WriteStatusGridImgsAdapter statusImgsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_status);
		retweeted_status = (Status) getIntent().getSerializableExtra("status");
		init();
	}

	private void init() {
		new TitleBuilder(this).setTitle("发微博").setTextLeft("取消").setTextRight("发送")
		.setOnClickListenerLeft(this).setOnClickListenerRight(this);
		et_write_status = (EditText) findViewById(R.id.et_write_status);
		gv_write_status = (WrapHeightGridView) findViewById(R.id.gv_write_status);
		include_retweeted_status_card = findViewById(R.id.include_retweeted_status_card);
		iv_retweeted_card = (ImageView) findViewById(R.id.iv_retweeted_card);
		tv_retweeted_card_user = (TextView) findViewById(R.id.tv_retweeted_card_user);
		tv_retweeted_card_content = (TextView) findViewById(R.id.tv_retweeted_card_content);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		iv_at = (ImageView) findViewById(R.id.iv_at);
		iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
		iv_topic = (ImageView) findViewById(R.id.iv_topic);
		ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
		vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
		et_write_status.setHint("分享新鲜事...");
		
		statusImgsAdapter = new WriteStatusGridImgsAdapter(this, imgUris);
		gv_write_status.setAdapter(statusImgsAdapter);
		gv_write_status.setOnItemClickListener(this);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("发送中...");

		iv_image.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
		iv_add.setOnClickListener(this);
		initRetweetedStatus();
		initEmotion();
		
	}
	
	private void initRetweetedStatus() {
		if (retweeted_status != null) {
			Status rrStatus = retweeted_status.getRetweeted_status();
			if(rrStatus != null) {
				cardStatus = rrStatus;
				et_write_status.setText(StringUtils.getWeiboContent(this, et_write_status, "//@"+retweeted_status.getUser().getName()+":"+retweeted_status.getText()));
				et_write_status.setSelection(0);
			}else {
				cardStatus = retweeted_status;
				et_write_status.setHint("说说分享心得...");
			}
			if (TextUtils.isEmpty(cardStatus.getBmiddle_pic())) {
				imageLoader.displayImage(cardStatus.getUser().getAvatar_large(), iv_retweeted_card, ImageOpHelper.getAvatarOptions());
			}else {
				imageLoader.displayImage(cardStatus.getBmiddle_pic(), iv_retweeted_card, ImageOpHelper.getImgOptions());
			}
			tv_retweeted_card_user.setText("@" + cardStatus.getUser().getName());
			tv_retweeted_card_content.setText(cardStatus.getText());
			iv_image.setVisibility(View.GONE);
			include_retweeted_status_card.setVisibility(View.VISIBLE);
		}
	}

	private void initEmotion() {
		int gv_width = DisplayUtils.getScreenWidthPixels(this);
		int spacing = DisplayUtils.dp2px(this, 8);
		int itemWidthHeight = (gv_width - 8 * spacing) / 7; 
		int gv_height = itemWidthHeight * 3 + spacing * 4;
		
		ArrayList<GridView> gvs = new ArrayList<GridView>();
		ArrayList<String> emotionNames = new ArrayList<String>();
		for(String name : EmotionUtils.emojiMap.keySet()) {
			emotionNames.add(name);
			if( emotionNames.size() == 20) {
				GridView gv = createGridView(itemWidthHeight, spacing, gv_width, gv_height, emotionNames);
				gvs.add(gv);
//				emotionNames.clear();
//				����
				emotionNames = new ArrayList<String>();
			} 
		}
		if(emotionNames.size() > 0) {
			GridView gv = createGridView(itemWidthHeight, spacing, gv_width, gv_height, emotionNames);
			gvs.add(gv);
		}
		
		emotionPagerAdapter = new EmotionPagerAdapter(gvs);
		vp_emotion_dashboard.setAdapter(emotionPagerAdapter);
		//�Ȳ�����
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gv_width, gv_height);
		vp_emotion_dashboard.setLayoutParams(params);
//		���ż���ll�Ŀ��
		
	}
	private GridView createGridView(int itemWidthHeight, int spacing,
			int gv_width, int gv_height, ArrayList<String> emotionNames) {
		GridView gv = new GridView(this);
		gv.setBackgroundResource(R.color.bg_gray);
		gv.setPadding(spacing, spacing, spacing, spacing);
		gv.setHorizontalSpacing(spacing);
		gv.setVerticalSpacing(spacing);
		gv.setNumColumns(7);
		gv.setSelector(R.color.transparent);
		LayoutParams params = new LayoutParams(gv_width, gv_height);
		gv.setLayoutParams(params);
		EmotionGvAdapter emotionGvAdapter = new EmotionGvAdapter(this, itemWidthHeight, emotionNames);
		gv.setAdapter(emotionGvAdapter);
		gv.setOnItemClickListener(this);
		//���ñ���
		return gv;
	}

	private void updateImgs() {
		if(imgUris.size() > 0){
			gv_write_status.setVisibility(View.VISIBLE);
			statusImgsAdapter.notifyDataSetChanged();
		}
		else {
			gv_write_status.setVisibility(View.GONE);
		}	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_tv_left:
			finish();
			break;
		case R.id.titlebar_tv_right:
			sendStatus();
			break;
		case R.id.iv_image:
			ImageUtils.showImagePickDialog(this);
			break;
		case R.id.iv_emoji:
			if (ll_emotion_dashboard.getVisibility() == View.GONE) {
				ll_emotion_dashboard.setVisibility(View.VISIBLE);
				iv_emoji.setImageResource(R.drawable.btn_insert_keyboard);
			}else if (ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
				ll_emotion_dashboard.setVisibility(View.GONE);
				iv_emoji.setImageResource(R.drawable.btn_insert_emotion);
			}
			break;
		case R.id.iv_at:
			break;
		default:
			break;
		}
	}
	
	private void sendStatus() {
		String statusContent = et_write_status.getText().toString();
		if(TextUtils.isEmpty(statusContent)) {
			showToast("微博内容不能为空");
			return;
		}
		String imgFilePath = null;
		if(imgUris.size() > 0) {
			Uri uri = imgUris.get(0);
			imgFilePath = ImageUtils.getImageAbsolutePath(this, uri);
		}
		long retweetedStatusId = cardStatus == null ? -1 : cardStatus.getId();
		progressDialog.show();
		api.sendStatus(statusContent, imgFilePath, retweetedStatusId, new SimpleRequestListener(this, progressDialog) {
			@Override
			public void onComplete(String response) {
				super.onComplete(response);
				showToast("发送成功");
				finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Object itemAdapter = parent.getAdapter();
		if(itemAdapter instanceof WriteStatusGridImgsAdapter) {
			if(position == statusImgsAdapter.getCount()-1) {
				ImageUtils.showImagePickDialog(this);
			}
		}
		if(itemAdapter instanceof EmotionGvAdapter) {
			EmotionGvAdapter emotionGvAdapter = (EmotionGvAdapter) itemAdapter;
			if(position == emotionGvAdapter.getCount() - 1) {
				et_write_status.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			}else {
				String emotionName = emotionGvAdapter.getItem(position);
				StringBuilder sb = new StringBuilder();
				sb.append(et_write_status.getText());
				int curPosition = et_write_status.getSelectionStart();
				sb.insert(curPosition, emotionName);
				et_write_status.setText(StringUtils.getWeiboContent(this, et_write_status, sb.toString()));
				et_write_status.setSelection(curPosition + emotionName.length());
			}
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ImageUtils.GET_IMAGE_FROM_PHONE:
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			Uri imageUri = data.getData();
			imgUris.add(imageUri);
			updateImgs();
			break;
		case ImageUtils.GET_IMAGE_BY_CAMERA:
			if (resultCode == RESULT_CANCELED) {
				ImageUtils.delateImageUri(this,ImageUtils.imageUriFromCamera);
			}else {
				Uri imageUriCamera = ImageUtils.imageUriFromCamera;
				imgUris.add(imageUriCamera);
				updateImgs();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
			ll_emotion_dashboard.setVisibility(View.GONE);
			iv_emoji.setImageResource(R.drawable.btn_insert_emotion);
		} else {
			finish();
		}
	}
}
