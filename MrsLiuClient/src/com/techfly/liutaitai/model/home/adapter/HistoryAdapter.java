package com.techfly.liutaitai.model.home.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.dao.Persistence;
import com.techfly.liutaitai.model.home.bean.SearchHistory;

public class HistoryAdapter extends BaseAdapter{
    private Context mContext;
    private List<Persistence> mList;
    public HistoryAdapter(Context context,List<Persistence> mHistoryList){
        this.mContext=context;
        this.mList=mHistoryList;
    }
    public void updateList(List<Persistence> list){
        this.mList=list;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):new SearchHistory();
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
        SearchHistory history = new SearchHistory();
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_search, null);
            holder.productName = (TextView) convertView.findViewById(R.id.product_name);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.productName.setText(history.getName());
        return convertView;
    }
    class ViewHolder{
        private TextView productName;
      
    }

}
