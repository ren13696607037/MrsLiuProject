package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.SortRule;

public class PopUpAdapter extends BaseAdapter{
    private List<SortRule> mItemList;
    private Context mContext;
    private int duration=500;
    private Animation push_left_in,push_right_in;  
    private Animation slide_top_to_bottom,slide_bottom_to_top;  
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mItemList==null?0:mItemList.size();
//        return 10;
    }
    public PopUpAdapter(Context context, List<SortRule> items) {
        this.mContext = context;
        this.mItemList = items;
//        push_left_in=AnimationUtils.loadAnimation(context, R.anim.push_left_in);  
//        push_right_in=AnimationUtils.loadAnimation(context, R.anim.push_right_in);  
//        slide_top_to_bottom=AnimationUtils.loadAnimation(context, R.anim.slide_top_to_bottom);  
//        slide_bottom_to_top=AnimationUtils.loadAnimation(context, R.anim.slide_bottom_to_top);  
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
      
        return mItemList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder  = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_popupwindow, null);
            holder.mName = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
//        slide_bottom_to_top.setDuration(duration); 
//        convertView.setAnimation(slide_bottom_to_top); 
        holder.mName.setText(mItemList.get(position).getmName());
//        holder.mBar.setRating(Float.parseFloat(mItemList.get(position).getmRatingBar()));
//        holder.mComment.setText(mContext.getString(R.string.comment_people,mItemList.get(position).getmComment()));
//        holder.mAddress.setText(mItemList.get(position).getmAddress());
//        ImageLoader.getInstance().displayImage(mItemList.get(position).getmIcon(),holder.mIcon,ImageLoaderUtil.mHallIconLoaderOptions);
        return convertView;
    }
    
    class ViewHolder {
     
        private TextView mName;
   
    }
}
