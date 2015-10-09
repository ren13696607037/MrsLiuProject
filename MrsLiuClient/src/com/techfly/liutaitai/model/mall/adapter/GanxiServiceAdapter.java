package com.techfly.liutaitai.model.mall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.util.ImageLoaderUtil;

public class GanxiServiceAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Product> mList;
    private int mDisplayStyle;
    public GanxiServiceAdapter(Context context,ArrayList<Product> list,int displayStyle){
        this.mContext=context;
        this.mList=list;
        mDisplayStyle = displayStyle;
    }
    public void updateList(ArrayList<Product> list){
        this.mList=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):new Product();
    }

    @Override
    public long getItemId(int position) {
            return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(mList.size()==0){
            return convertView;
        }
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_service, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.service_img);
            holder.productName= (TextView) convertView.findViewById(R.id.service_name);
            holder.productType= (TextView) convertView.findViewById(R.id.service_person);
            holder.productMarketPrice = (TextView) convertView.findViewById(R.id.service_price);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
      
        ImageLoader.getInstance().displayImage(mList.get(position).getmImg(), holder.imageView,ImageLoaderUtil.mHallIconLoaderOptions);
        holder.productName.setText(mList.get(position).getmName());
        holder.productType.setText(mList.get(position).getmSale()+"人体验");
        holder.productMarketPrice.setText("￥"+mList.get(position).getmMarketPrice()+"元"+"/"+mList.get(position).getmUnit());
       
       
        return convertView;
    }
    class ViewHolder{
        private TextView productName;
        private ImageView imageView;
        private TextView productType;
        private TextView productMarketPrice;
       
    }
    public void updateListView(ArrayList<Product> listItems) {
        mList = listItems;
        notifyDataSetChanged();
    }

}
