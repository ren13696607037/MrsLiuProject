package com.techfly.liutaitai.model.shopcar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.parser.LoginParser;
import com.techfly.liutaitai.model.mall.activities.ServiceOrderActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.model.shopcar.activities.TakingOrderActivity;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment;

public class CreateOrderSucFragment extends CreateOrderPayCommonFragment
		implements OnClickListener {
	private TextView mPayTv;
	private TextView mBalanceTv;
	private TextView mMoneyTv;
	private CheckBox mBalanceCB;
	private CheckBox mAlipayCB;
	private CheckBox mWenxinCB;
	private CheckBox mOfflineCB;
	private int mPayMethod = Constant.PAY_BALANCE;
	private String mMoney;
    protected double mBalance;

	@Override
	public void requestData() {

		RequestParam param = new RequestParam();
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
		// param.setPostRequestMethod();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.USER_INFO_URL);
		param.setmHttpURL(url);
		param.setmParserClassName(LoginParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				AppLog.Logd("Fly", "object====" + object.toString());
				if (getActivity() != null && !isDetached()) {
					User user = (User) object;
					mBalanceTv.setText(getString(R.string.cash_balance_text1,
							user.getmMoney()));
					try {
                        mBalance = Float.parseFloat(user.getmMoney());
                    } catch (Exception e) {
                        mBalance = 0.0;
                    }
					return;
				}

			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (getActivity() == null || isDetached()) {
					return;
				}

			}
		};
	}

	@Override
	public String onEncapleOrderInfo(HttpURL url) {
		return null;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startReqTask(this);
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
		initTitleView();
		initView(view);
	}

	private void initView(View view) {

		mPayTv = (TextView) view.findViewById(R.id.order_pay);
		mPayTv.setOnClickListener(this);

		mAlipayCB = (CheckBox) view.findViewById(R.id.check_box_alipay);
		mAlipayCB.setOnClickListener(this);
		mBalanceTv = (TextView) view.findViewById(R.id.balance);
		mMoneyTv = (TextView) view.findViewById(R.id.submit_order_success);
		mWenxinCB = (CheckBox) view.findViewById(R.id.check_box_wenxin);
		mWenxinCB.setOnClickListener(this);

		mBalanceCB = (CheckBox) view.findViewById(R.id.check_box);
		mBalanceCB.setOnClickListener(this);

		mOfflineCB = (CheckBox) view.findViewById(R.id.check_box_offline);
		mOfflineCB.setOnClickListener(this);

	}

	private void initTitleView() {
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
		setTitleText(getString(R.string.order_pay_title));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order_create, container,
				false);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.order_pay:
			if (getActivity() instanceof ServiceOrderActivity) {
			    
				ServiceOrderActivity ac = (ServiceOrderActivity) getActivity();
				final Bundle bundle = ac.getBundleInfo();
				String orderId = bundle.getString(IntentBundleKey.ORDER_ID, "");
				String payMoney = bundle.getString(IntentBundleKey.ORDER_MONEY,
						"0.0");
				if(mPayMethod == Constant.PAY_BALANCE){
		                if(mBalance<Float.parseFloat(payMoney)){
		                    showSmartToast("余额不足", Toast.LENGTH_LONG);
		                    return;
		                }
		            }
				String productName = bundle.getString(
						IntentBundleKey.ORDER_PRODUCT, "");
				bundle.putInt(IntentBundleKey.ORDER_PAY_METHOD, mPayMethod);
				onPay(mPayMethod, orderId, payMoney, productName,
						new PayCallBack() {

							@Override
							public void onPaySuccess() {
								ServiceOrderActivity activity = (ServiceOrderActivity) getActivity();
								activity.showOrderFinishFragment(bundle);
							}
						});

			} else {
				TakingOrderActivity ac = (TakingOrderActivity) getActivity();
				final Bundle bundle = ac.getBundleInfo();
				String orderId = bundle.getString(IntentBundleKey.ORDER_ID, "");
				String payMoney = bundle.getString(IntentBundleKey.ORDER_MONEY,
						"0.0");
				if(mPayMethod == Constant.PAY_BALANCE){
                    if(mBalance<Float.parseFloat(payMoney)){
                        showSmartToast("余额不足", Toast.LENGTH_LONG);
                        return;
                    }
                }
				String productName = bundle.getString(
						IntentBundleKey.ORDER_PRODUCT, "");
				bundle.putInt(IntentBundleKey.ORDER_PAY_METHOD, mPayMethod);
				onPay(mPayMethod, orderId, payMoney, productName,
						new PayCallBack() {

							@Override
							public void onPaySuccess() {
								TakingOrderActivity activity = (TakingOrderActivity) getActivity();
								activity.showOrderFinishFragment(bundle);
							}
						});
			}
			break;

		case R.id.check_box:
			mPayMethod = Constant.PAY_BALANCE;
			mBalanceCB.setChecked(true);
			mAlipayCB.setChecked(false);
			mWenxinCB.setChecked(false);
			mOfflineCB.setChecked(false);
			break;
		case R.id.check_box_alipay:
			mPayMethod = Constant.PAY_ALIPAY;
			mAlipayCB.setChecked(true);
			mBalanceCB.setChecked(false);
			mWenxinCB.setChecked(false);
			mOfflineCB.setChecked(false);
			break;
		case R.id.check_box_wenxin:
			mPayMethod = Constant.PAY_WENXIN;
			mWenxinCB.setChecked(true);
			mBalanceCB.setChecked(false);
			mAlipayCB.setChecked(false);
			mOfflineCB.setChecked(false);
			break;
		case R.id.check_box_offline:
			mPayMethod = Constant.PAY_OFFLINE;
			mWenxinCB.setChecked(false);
			mBalanceCB.setChecked(false);
			mAlipayCB.setChecked(false);
			mOfflineCB.setChecked(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onOrderCreateSuccess(String orderId, String money,
			String proName) {

	}

	public void onShowDisplay(Bundle bundle) {
		mMoney = bundle.getString(IntentBundleKey.ORDER_MONEY, "");
		mMoneyTv.setText(getString(R.string.submit_order_success, mMoney));

	}

}
