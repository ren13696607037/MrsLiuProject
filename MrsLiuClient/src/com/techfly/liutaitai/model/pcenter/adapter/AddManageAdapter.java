package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.model.pcenter.fragment.AddressManageFragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddManageAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<AddressManage> mList;
	private AddressManageFragment mFragment;

	public interface OnDeleteListener {
		void onDelete();
	}

	private OnDeleteListener mListener;

	public void setListener(OnDeleteListener clickListener) {
		mListener = clickListener;
	}

	public OnDeleteListener getListener() {
		return mListener;
	}

	public AddManageAdapter(Context context, ArrayList<AddressManage> list,
			AddressManageFragment fragment) {
		this.mContext = context;
		this.mList = list;
		this.mFragment = fragment;
	}

	public void updateList(ArrayList<AddressManage> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList != null ? mList.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return mList != null ? mList.get(arg0) : new AddressManage();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.item_address,
					null);
			holder.mTvAdd = (TextView) arg1.findViewById(R.id.iaddm_address);
			holder.mTvChange = (TextView) arg1.findViewById(R.id.iaddm_change);
			holder.mTvDelete = (TextView) arg1.findViewById(R.id.iaddm_delete);
			holder.mTvName = (TextView) arg1.findViewById(R.id.iaddm_name);
			holder.mTvPhone = (TextView) arg1.findViewById(R.id.iaddm_phone);
			holder.mIvSelect = (ImageView) arg1.findViewById(R.id.iaddm_select);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		String address = holder.mTvAdd.getText().toString();
		if (!TextUtils.isEmpty(mList.get(arg0).getmCity())
				&& !"null".equals(mList.get(arg0).getmCity())) {
			holder.mTvAdd.setText(mContext.getString(R.string.address_text,
					mList.get(arg0).getmCity()));
		}
		if (!TextUtils.isEmpty(mList.get(arg0).getmDetail())
				&& !"null".equals(mList.get(arg0).getmDetail())) {
			address = holder.mTvAdd.getText().toString();
			holder.mTvAdd.setText(address + "     " + mList.get(arg0).getmDetail());
		}
		holder.mTvName.setText(mList.get(arg0).getmName());
		holder.mTvPhone.setText(mList.get(arg0).getmPhone());
		holder.mTvChange
				.setOnClickListener(new AddManageClick(mContext, mList
						.get(arg0).getmId() + "", "", this, mFragment, mList
						.get(arg0)));
		holder.mTvDelete.setOnClickListener(new AddManageClick(mContext, mList
				.get(arg0).getmId() + "", mList.get(arg0).isDefault() + "",
				this, mFragment, mList.get(arg0)));
		holder.mIvSelect
				.setBackgroundResource(mList.get(arg0).isDefault() ? R.drawable.address_default
						: R.drawable.address_undefault);
		return arg1;
	}

	class ViewHolder {
		private TextView mTvName;
		private TextView mTvPhone;
		private TextView mTvAdd;
		private TextView mTvChange;
		private TextView mTvDelete;
		private ImageView mIvSelect;
	}

}
