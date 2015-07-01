package com.techfly.liutaitai.model.pcenter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.model.pcenter.bean.City;
import com.techfly.liutaitai.model.pcenter.bean.Province;

public class CitysAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private ArrayList<Province> mList;
//	private OnChildTreeViewClickListener mTreeViewClickListener;// 点击子ExpandableListView子项的监听
	public CitysAdapter(Context context,ArrayList<Province> list){
		this.mContext=context;
		this.mList=list;
	}
	public void updateList(ArrayList<Province> list){
		this.mList=list;
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return mList!=null?mList.size():0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mList!=null&&mList.get(groupPosition).getmList()!=null?mList.get(groupPosition).getmList().size():1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mList!=null?mList.get(groupPosition):null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mList!=null?mList.get(groupPosition).getmList().get(childPosition):new City();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder holder;
		if(convertView==null){
			holder=new GroupHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_city_group, null);
			holder.mTvGroup=(TextView) convertView.findViewById(R.id.group_text);
			convertView.setTag(holder);
		}else{
			holder=(GroupHolder) convertView.getTag();
		}
		holder.mTvGroup.setText(mList.get(groupPosition).getmName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder;
		if(convertView==null){
			holder=new ChildHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_city_child, null);
			holder.mTvChild=(TextView) convertView.findViewById(R.id.child_text);
			convertView.setTag(holder);
		}else{
			holder=(ChildHolder) convertView.getTag();
		}
		if(mList.get(groupPosition).getmList()!=null){
			holder.mTvChild.setText(mList.get(groupPosition).getmList().get(childPosition).getmName());
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	class GroupHolder{
		private TextView mTvGroup;
	}
	class ChildHolder{
		private TextView mTvChild;
	}

}
