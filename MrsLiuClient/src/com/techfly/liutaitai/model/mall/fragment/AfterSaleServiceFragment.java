package com.techfly.liutaitai.model.mall.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class AfterSaleServiceFragment extends CommonFragment implements
		OnClickListener {

	private String mOrderId;

	private EditText mEtContent;
	private TextView mTvCommit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOrderId = getActivity().getIntent().getStringExtra(
				IntentBundleKey.ORDER_ID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_after_sale_service,
				container, false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initHeader();
		initViews(view);
	}

	private void initHeader() {
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
		setTitleText(R.string.after_sale_service);

	}

	private void initViews(View view) {
		mEtContent = (EditText) view.findViewById(R.id.after_et_content);
		mTvCommit = (TextView) view.findViewById(R.id.after_commint);
		mTvCommit.setOnClickListener(this);

	}

	@Override
	public void requestData() {
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
				+ Constant.AFTER_SALE_SERVICE);
		url.setmGetParamPrefix(Constant.ORDER_INFO_ID).setmGetParamValues(
				mOrderId);
		url.setmGetParamPrefix(Constant.AFTER_SALE_SERVICE_CONTENT)
				.setmGetParamValues(mEtContent.getText().toString());
		param.setmHttpURL(url);
		param.setmIsLogin(true);
		param.setmToken(SharePreferenceUtils.getInstance(getActivity())
				.getUser().getmToken());
		param.setmId(SharePreferenceUtils.getInstance(getActivity()).getUser()
				.getmId());
		param.setPostRequestMethod();
		param.setmParserClassName(CommonParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd(object.toString());
				if (getActivity() == null || isDetached()) {
					return;
				}

				if (object instanceof ResultInfo) {
					ResultInfo ri = (ResultInfo) object;
					if (ri != null) {
						if (ri.getmCode() == 0) {
							SmartToast.makeText(getActivity(), "操作成功",
									Toast.LENGTH_SHORT).show();
							getActivity().finish();
							// 之后的一些操作,例如刷新列表
						} else {
							SmartToast.makeText(getActivity(), "操作失败",
									Toast.LENGTH_SHORT).show();
						}
					}

				}

			}

		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				AppLog.Loge(" data failed to load" + error.getMessage());
				SmartToast.makeText(getActivity(), R.string.loading_fail,
						Toast.LENGTH_SHORT).show();
			}
		};
	}

	@Override
	public void onClick(View v) {
		if (TextUtils.isEmpty(mOrderId)) {
			SmartToast.makeText(getActivity(), "未正确获取到订单号", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (mEtContent.length() == 0) {
			SmartToast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		startReqTask(this);

	}

}
