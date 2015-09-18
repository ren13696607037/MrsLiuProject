package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.adapter.TimesAdapter.ViewHolder;
import com.techfly.liutaitai.model.mall.bean.TimeBean;
import com.techfly.liutaitai.model.mall.bean.TimePoints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JishiScheuleTimeAdapter extends BaseAdapter {
    private List<TimeBean> mList;
    private List<TimePoints> mListPoints;
    private Context mContext;
    public JishiScheuleTimeAdapter(Context context,List<TimeBean> list,List<TimePoints> time){
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
        return mList!=null?mList.get(position):new TimeBean();
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
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_base_text2, null);
            holder.time = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        if(position%11==0){
        	   holder.time.setBackgroundDrawable(null);
//        	   holder.time.setTextSize(10);
        }else{
        	if(mListPoints!=null){
        		for(TimePoints points:mListPoints){
        			      if(mList.get(position).getTimeMill()==points.getmTimeMills()){
        			    	  if(points.ismIsWholeHours()){
        			    		   holder.time.setBackgroundResource(R.drawable.jishi_time_bg2); 
        			    	  }else{
        			    		   holder.time.setBackgroundResource(R.drawable.jishi_time_bg2); 
        			    	  }
        			      }
        		}
        	}else{
        		   holder.time.setBackgroundResource(R.drawable.jishi_time_bg2);
        	}
        }
        holder.time.setText(mList.get(position).getTime());
       return convertView;
    }
    class ViewHolder{
        private TextView time;
    }
}
