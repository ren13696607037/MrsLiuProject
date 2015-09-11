package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        mIvRight = (ImageView) view.findViewById(R.id.home_right);
        mIvRight.setOnClickListener(this);

        mListView = (HorizontalListView) view.findViewById(R.id.home_list);
        mListView.setDivider(getActivity().getResources().getDrawable(R.drawable.vertical_line));
        mListView.setAdapter(new DateAdapter(getActivity(), initDateList()));

        mGridView = (GridViewForScrollView) view.findViewById(R.id.home_grid);
        mGridView.setAdapter(new TimesAdapter(getActivity(), initTimeList()));

    }

    private List<TimeBean> initTimeList() {
        List<TimeBean> list = new ArrayList<TimeBean>();

        TimeBean time1 = new TimeBean();
        return null;
    }

    private List<TimeBean> initDateList() {

        List<TimeBean> list = new ArrayList<TimeBean>();
        TimeBean time1 = new TimeBean();
        time1.setMisSelect(true);
        time1.setTime("明天\n"
                + DateUtils.getTime(new Date().getTime() + 24 * 60 * 60 * 1000,
                        "MM月dd日"));
        time1.setTimeMill(new Date().getTime() + 24 * 60 * 60 * 1000);

        TimeBean time2 = new TimeBean();
        long timeMill2 = new Date().getTime() + 2 * 24 * 60 * 60 * 1000;
        time2.setTime(DateUtils.getWeek(timeMill2) + "\n"
                + DateUtils.getTime(timeMill2, "MM月dd日"));
        time2.setTimeMill(timeMill2);

        TimeBean time3 = new TimeBean();
        long timeMill3 = new Date().getTime() + 3 * 24 * 60 * 60 * 1000;
        time3.setTime(DateUtils.getWeek(timeMill3) + "\n"
                + DateUtils.getTime(timeMill3, "MM月dd日"));
        time3.setTimeMill(timeMill3);

        TimeBean time4 = new TimeBean();
        long timeMill4 = new Date().getTime() + 4 * 24 * 60 * 60 * 1000;
        time4.setTime(DateUtils.getWeek(timeMill4) + "\n"
                + DateUtils.getTime(timeMill4, "MM月dd日"));
        time4.setTimeMill(timeMill4);

        TimeBean time5 = new TimeBean();
        long timeMill5 = new Date().getTime() + 5 * 24 * 60 * 60 * 1000;
        time5.setTime(DateUtils.getWeek(timeMill5) + "\n"
                + DateUtils.getTime(timeMill5, "MM月dd日"));
        time5.setTimeMill(timeMill5);

        TimeBean time6 = new TimeBean();
        long timeMill6 = new Date().getTime() + 6 * 24 * 60 * 60 * 1000;
        time6.setTime(DateUtils.getWeek(timeMill6) + "\n"
                + DateUtils.getTime(timeMill6, "MM月dd日"));
        time6.setTimeMill(timeMill6);

        list.add(time1);
        list.add(time2);
        list.add(time3);
        list.add(time4);
        list.add(time5);
        list.add(time6);
        return list;
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

        default:
            break;
        }
    }

}
