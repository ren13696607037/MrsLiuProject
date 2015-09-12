package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.adapter.ServiceAdapter.ViewHolder;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.model.mall.bean.TimeBean;

public class DateAdapter extends BaseAdapter{
    private List<TimeBean> mList;
    private Context mContext;
    public DateAdapter(Context context,List<TimeBean> list){
        mList = list;   
        mContext = context;
      }
      
    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):new TimeBean();
    }

    @Override
    public long getItemId(int arg0) {
     
        return 0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if(mList.size()==0){
            return convertView;
        }
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_base_text, null);
            holder.time = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        if(mList.get(arg0).isMisSelect()){
            holder.time.setTextColor(mContext.getResources().getColor(R.color.bg_header));
        }else{
            holder.time.setTextColor(mContext.getResources().getColor(R.color.TextColorBLACK));
        }
        holder.time.setText(mList.get(arg0).getTime());
       return convertView;
    }
    class ViewHolder{
        private TextView time;
    }

}
