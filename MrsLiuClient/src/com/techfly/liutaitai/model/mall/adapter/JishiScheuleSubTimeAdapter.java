package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.adapter.JishiScheuleTimeAdapter.ViewHolder;
import com.techfly.liutaitai.model.mall.bean.TimeBean;
import com.techfly.liutaitai.model.mall.bean.TimePoints;

public class JishiScheuleSubTimeAdapter extends BaseAdapter {
    private List<TimeBean> mList;
    private List<TimePoints> mListPoints;
    private Context mContext;

    public JishiScheuleSubTimeAdapter(Context context, List<TimeBean> list,
            List<TimePoints> time) {
        mList = list;
        mContext = context;
        mListPoints = time;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : new TimeBean();
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (mList.size() == 0) {
            return convertView;
        }
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_base_text2, null);
            holder.time = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.time.setBackgroundResource(R.drawable.jishi_time_bg2);
        if (mListPoints != null) {
            for (TimePoints points : mListPoints) {
                if (mList.get(position).getTimeMill() == points.getmTimeMills()) {
                    if (points.ismIsWholeHours()) {
                        holder.time
                                .setBackgroundResource(R.drawable.jishi_time_bg1);
                        holder.time.setTextColor(mContext.getResources().getColor(android.R.color.white));
                    } else {
                        if(points.ismBeforeHalfJHours()){
                            holder.time
                            .setBackgroundResource(R.drawable.jishi_time_bg3);
                        }else{
                            holder.time
                            .setBackgroundResource(R.drawable.jishi_time_bg4);
                        }
                        
                    }
                }
            }
        } 
        holder.time.setText(mList.get(position).getTime());
        return convertView;
    }

    class ViewHolder {
        private TextView time;
    }
}
