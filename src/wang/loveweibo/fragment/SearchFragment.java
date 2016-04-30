package wang.loveweibo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import wang.loveweibo.BaseFragment;
import wang.loveweibo.R;
import wang.loveweibo.utils.TitleBuilder;

public class SearchFragment extends BaseFragment {
	
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = View.inflate(activity, R.layout.frag_search, null);
		new TitleBuilder(view).setTextLeft("Left").setTitle("Search").setTextRight("Right");
		return view;
	}
}
