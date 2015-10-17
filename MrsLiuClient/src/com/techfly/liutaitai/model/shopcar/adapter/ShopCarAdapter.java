package com.techfly.liutaitai.model.shopcar.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.view.ShopCartChangeView;

public class ShopCarAdapter extends BaseAdapter{
    public interface EditCallBack{
        void onEdit();
    }
    private Context mContext;
    private List<Product> mList;
    private EditCallBack mEditCallBack;
    public ShopCarAdapter(Context context,List<Product> list,EditCallBack callback){
        this.mContext=context;
        this.mList=list;
        mEditCallBack =callback;
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
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_shop_car, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.product_img);
            holder.productCheckBox = (CheckBox) convertView.findViewById(R.id.product_checkbox);
            holder.productName = (TextView) convertView.findViewById(R.id.product_name);
            holder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
            holder.productUpdateView = (ShopCartChangeView) convertView.findViewById(R.id.product_update_view);
            holder.productEdit =(TextView) convertView.findViewById(R.id.product_edit);
            holder.productUnit=(TextView) convertView.findViewById(R.id.unit);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(mList.get(position).getmImg(), holder.imageView,ImageLoaderUtil.mHallIconLoaderOptions);
        if(mList.get(position).ismEditable()){
            holder.productEdit.setVisibility(View.GONE);
        }else{
            holder.productEdit.setVisibility(View.VISIBLE);
        }
        holder.productEdit.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                mList.get(position).setmEditable(true);
                notifyDataSetChanged();
                mEditCallBack.onEdit();
            }
        });
        holder.productCheckBox.setChecked(mList.get(position).ismIsCheck());
        holder.productName.setText(mList.get(position).getmName());
        holder.productPrice.setText("￥"+mList.get(position).getmPrice());
        holder.productUpdateView.onAttachView(mList.get(position));
        holder.productUnit.setText("1"+mList.get(position).getmUnit());
        holder.productCheckBox.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                mList.get(position).setmIsCheck(!mList.get(position).ismIsCheck());
                OnShopCarLisManager.getShopCarLisManager().onNotifyShopCarChange(new Bundle());
            }
        });
        return convertView;
    }
    class ViewHolder{
        private CheckBox productCheckBox;
        private TextView productName;
        private ImageView imageView;
        private TextView productPrice;
        private TextView productUnit;
        private ShopCartChangeView productUpdateView;
        private TextView productEdit;
    }
    public void updateListView(List<Product> listItems) {
        mList = listItems;
        notifyDataSetChanged();
    }
}
