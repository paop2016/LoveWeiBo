package wang.loveweibo;

import wang.loveweibo.activity.MainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	protected MainActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		activity = (MainActivity) getActivity();
	}
	protected void intent2Activity(Class<? extends Activity> tarActivity) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(activity, tarActivity);
		startActivity(intent);
	}
}
