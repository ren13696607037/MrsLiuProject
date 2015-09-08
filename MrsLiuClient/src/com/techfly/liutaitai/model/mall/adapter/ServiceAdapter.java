package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Service;
import com.techfly.liutaitai.util.ImageLoaderUtil;

public class ServiceAdapter extends BaseAdapter{
    private List<Service> mList;
    private Context mContext;
    public ServiceAdapter(Context context,List<Service> list){
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
        // TODO Auto-generated method stub
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
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_service, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.service_img);
            holder.personNum = (TextView) convertView.findViewById(R.id.service_person);
            holder.serviceName = (TextView) convertView.findViewById(R.id.service_name);
            holder.servicePrice = (TextView) convertView.findViewById(R.id.service_price);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
     
        ImageLoader.getInstance().displayImage(mList.get(position).getmServiceIcon(), holder.imageView,ImageLoaderUtil.mHallIconLoaderOptions);
        
        holder.personNum.setText(mList.get(position).getmServicePerson()+"人体验");
        
        holder.serviceName.setText(mList.get(position).getmServiceName());
        
        holder.servicePrice.setText("￥"+mList.get(position).getmServicePrice());
        
        return convertView;
        
    }

    public List<Service> getmList() {
        return mList;
    }

    public void setmList(List<Service> mList) {
        this.mList = mList;
    }
    class ViewHolder{
        private TextView serviceName;
        private ImageView imageView;
        private TextView personNum;
        private TextView servicePrice;
       
    }
}
