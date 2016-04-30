package wang.loveweibo.fragment;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.utils.LogUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import wang.loveweibo.BaseFragment;
import wang.loveweibo.R;
import wang.loveweibo.adapter.StatusAdapter;
import wang.loveweibo.api.LoveWeiboApi;
import wang.loveweibo.api.SimpleRequestListener;
import wang.loveweibo.entity.Status;
import wang.loveweibo.entity.response.StatusTimeLineResponse;
import wang.loveweibo.utils.Logger;
import wang.loveweibo.utils.TitleBuilder;

public class HomeFragment extends BaseFragment {
	
	private View view;
	private PullToRefreshListView plv_home;
	private StatusAdapter statusAdapter;
	private List<Status> statuses = new ArrayList<Status>();
	private int curPage = 1;
	private View footView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		addFootView();
		loadData(1);
		return view;
	}
	private void initView() {
		view = View.inflate(activity, R.layout.frag_home, null);
		new TitleBuilder(view).setImageLeftRes(R.drawable.com_sina_weibo_sdk_login_button_with_original_logo).setTitle("Home").setImageRightRes(R.drawable.com_sina_weibo_sdk_login_button_with_frame_logo);
		plv_home = (PullToRefreshListView) view.findViewById(R.id.lv_home);
		statusAdapter = new StatusAdapter(activity, statuses);
		plv_home.setAdapter(statusAdapter);
		view.findViewById(R.id.titlebar_tv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				plv_home.getRefreshableView().setSelection(1);
			}
		});
		plv_home.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				loadData(1);
			}
		});
		plv_home.setOnLastItemVisibleListener (new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadData(curPage + 1);
			}
		});
		footView = View.inflate(activity, R.layout.footview_loading, null);
	}
	private void loadData(final int page) {
		LoveWeiboApi api = new LoveWeiboApi(activity);
		api.statusesHome_timeline(page, new SimpleRequestListener(activity, null) {
			@Override
			public void onComplete(String response) {
				super.onComplete(response);
				if(page == 1) {
					statuses.clear();
				}
				curPage = page;
				addData(new Gson().fromJson(response, StatusTimeLineResponse.class));
			}
			@Override
			public void onAllDone() {
				super.onAllDone();
				plv_home.onRefreshComplete();
			}
		});
	}
	private void addData(StatusTimeLineResponse resStatus) {
		for(Status status : resStatus.getStatuses()) {
			if(!statuses.contains(status)) {
				statuses.add(status);
			}
		}
		Logger.showLog("jay", statuses.size()+"");
		Logger.showLog("jay", resStatus.getTotal_number()+"");
		statusAdapter.notifyDataSetChanged();
		if(statuses.size() < resStatus.getTotal_number()) {
			addFootView();
		}else {
			removeFootView();
		}
	}
	private void removeFootView() {
		ListView lv = plv_home.getRefreshableView();
		if(lv.getFooterViewsCount() > 1) {
			lv.removeFooterView(footView);
		}
	}
	private void addFootView() {
		ListView lv = plv_home.getRefreshableView();
		if(lv.getFooterViewsCount() == 1) {
			lv.addFooterView(footView);
		}
	}
}
