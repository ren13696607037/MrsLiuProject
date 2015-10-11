package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.bean.OrderEva;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.adapter.ViewHolder;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class OrderEvaFragment extends CommonFragment implements OnClickListener {

	private Button mBtCommit;
	private ListView mListView;
	private ArrayList<OrderEva> mDatas = new ArrayList<OrderEva>();
	private CommonAdapter<OrderEva> mAdapter;

	private MyOrder mOrder;
	private String mJson;
	private User mUser;
	private int index = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
		mOrder = (MyOrder) getActivity().getIntent().getSerializableExtra(
				IntentBundleKey.ORDER);
		if (mOrder == null) {
			SmartToast.makeText(getActivity(), R.string.error_get_user_id,
					Toast.LENGTH_SHORT).show();
			return;
		}

		setData();
	}

	private void setData() {
		ArrayList<Product> list = mOrder.getmList();
		if (list == null || list.size() == 0) {
			return;
		}

		mDatas.clear();

		for (int i = 0; i < list.size(); i++) {
			Product p = list.get(i);
			if (p != null) {
				OrderEva oe = new OrderEva();
				oe.setmProductId(Integer.valueOf(p.getmId()));
				oe.setmProductName(p.getmName());
				mDatas.add(oe);
			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_order_eva, container, false);
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
		setTitleText(R.string.order_eva);

	}

	private void initViews(View view) {
		mBtCommit = (Button) view.findViewById(R.id.order_eva_bt_submit);
		mBtCommit.setOnClickListener(this);

		mListView = (ListView) view.findViewById(R.id.order_eva_list);
		mAdapter = new CommonAdapter<OrderEva>(getActivity(), mDatas,
				R.layout.item_order_eva) {

			@Override
			public void convert(ViewHolder holder, final OrderEva item,
					final int position) {
				holder.setText(R.id.order_eva_item_name, item.getmProductName());
				holder.setRating(R.id.order_eva_item_bar,
						(float) item.getmStar());
				RatingBar rb = holder.getView(R.id.order_eva_item_bar);
				rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						item.setmStar((int) rating);

					}
				});
				EditText et = holder.getView(R.id.order_eva_item_content);

				et.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						EditText et = (EditText) arg0;
						String hint;
						if (arg1) {
							hint = et.getHint().toString();
							et.setTag(hint);
							et.setHint("");
						} else {
							hint = et.getTag().toString();
							et.setHint(hint);
						}

					}
				});
				et.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						item.setmContent(s.toString());

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub

					}
				});
				et.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_UP) {
							index = position;
						}
						return false;
					}
				});

				if (index >= 0 && position == index) {
					et.requestFocus();
				}

				if (item.getmContent() != null) {
					et.setText(item.getmContent());
				}

			}
		};
		mListView.setAdapter(mAdapter);

	}

	@Override
	public void requestData() {
		if (mUser == null) {// 未登录，先这样，过些时间在做
			SmartToast.makeText(getActivity(), R.string.error_get_user_id,
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ORDER_EVA);
		url.setmGetParamPrefix(Constant.ORDER_EVA_CONTENT).setmGetParamValues(
				mJson);
		url.setmGetParamPrefix(Constant.ORDER_EVA_ID).setmGetParamValues(
				mOrder.getmId());
		param.setmHttpURL(url);
		param.setmIsLogin(true);
		param.setmToken(mUser.getmToken());
		param.setmId(mUser.getmId());
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
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				if (object instanceof ResultInfo) {
					ResultInfo ri = (ResultInfo) object;
					if (ri.getmCode() == 0) {
						SmartToast.makeText(getActivity(), "评价成功",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.putExtra(IntentBundleKey.DATA, true);
						getActivity().setResult(Activity.RESULT_OK, intent);
						getActivity().finish();
						return;
					}

					SmartToast.makeText(getActivity(), "评价失败",
							Toast.LENGTH_SHORT).show();
				}

			}

		};
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
	public void onClick(View v) {
		setJson();
		if (mJson != null) {
			startReqTask(this);
		}
	}

	private void setJson() {
		mJson = null;

		try {
			JSONArray array = new JSONArray();
			for (int i = 0; i < mDatas.size(); i++) {
				OrderEva oe = mDatas.get(i);
				LinearLayout layout = (LinearLayout) mListView.getChildAt(i);
				if (layout == null) {
					return;
				}

				EditText et = (EditText) layout
						.findViewById(R.id.order_eva_item_content);
				oe.setmContent(et.getText().toString());
				RatingBar bar = (RatingBar) layout
						.findViewById(R.id.order_eva_item_bar);
				oe.setmStar((int) bar.getRating());

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", oe.getmProductId());
				jsonObject.put("content", oe.getmContent());
				jsonObject.put("stars", oe.getmStar());
				array.put(jsonObject);

			}

			mJson = array.toString();
			AppLog.Logd("Shi", "Json====" + mJson);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
