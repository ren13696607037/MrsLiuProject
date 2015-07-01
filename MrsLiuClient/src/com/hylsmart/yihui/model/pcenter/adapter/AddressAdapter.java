package com.hylsmart.yihui.model.pcenter.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.pcenter.activities.ChangeAddressActivity;
import com.hylsmart.yihui.model.pcenter.bean.Address;
import com.hylsmart.yihui.util.AppLog;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.IntentBundleKey;
import com.hylsmart.yihui.util.ManagerListener;

public class AddressAdapter extends BaseAdapter {
    private Fragment mContext;
    private ArrayList<Address> mList;
    private boolean isMulChoice;
    private HashMap<Integer, View> mView;
    public HashMap<Integer, Integer> visiblecheck;// 用来记录是否显示checkBox
    public HashMap<Integer, Boolean> ischeck;
    private Address mAddress;
    private int mExtra;
    
    public AddressAdapter(Fragment context, ArrayList<Address> list, boolean b, int extra) {
        this.mContext = context;
        this.mList = list;
        this.isMulChoice = b;
        mExtra = extra;
        mView = new HashMap<Integer, View>();
        visiblecheck = new HashMap<Integer, Integer>();
        ischeck = new HashMap<Integer, Boolean>();
        if (isMulChoice) {
            for (int i = 0; i < mList.size(); i++) {
                ischeck.put(i, false);
                visiblecheck.put(i, CheckBox.VISIBLE);
            }
        } else {
            for (int i = 0; i < mList.size(); i++) {
                ischeck.put(i, false);
                visiblecheck.put(i, CheckBox.GONE);
            }
        }
    }
    
    public void updateList(ArrayList<Address> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }
    
    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : new Address();
    }
    
    @Override
    public long getItemId(int position) {
        return 0;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mAddress = mList.get(position);
        final ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.item_address, null);
            mHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.iaddress_check);
            mHolder.mTvAddress = (TextView) convertView.findViewById(R.id.iaddress_address);
            mHolder.mTvName = (TextView) convertView.findViewById(R.id.iaddress_people);
            mHolder.mTvPhone = (TextView) convertView.findViewById(R.id.iaddress_phone);
            mHolder.mCbDelete = (CheckBox) convertView.findViewById(R.id.iaddress_delete);
            mHolder.mTvUpdate = (TextView) convertView.findViewById(R.id.iaddress_modify);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mCheckBox.setChecked(mAddress.ismIsSelect());
        mHolder.mTvAddress.setText(mAddress.getmAddress() + "/" + mAddress.getmDetail());
        mHolder.mTvName.setText(mAddress.getmName());
        mHolder.mTvPhone.setText(mAddress.getmPhone());
        if (ischeck.size() > 0) {
            mHolder.mCbDelete.setChecked(ischeck.get(position));
            mHolder.mCbDelete.setVisibility(visiblecheck.get(position));
        }
        if (isMulChoice) {
            mHolder.mCheckBox.setVisibility(View.GONE);
        } else {
            mHolder.mCheckBox.setVisibility(View.VISIBLE);
        }
        mHolder.mCbDelete.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                AppLog.Loge("xll", mList.get(position).getmId());
                if (mHolder.mCbDelete.isChecked()) {
                    ManagerListener.newManagerListener().notifySelectListener(1, mList.get(position).getmId());
                } else {
                    ManagerListener.newManagerListener().notifySelectListener(0, mList.get(position).getmId());
                }
            }
        });
        
        mHolder.mTvUpdate.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext.getActivity(), ChangeAddressActivity.class);
                intent.putExtra(IntentBundleKey.ADDRESS, mAddress);
                mContext.startActivityForResult(intent, Constant.ADDRESS_INTENT);
            }
        });
        
        if (mExtra != -1) {
            mHolder.mTvUpdate.setVisibility(View.VISIBLE);
        } else {
            mHolder.mTvUpdate.setVisibility(View.GONE);
        }
        
        mHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ManagerListener.newManagerListener().notifyDefaultListener(mList.get(position).ismIsSelect(), mList.get(position).getmId());
            }
        });
        mView.put(position, convertView);
        return convertView;
    }
    
    public class ViewHolder {
        private TextView mTvName;
        private TextView mTvPhone;
        private TextView mTvAddress;
        public CheckBox mCheckBox;
        public CheckBox mCbDelete;
        private TextView mTvUpdate;
    }
    
}
