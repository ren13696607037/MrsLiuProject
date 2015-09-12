package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Jishi;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.util.ImageLoaderUtil;

public class JishiAdapter extends BaseAdapter{
    private List<Jishi> mList;
    private Context mContext;
    public JishiAdapter(Context context,List<Jishi> list){
      mList = list;   
      mContext = context;
    }
    
    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):new Service();
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
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_jishi, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            holder.personNum = (TextView) convertView.findViewById(R.id.service_times);
            holder.serviceName = (TextView) convertView.findViewById(R.id.name);
            holder.mSexTv =(TextView) convertView.findViewById(R.id.sex);
            holder.mBar= (RatingBar) convertView.findViewById(R.id.rate_bar);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(mList.get(position).getmImg(), holder.imageView,ImageLoaderUtil.mHallIconLoaderOptions);
        holder.personNum.setText("服务:"+mList.get(position).getmServiceTime()+"次");
        holder.mBar.setRating(mList.get(position).getmRating());
        holder.mSexTv.setText("性别:"+mList.get(position).getmSex());
        holder.serviceName.setText(mList.get(position).getmName());
        return convertView;
        
    }

    public List<Jishi> getmList() {
        return mList;
    }

    public void setmList(List<Jishi> mList) {
        this.mList = mList;
    }
    class ViewHolder{
        private TextView serviceName;
        private ImageView imageView;
        private TextView personNum;
        private TextView servicePrice;
        private RatingBar mBar;
        private TextView mSexTv;
       
    }
    }
