package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.activities.MyBalanceActivity;
import com.techfly.liutaitai.model.pcenter.adapter.BalanceAdapter;
import com.techfly.liutaitai.model.pcenter.bean.Balance;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.XListView;

public class MyBalanceFragment extends CommonFragment {
	private MyBalanceActivity mActivity;
	private TextView mPrice;
	private XListView mListView;
	private BalanceAdapter mAdapter;
	private ArrayList<Balance> mList = new ArrayList<Balance>();
	private Handler mBalanceHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MyBalanceActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        startReqTask(this);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mybalance,
                container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        onInitView(view);
    }
    private void onInitView(View view){
    	mPrice = (TextView) view.findViewById(R.id.mybalance_price);
    	mListView = (XListView) view.findViewById(R.id.mybalance_list);
    	mAdapter = new BalanceAdapter(mActivity, mList);
    	mListView.setAdapter(mAdapter);
    }

	@Override
	public void requestData() {
		
	}

}
