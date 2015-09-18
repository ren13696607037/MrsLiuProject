package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.JishiScheuleTime;
import com.techfly.liutaitai.model.mall.bean.TimePoints;
import com.techfly.liutaitai.util.view.GridViewForScrollView;

public class JishiScheuleTimeAdapter extends BaseAdapter {
    private List<JishiScheuleTime> mList;
    private List<TimePoints> mListPoints;
    private Context mContext;
    public JishiScheuleTimeAdapter(Context context,List<JishiScheuleTime> list,List<TimePoints> time){
        mList = list;   
        mContext = context;
        mListPoints = time;
      }
      
    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):new JishiScheuleTime();
    }

    @Override
    public long getItemId(int arg0) {
     
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if(mList.size()==0){
            return convertView;
        }
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_jishi_time, null);
            holder.time = (GridViewForScrollView) convertView.findViewById(R.id.home_grid);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.date.setText(mList.get(position).getmDate());
        holder.time.setAdapter(new JishiScheuleSubTimeAdapter(mContext,mList.get(position).getList(),mListPoints));
       return convertView;
    }
    class ViewHolder{
        private GridViewForScrollView time;
        private TextView date;
    }
}
