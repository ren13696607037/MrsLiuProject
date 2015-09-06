package com.techfly.liutaitai.util.view;


import java.util.ArrayList;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.pcenter.bean.Area;
import com.techfly.liutaitai.util.ManagerListener;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowDialog extends Dialog{
	private Context mContext;
	private ArrayList<Area> mList;
	private ListView mListView;

	public ShowDialog(Context context, int theme) {
		super(context, R.style.loading_dialog);
	}
	public ShowDialog(Context context,ArrayList<Area> list){
		super(context);
		this.mContext=context;
		this.mList=list;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window theWindow = getWindow();
		theWindow.setGravity(Gravity.CENTER);
		setContentView(R.layout.view_show);
		WindowManager.LayoutParams lp = theWindow.getAttributes();
		theWindow.setAttributes(lp);
		theWindow.setBackgroundDrawable(new BitmapDrawable());
		theWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		initView();
	}
	private void initView(){
		mListView=(ListView) findViewById(R.id.show_list);
		mListView.setAdapter(new ShowAdapter(mContext, mList));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Area area = (Area) arg0.getAdapter().getItem(arg2);
				ManagerListener.newManagerListener().notifyUpdateList(area);
			}
		});
	}
	class ShowAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<Area> mAreas;
		public ShowAdapter(Context context, ArrayList<Area> list){
			this.mContext = context;
			this.mAreas = list;
		}

		@Override
		public int getCount() {
			return mAreas != null ? mAreas.size() : 0;
		}

		@Override
		public Object getItem(int arg0) {
			return mAreas != null ? mAreas.get(arg0) : new Area();
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder;
			if(arg1 == null){
				holder = new ViewHolder();
				arg1 = LayoutInflater.from(mContext).inflate(R.layout.item_dialog, null);
				holder.textView = (TextView) arg1.findViewById(R.id.idialog_text);
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}
			holder.textView.setText(mAreas.get(arg0).getmName());
			return arg1;
		}
		
		class ViewHolder{
			private TextView textView;
		}
		
	}

}

