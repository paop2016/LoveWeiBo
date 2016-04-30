package wang.loveweibo.activity;

import wang.loveweibo.BaseActivity;
import wang.loveweibo.R;
import wang.loveweibo.api.SimpleRequestListener;
import wang.loveweibo.entity.Status;
import wang.loveweibo.utils.StringUtils;
import wang.loveweibo.utils.TitleBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class WriteCommentActivity extends BaseActivity {
	EditText et_write_status;
	ImageView iv_at;
	ImageView iv_topic;
	ImageView iv_image;
	ImageView iv_add;
	ImageView iv_emoji;
	Status status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_status);
		
		status = (Status) getIntent().getSerializableExtra("status");
		init();
	}
	private void init() {
		new TitleBuilder(this).setTextLeft("取消").setTextRight("发送")
		.setTitle("发评论").setOnClickListenerLeft(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		}).setOnClickListenerRight(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sentComment();
			}
		});
		et_write_status = (EditText) findViewById(R.id.et_write_status);
		iv_at = (ImageView) findViewById(R.id.iv_at);
		iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		iv_topic = (ImageView) findViewById(R.id.iv_topic);
	}
	protected void sentComment() {
		String comment = et_write_status.getText().toString();
		if(TextUtils.isEmpty(comment)) {
			showToast("评论内容不能为空");
			return;
		}
		api.comentsCreate(status.getId(), comment, new SimpleRequestListener(this, null) {
			@Override
			public void onComplete(String response) {
				super.onComplete(response);
				showToast("评论成功");
				Intent intent = new Intent();
				intent.putExtra("sentCommentSuccess", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
}
