package com.techfly.liutaitai.util.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.techfly.liutaitai.MainActivity;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.view.CircleFlowIndicator;
import com.techfly.liutaitai.util.view.ViewFlow;

public class GuideFragment extends CommonFragment {
    private static final int GUIDES_PAGES = 3;
    private ViewFlow mViewFlow;
    private CircleFlowIndicator mIndic;
    private BaseAdapter mAdapter;
    private Integer[] COACHING_IDS={R.drawable.guide_page1,R.drawable.guide_page2,R.drawable.guide_page3};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_guide_layout, null);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        mAdapter = new ImageAdapter(getActivity());
        mViewFlow.setAdapter(mAdapter, 0);
        mIndic = (CircleFlowIndicator) view.findViewById(R.id.viewflowindic);
        mIndic.setVisibility(View.GONE);
        mViewFlow.setFlowIndicator(mIndic);
    }

    public class ImageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public ImageAdapter(Context context) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return GUIDES_PAGES;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_guide, null);
            }
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            image.setImageResource(COACHING_IDS[position]);
          if(position ==COACHING_IDS.length-1){
              image .setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
                    SharePreferenceUtils.getInstance(getActivity()).saveIsFirst(false);
                    getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                    getActivity().finish();
                    
                }
            });
            }else{
                image.setOnClickListener(null);
            }
            return convertView;
        }
    }
    
    
//    private int getIndexLayout(int index){
//        int i = R.layout.health_mguide_1;
//        String s = "";
//        if (Constants.TYPE_MEASURE == mType) {
//            s = "health_mguide_"+index;
//        } else if (Constants.TYPE_COACHING == mType) {
//            s = "health_cguide_"+index;
//        }
//        try {
//            Field fild = R.layout.class.getDeclaredField(s);
//            try {
//                i = fild.getInt("");
//            } catch (Exception e) {
//                
//            } 
//        } catch (Exception e) {
//            
//        }
//        return i;
//    }

    @Override
    public void requestData() {
        // TODO Auto-generated method stub
        
    }
    
}
