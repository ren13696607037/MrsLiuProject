package com.hylsmart.yihui.model.home.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.dao.DataBaseInfo;
import com.hylsmart.yihui.dao.DataHelper;
import com.hylsmart.yihui.dao.Persistence;
import com.hylsmart.yihui.dao.SearchHistoryDbAdapter;
import com.hylsmart.yihui.model.home.adapter.HistoryAdapter;
import com.hylsmart.yihui.model.home.bean.SearchHistory;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.util.fragment.CommonFragment;

public class RecentSearchFragment extends CommonFragment {
	private EditText mSearchEdit;
	private TextView mSearchBtn;
	private TextView mHintText;
	private ListView mHistoryView;
	private TextView clearText;
	private String mKeyword;
	private ArrayList<Product> mData = new ArrayList<Product>();

	private DataHelper mhelper;
	private SearchHistoryDbAdapter mDbAdapter;
	private List<Persistence> mHistoryList = new ArrayList<Persistence>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mhelper = new DataHelper();
		mDbAdapter = new SearchHistoryDbAdapter(getActivity());
		mhelper.setmAdapter(mDbAdapter);
		mHistoryList = mDbAdapter.getDataList(
				DataBaseInfo.History.COLUMN_NAME_TYPE + "=?",
				new String[] { String.valueOf(1) });
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recent_search, container,
				false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mSearchEdit = (EditText) view.findViewById(R.id.search_edit);
		mSearchEdit.setHint(R.string.search_hint_text);

		mSearchBtn = (TextView) view.findViewById(R.id.search_btn);
		mSearchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// fly.f.ren add for fixed mutil request begin
				if (TextUtils.isEmpty(mSearchEdit.getText().toString())
						|| mSearchEdit.getText().toString().equals(mKeyword)) {
					return;
				}
				// fly.f.ren add for fixed mutil request end
				mKeyword = mSearchEdit.getText().toString();
				startReqTask(RecentSearchFragment.this);
			}
		});

		// mTagLayout = (LinearLayout)
		// view.findViewById(R.id.search_tag_layout);
		// initTag(mTagLayout,mTags);

		mHistoryView = (ListView) view.findViewById(R.id.search_history_list);
		clearText = new TextView(getActivity());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		clearText.setPadding(10, 12, 10, 12);
		clearText.setTextColor(getResources().getColor(
				android.R.color.darker_gray));
		clearText.setTextSize(16);
		clearText.setGravity(Gravity.CENTER);
		clearText.setLayoutParams(params);
		clearText.setText(getResources().getString(
				R.string.clear_search_history));
		clearText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Persistence p : mHistoryList) {
					SearchHistory history = (SearchHistory) p;
					mhelper.deleteData(String.valueOf(history.getId()));
				}
				mHistoryList.clear();
				mHistoryView.setVisibility(View.GONE);
			}
		});
		mHistoryView.addFooterView(clearText);
		if (mHistoryList.size() != 0 && mHistoryList != null) {
			mHistoryView.setAdapter(new HistoryAdapter(getActivity(),
					mHistoryList));
		}
		mHistoryView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Persistence p = (Persistence) parent.getAdapter().getItem(
						position);
				SearchHistory history = (SearchHistory) p;
				mSearchEdit.setText(history.getName());
				mKeyword = history.getName();
				startReqTask(RecentSearchFragment.this);
			}
		});
		mHintText = (TextView) view.findViewById(R.id.no_data_prompt);
		mHintText.setText(R.string.no_search_data);
	}

	@Override
	public void requestData() {

	}

}
