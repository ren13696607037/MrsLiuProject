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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.adapter.JishiScheuleTimeAdapter;
import com.techfly.liutaitai.model.mall.bean.JishiInfo;
import com.techfly.liutaitai.model.mall.bean.JishiScheuleTime;
import com.techfly.liutaitai.model.mall.bean.TimeBean;
import com.techfly.liutaitai.model.mall.parser.JiShiInfoParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.DateUtils;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.ListViewForScrollView;

public class JishiInfoFragment extends CommonFragment {
    private TextView mName;
    private TextView mSex;
    private TextView mServiceTime;
    private ImageView mHeadImg;
    private RatingBar mRate;
    private TextView mConfirm;
    private ListViewForScrollView mListView;
    private JishiScheuleTimeAdapter mAdapter;
    private JishiInfo mInfo;
    private String mId;
    private List<JishiScheuleTime> mList = new ArrayList<JishiScheuleTime>();
    private static final String TIME_FORMAT1 = "MM dd";

    @Override
    public void requestData() {
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
        int userId = 0;
        if (user != null) {
            userId = Integer.parseInt(user.getmId());
        }
        if (userId == 0) {
            return;
        }
        param.setmIsLogin(true);
        param.setmId(user.getmId());
        param.setmToken(user.getmToken());
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + "service/master");// 生鲜请求地址组装
        url.setmGetParamPrefix("mid");
        url.setmGetParamValues(mId);
        // url.setmGetParamPrefix(JsonKey.UserKey.PUSH).setmGetParamValues(
        // JPushInterface.getRegistrationID(getActivity()));
        param.setmHttpURL(url);
        param.setmParserClassName(JiShiInfoParser.class.getName());
        RequestManager
                .getRequestData(getActivity(), createMyReqSuccessListener(),
                        createMyReqErrorListener(), param);

    }

    private Response.Listener<Object> createMyReqSuccessListener() {

        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mInfo = (JishiInfo) object;
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                onDisplayInfo();
            }

        };
    }

    private void onDisplayInfo() {
        mList.clear();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(new Date());
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        int startTime = 0;
        if (mInfo.getmIdleTime() == 0) {
            startTime = 1;
        }
        for (int i = startTime; i <= 6; i++) {
            TimeBean time1 = new TimeBean();
            long timeMill2 = c.getTimeInMillis() + i * 24 * 60 * 60 * 1000;
            time1.setMisSelect(true);
            time1.setTime(DateUtils.getTime(timeMill2, TIME_FORMAT1));
            time1.setTimeMill(timeMill2);
            if (i == 0) {
                initTimeList(time1, true);
            } else {
                initTimeList(time1, false);
            }

        }
        mName.setText(mInfo.getmName());
        mSex.setText("性别：" + mInfo.getmSex());
        mServiceTime.setText("服务：" + mInfo.getmServiceTime() + "次");
        ImageLoader.getInstance().displayImage(mInfo.getmImg(), mHeadImg,
                ImageLoaderUtil.mHallIconLoaderOptions);
        mRate.setRating(mInfo.getmRating());
        mAdapter = new JishiScheuleTimeAdapter(getActivity(), mList,
                mInfo.getmList());
        mListView.setAdapter(mAdapter);

    }

    /**
     * 初始化 每行的时间区域
     * 
     * @param starttime
     * @return
     */
    private List<TimeBean> initTimeBean(long starttime, boolean isToady) {
        List<TimeBean> time = new ArrayList<TimeBean>();
        TimeBean time1 = new TimeBean();
        time1.setMisSelect(true);
        time1.setTimeMill(starttime);
        if (isToady) {
            int delayTime = mInfo.getmIdleTime();
            long currentTimeMills = new Date().getTime();
            for (int i = 1; i <= 11; i++) {
                    if (DateUtils.currentMills(currentTimeMills,
                            "8:00") + i * 60 * 60 * 1000 <= currentTimeMills
                            + delayTime * 60 * 60 * 1000) {
                        TimeBean time2 = new TimeBean();
                        time.add(time2);// 空闲的时间
                        //
                    } else {
                        TimeBean time2 = new TimeBean();
                        time2.setTime(DateUtils.getTime(DateUtils.currentMills(currentTimeMills,
                                "8:00") + i
                                * 60 * 60 * 1000, "HH"));
                        time2.setTimeMill(time1.getTimeMill() + i * 60 * 60
                                * 1000);
                        time2.setTimeMill2(time1.getTimeMill() + i * 60 * 60
                                * 1000 + 30 * 60 * 1000);
                        time.add(time2);
                    }
            }

        } else {
            for (int i = 1; i <= 11; i++) {
                TimeBean time2 = new TimeBean();
                time2.setTime(DateUtils.getTime(time1.getTimeMill() + i * 60
                        * 60 * 1000, "HH"));
                time2.setTimeMill(time1.getTimeMill() + i * 60 * 60 * 1000);
                time2.setTimeMill2(time1.getTimeMill() + i * 60 * 60 * 1000
                        + 30 * 60 * 1000);
                time.add(time2);
            }
        }

        return time;
    }

    private void initTimeList(TimeBean date, boolean isToday) {
        if (mInfo.getmIdleTime() != 0) {
            if (isToday) {
                int delayTime = mInfo.getmIdleTime();
                long currentTimeMills = new Date().getTime();
                if (currentTimeMills + delayTime * 60 * 60 * 1000 >= DateUtils
                        .currentMills(date.getTimeMill(), "9:00")
                        && currentTimeMills + delayTime * 60 * 60 * 1000 <= DateUtils
                                .currentMills(date.getTimeMill(), "19:00")) {
                    JishiScheuleTime scheuleTime = new JishiScheuleTime();
                    scheuleTime.setmDate("今天");
                    scheuleTime.setList(initTimeBean(currentTimeMills, true));
                    mList.add(scheuleTime);
                } else {
                    if (currentTimeMills + delayTime * 60 * 60 * 1000 > DateUtils
                            .currentMills(date.getTimeMill(), "19:00")) {
                        JishiScheuleTime scheuleTime = new JishiScheuleTime();
                        if (date.getTimeMill() - new Date().getTime() < 24 * 60 * 60 * 1000) {
                            scheuleTime.setmDate("明天");
                        } else {
                            scheuleTime.setmDate(DateUtils.getTime(
                                    date.getTimeMill(), "MM.dd"));
                        }
                        scheuleTime.setList(initTimeBean(DateUtils
                                .currentMills(date.getTimeMill(), "8:00"),
                                false));
                        mList.add(scheuleTime);
                    } else {
                        JishiScheuleTime scheuleTime = new JishiScheuleTime();
                        scheuleTime.setmDate("今天");
                        scheuleTime
                                .setList(initTimeBean(
                                        DateUtils.currentMills(
                                                date.getTimeMill(), "8:00"),
                                        true));
                        mList.add(scheuleTime);
                    }
                }
            } else {
                JishiScheuleTime scheuleTime = new JishiScheuleTime();
                TimeBean time1 = new TimeBean();
                time1.setMisSelect(true);
                if (date.getTimeMill() - new Date().getTime() < 24 * 60 * 60 * 1000) {
                    scheuleTime.setmDate("明天");
                } else {
                    scheuleTime.setmDate(DateUtils.getTime(date.getTimeMill(),
                            "MM.dd"));
                }
                scheuleTime.setList(initTimeBean(
                        DateUtils.currentMills(date.getTimeMill(), "8:00"),
                        false));
                mList.add(scheuleTime);
            }

        } else {
            JishiScheuleTime scheuleTime = new JishiScheuleTime();
            TimeBean time1 = new TimeBean();
            time1.setMisSelect(true);
            if (date.getTimeMill() - new Date().getTime() < 24 * 60 * 60 * 1000) {
                scheuleTime.setmDate("明天");
            } else {
                scheuleTime.setmDate(DateUtils.getTime(date.getTimeMill(),
                        "MM.dd"));
            }
            scheuleTime.setList(initTimeBean(
                    DateUtils.currentMills(date.getTimeMill(), "8:00"), false));
            mList.add(scheuleTime);
        }

    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                mLoadHandler.removeMessages(Constant.NET_FAILURE);
                mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
                showMessage(R.string.loading_fail);
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mId = activity.getIntent().getStringExtra(IntentBundleKey.ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        startReqTask(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jishi_info, container,
                false);
        return view;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
        setTitleText("技师详情");
        mName = (TextView) view.findViewById(R.id.name);
        mConfirm = (TextView) view.findViewById(R.id.confirm);
        mListView = (ListViewForScrollView) view
                .findViewById(R.id.time_listview);
        mHeadImg = (ImageView) view.findViewById(R.id.img);
        mRate = (RatingBar) view.findViewById(R.id.rate_bar);
        mServiceTime = (TextView) view.findViewById(R.id.service_times);
        mSex = (TextView) view.findViewById(R.id.sex);
        mConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                getActivity().setResult(100);
                getActivity().finish();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

}
