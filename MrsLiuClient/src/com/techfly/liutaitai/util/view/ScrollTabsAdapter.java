package com.techfly.liutaitai.util.view;


import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techfly.liutaitai.R;

public class ScrollTabsAdapter extends TabAdapter {
	private Activity activity;
	private int mIndex;
	DisplayMetrics dm;
	
	public ScrollTabsAdapter(Activity activity, int index) {
		super();
		this.activity=activity;
		this.mIndex = index;
		dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
	}


	@Override
	public View getView(int position) {
		LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout=(LinearLayout) inflater.inflate(R.layout.tabs, null);
 
		layout.setMinimumWidth(dm.widthPixels/mIndex);
		TextView textView=(TextView) layout.findViewById(R.id.text);
		textView.setText(tabsList.get(position));
		TextView line=(TextView) layout.findViewById(R.id.tab_line);
		if(position == mIndex - 1){
			line.setVisibility(View.INVISIBLE);
		}
		return layout;
	}

}
