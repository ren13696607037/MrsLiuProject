package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.adapter.DateAdapter;
import com.techfly.liutaitai.model.mall.adapter.TimesAdapter;
import com.techfly.liutaitai.model.mall.bean.TimeBean;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.DateUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.GridViewForScrollView;
import com.techfly.liutaitai.util.view.HorizontalListView;

public class ServiceTimeFragment extends CommonFragment implements
        OnClickListener {
    private HorizontalListView mListView;
    private ImageView mIvLeft;
    private ImageView mIvRight;
    private DateAdapter mDateAdapter;
    private TimesAdapter mTimeAdapter;
    private GridViewForScrollView mGridView;
    private static final String TIME_FORMAT1="MM月dd日";
    private List<TimeBean> mList = new ArrayList<TimeBean>();
    
    private List<TimeBean> mDateList = new ArrayList<TimeBean>();
    private  TimesAdapter mAdapter;
    
    private  DateAdapter  mDAdapter;
    
    private long mSelectTimeMills;
    private TextView mTextView;
    
    @Override
    public void requestData() {

    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        TimeBean time1 = new TimeBean();
        time1.setMisSelect(true);
        time1.setTime("明天\n"
                + DateUtils.getTime(new Date().getTime() + 24 * 60 * 60 * 1000,
                        TIME_FORMAT1));
        time1.setTimeMill(new Date().getTime() + 24 * 60 * 60 * 1000);
        initDateList();
        initTimeList(time1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_service_time, container,
                false);// 服务时间

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        setTitleText("请选择服务时间");
        mIvLeft = (ImageView) view.findViewById(R.id.home_left);
        mIvLeft.setOnClickListener(this);
        mTextView = (TextView) view.findViewById(R.id.confirm);
        mTextView.setOnClickListener(this);
        mIvRight = (ImageView) view.findViewById(R.id.home_right);
        mIvRight.setOnClickListener(this);
        mListView = (HorizontalListView) view.findViewById(R.id.home_list);
        mListView.setDivider(getActivity().getResources().getDrawable(R.drawable.vertical_line));
        mDAdapter =new DateAdapter(getActivity(), mDateList);
        mListView.setAdapter(mDAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                for(TimeBean time:mDateList){
                    time.setMisSelect(false);
                }
                
               TimeBean time = (TimeBean) arg0.getItemAtPosition(arg2);
               time.setMisSelect(true);
               initTimeList(time);
               
               mAdapter.notifyDataSetChanged();
               mDAdapter.notifyDataSetChanged();
            }
        });
        

        mGridView = (GridViewForScrollView) view.findViewById(R.id.home_grid);
      
        mAdapter  = new TimesAdapter(getActivity(), mList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                for(TimeBean time:mList){
                    time.setMisSelect(false);
                }
                
               TimeBean time = (TimeBean) arg0.getItemAtPosition(arg2);
               time.setMisSelect(true);
               mSelectTimeMills = time.getTimeMill();
               mAdapter.notifyDataSetChanged();
                
            }
        });

    }

    private void initTimeList(TimeBean date) {
        mList.clear();

        TimeBean time1 = new TimeBean();
        time1.setMisSelect(true);
        time1.setTime("10:00");
        time1.setTimeMill(DateUtils.currentMills(date.getTimeMill(),time1.getTime()));
        mList.add(time1);
        mSelectTimeMills = time1.getTimeMill();
        for(int i=1;i<20;i++){
            TimeBean time2 = new TimeBean();
            time2.setTime(DateUtils.getTime(time1.getTimeMill()+i*30*60*1000, "HH:mm"));
            time2.setTimeMill(time1.getTimeMill()+i*30*60*1000);
            mList.add(time2); 
        }
    
    }

    private void initDateList() {
        TimeBean time1 = new TimeBean();
        time1.setMisSelect(true);
        time1.setTime("明天\n"
                + DateUtils.getTime(new Date().getTime() + 24 * 60 * 60 * 1000,
                        TIME_FORMAT1));
        time1.setTimeMill(new Date().getTime() + 24 * 60 * 60 * 1000);

        TimeBean time2 = new TimeBean();
        long timeMill2 = new Date().getTime() + 2 * 24 * 60 * 60 * 1000;
        time2.setTime(DateUtils.getWeek(timeMill2) + "\n"
                + DateUtils.getTime(timeMill2, TIME_FORMAT1));
        time2.setTimeMill(timeMill2);

        TimeBean time3 = new TimeBean();
        long timeMill3 = new Date().getTime() + 3 * 24 * 60 * 60 * 1000;
        time3.setTime(DateUtils.getWeek(timeMill3) + "\n"
                + DateUtils.getTime(timeMill3, TIME_FORMAT1));
        time3.setTimeMill(timeMill3);

        TimeBean time4 = new TimeBean();
        long timeMill4 = new Date().getTime() + 4 * 24 * 60 * 60 * 1000;
        time4.setTime(DateUtils.getWeek(timeMill4) + "\n"
                + DateUtils.getTime(timeMill4,TIME_FORMAT1));
        time4.setTimeMill(timeMill4);

        TimeBean time5 = new TimeBean();
        long timeMill5 = new Date().getTime() + 5 * 24 * 60 * 60 * 1000;
        time5.setTime(DateUtils.getWeek(timeMill5) + "\n"
                + DateUtils.getTime(timeMill5, TIME_FORMAT1));
        time5.setTimeMill(timeMill5);

        TimeBean time6 = new TimeBean();
        long timeMill6 = new Date().getTime() + 6 * 24 * 60 * 60 * 1000;
        time6.setTime(DateUtils.getWeek(timeMill6) + "\n"
                + DateUtils.getTime(timeMill6, TIME_FORMAT1));
        time6.setTimeMill(timeMill6);

        mDateList.add(time1);
        mDateList.add(time2);
        mDateList.add(time3);
        mDateList.add(time4);
        mDateList.add(time5);
        mDateList.add(time6);
      
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.home_left:
            
            break;
            
        case R.id.home_right:
            
            break;
        case R.id.confirm:
            getActivity().setResult(0, new Intent().putExtra("data", mSelectTimeMills));
            getActivity().finish();
            break;
        default:
            
            break;
        }
    }

}
