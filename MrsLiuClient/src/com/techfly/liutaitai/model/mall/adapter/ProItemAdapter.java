package com.techfly.liutaitai.model.mall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;

public class ProItemAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Product> mList;
    private int mDisplayStyle;
    public ProItemAdapter(Context context,ArrayList<Product> list,int displayStyle){
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
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_product, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.product_icon);
            holder.productName = (TextView) convertView.findViewById(R.id.product_name);
            holder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
            holder.productMarketPrice = (TextView) convertView.findViewById(R.id.product_market_price);
            holder.productMarketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);//下划线
            holder.productSold = (TextView) convertView.findViewById(R.id.product_sale);
            holder.proCheckBox = (CheckBox) convertView.findViewById(R.id.product_checkbox);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.proCheckBox.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View view) {
                mList.get(position).setmIsCheck(!mList.get(position).ismIsCheck());
                
            }
        });
        ImageLoader.getInstance().displayImage(mList.get(position).getmImg(), holder.imageView,ImageLoaderUtil.mHallIconLoaderOptions);
        holder.productName.setText(mList.get(position).getmName());
        holder.productPrice.setText("￥"+mList.get(position).getmPrice()+"元");
        holder.productMarketPrice.setText("￥"+mList.get(position).getmMarketPrice()+"元");
        if(mList.get(position).ismEditable()){
            AppLog.Logd("Fly", "123456");
            holder. proCheckBox.setVisibility(View.VISIBLE);
            holder. proCheckBox.setChecked(mList.get(position).ismIsCheck());
        }else{
            holder. proCheckBox.setVisibility(View.GONE);
        }
        if(mDisplayStyle==Constant.COLLECT_PRO_ITEM_STYLE){
            holder.productSold.setText(mContext.getString(R.string.sale_prefix_text,mList.get(position).getmSale()));
        }else{
            holder.productSold.setText(mContext.getString(R.string.sale_prefix_text2,mList.get(position).getmSale()));
        }
        return convertView;
    }
    class ViewHolder{
        private TextView productName;
        private ImageView imageView;
        private TextView productPrice;
        private TextView productMarketPrice;
        private TextView productSold;
        private CheckBox proCheckBox;
    }
    public void updateListView(ArrayList<Product> listItems) {
        mList = listItems;
        notifyDataSetChanged();
    }
}
