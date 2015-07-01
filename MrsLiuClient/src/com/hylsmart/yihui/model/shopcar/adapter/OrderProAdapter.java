package com.hylsmart.yihui.model.shopcar.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OrderProAdapter extends BaseAdapter {

    private Context mContext;
    private List<Product> mList;
    public OrderProAdapter(Context context,List<Product> list){
        this.mContext=context;
        this.mList=list;
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
        if(mList==null){
            return 0;
        }
        String id = mList.get(position).getmId();
        if(!TextUtils.isEmpty(id)){
            return Integer.parseInt(id);
        }else{
            return 0;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(mList.size()==0){
            return convertView;
        }
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_order_product, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.product_icon);
            holder.productName = (TextView) convertView.findViewById(R.id.product_name);
            holder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
            holder.productCount = (TextView) convertView.findViewById(R.id.product_count);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(mList.get(position).getmImg(), holder.imageView,ImageLoaderUtil.mHallIconLoaderOptions);
        holder.productName.setText(mList.get(position).getmName());
        holder.productPrice.setText("￥"+mList.get(position).getmPrice());
        holder.productCount.setText("x"+mList.get(position).getmAmount());
        return convertView;
    }
    class ViewHolder{
        private TextView productName;
        private ImageView imageView;
        private TextView productPrice;
        private TextView productCount;
    }
    public void updateListView(List<Product> listItems) {
        mList = listItems;
        notifyDataSetChanged();
    }
}
