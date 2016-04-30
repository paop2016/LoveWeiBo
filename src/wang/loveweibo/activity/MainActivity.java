package wang.loveweibo.activity;




import wang.loveweibo.R;
import wang.loveweibo.fragment.FragmentController;
import wang.loveweibo.utils.Logger;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements OnClickListener, OnCheckedChangeListener{
	private FragmentController controller;
	RadioGroup rg_tab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		controller = FragmentController.getInstance(this, R.id.fl_content);
		initView();
	}

	private void initView() {
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		rg_tab.setOnCheckedChangeListener(this);
		findViewById(R.id.iv_add).setOnClickListener(this);
		controller.showFragment(0);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_home:
			controller.showFragment(0);
			break;
		case R.id.rb_message:
			controller.showFragment(1);
			break;
		case R.id.rb_search:
			controller.showFragment(2);
			break;
		case R.id.rb_user:
			controller.showFragment(3);
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_add:
			Intent intent = new Intent(MainActivity.this, WriteStatusActivity.class);
			startActivity(intent);


		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
//		moveTaskToBack(true); 
//		android.os.Process.killProcess(android.os.Process.myPid());
		finish();
		System.exit(0);
	}
}
