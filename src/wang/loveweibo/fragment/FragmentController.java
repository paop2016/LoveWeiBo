package wang.loveweibo.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentController {
	private int containerId;
	private FragmentManager fm;
	private static FragmentController controller;
	private ArrayList<Fragment> fragments;
	private FragmentController(FragmentActivity activity, int containerId){
		this.containerId = containerId;
		fm = activity.getSupportFragmentManager();
		initFragment();
	};
	private void initFragment() {
		// TODO Auto-generated method stub
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new MessageFragment());
		fragments.add(new SearchFragment());
		fragments.add(new UserFragment());
		
		FragmentTransaction ft = fm.beginTransaction();
		for (Fragment fragment : fragments) {
			ft.add(containerId, fragment);
		}
		ft.commit();
	}
	public void hideFragment() {
		FragmentTransaction ft = fm.beginTransaction();
		for(Fragment fragment : fragments) {
			if(fragment != null) {
				ft.hide(fragment);
			}
		}
		ft.commit();
	}
	public void showFragment(int position) {
		hideFragment();
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(fragments.get(position));
		ft.commit();
	}
	public static FragmentController getInstance(FragmentActivity activity, int containerId) {
		if(controller == null) {
			controller = new FragmentController(activity, containerId);
		}
		return controller;
	}
	//为什么有这个方法
	public Fragment getFragment(int position) {
		return fragments.get(position);
	}
}
