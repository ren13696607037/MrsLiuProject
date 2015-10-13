package com.techfly.liutaitai.model.pcenter.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.TechOrderDetailParser;
import com.techfly.liutaitai.model.pcenter.activities.OrderDetailActivity;
import com.techfly.liutaitai.model.pcenter.adapter.OrderClick;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.FileTool;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.ManagerListener.OrderDetailListener;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.StartTimeText;
import com.techfly.liutaitai.util.view.TechFinishDialog;

public class TechOrderDetailFragment extends CommonFragment implements
		OnClickListener, OrderDetailListener{
	private OrderDetailActivity mActivity;
	private TextView mNo;
	private TextView mTime;
	private TextView mTimeShow;
	private TextView mName;
	private TextView mPhone;
	private TextView mAddress;
	private TextView mServiceTime;
	private TextView mVoucher;
	private TextView mTotal;
	private ImageView mImageView;
	private TextView mPrice;
	private TextView mProName;
	private TextView mState;
	private Button mButton;
	private Button mButton2;
	private TechOrder mOrder;
	private String mId;
	private ImageView mIvPhone;
	private ImageView mIvAddress;
	private TextView mClear;
	private User mUser;
	private StartTimeText mTimeStart;
	private final int MSG_DATA = 0x101;
	private Handler mServiceDetailHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_DATA) {
				setData();
			}
		};
	};
	
	private int mType;
	private TechFinishDialog mDialog;
	private static final int TAKE_BIG_PICTURE = 0x901;
	private static final int TAKE_BIG_LOCAL_PICTURE = TAKE_BIG_PICTURE + 1;
	private static final int UPLOAD_IMAGE = TAKE_BIG_LOCAL_PICTURE + 1;
	// 获取照片type
	public static final int IMAGE_TYPE_CAMERA = 0; // 摄像头拍照页面
	private String mSelectItems;
	private Uri mPhotoPath;
	private TextView mPayWay;
	BroadcastReceiver mImageWallChangeBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getBooleanExtra(IntentBundleKey.REDIRECT_TYPE, false)
					&& null != intent
							.getStringExtra(IntentBundleKey.IMAGE_PATH)) {
				mSelectItems = intent
						.getStringExtra(IntentBundleKey.IMAGE_PATH);
			}
			if (!TextUtils.isEmpty(mSelectItems)) {
				mDialog = new TechFinishDialog(getActivity(), "file:///"
						+ mSelectItems,1);
				mDialog.show();
				mDialog.setCanceledOnTouchOutside(true);
			}
		}
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (OrderDetailActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mId = mActivity.getIntent().getStringExtra(IntentBundleKey.ORDER_ID);
		mUser = SharePreferenceUtils.getInstance(mActivity).getUser();
		startReqTask(TechOrderDetailFragment.this);
		ManagerListener.newManagerListener().onRegisterOrderDetailListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tech_service_detail,
				container, false);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ManagerListener.newManagerListener().onUnRegisterOrderDetailListener(this);
		mTimeStart.toFinishHandler();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
				"WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onInitView(view);
	}

	private void onInitView(View view) {
		IntentFilter intentFilter = new IntentFilter(
				Constant.REFRESH_UPLOAD_GRIDVIEW_IMAGE);
		getActivity().registerReceiver(mImageWallChangeBroadcastReceiver,
				intentFilter);
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
		setTitleText(R.string.service_detail_title);

		mAddress = (TextView) view.findViewById(R.id.tsd_address);
		mButton = (Button) view.findViewById(R.id.tsd_btn);
		mButton2 = (Button) view.findViewById(R.id.tsd_btn2);
		mImageView = (ImageView) view.findViewById(R.id.tsd_img);
		mName = (TextView) view.findViewById(R.id.tsd_name);
		mNo = (TextView) view.findViewById(R.id.tsd_no);
		mPhone = (TextView) view.findViewById(R.id.tsd_phone);
		mPrice = (TextView) view.findViewById(R.id.tsd_product_price);
		mProName = (TextView) view.findViewById(R.id.tsd_product_name);
		mTime = (TextView) view.findViewById(R.id.tsd_time);
		mTotal = (TextView) view.findViewById(R.id.tsd_total);
		mVoucher = (TextView) view.findViewById(R.id.tsd_voucher);
		mServiceTime = (TextView) view.findViewById(R.id.tsd_service_time);
		mState = (TextView) view.findViewById(R.id.tsd_state);
		mIvAddress = (ImageView) view.findViewById(R.id.tsd_address_img);
		mIvPhone = (ImageView) view.findViewById(R.id.tsd_phone_img);
		mTimeStart = (StartTimeText) view.findViewById(R.id.tsd_time_start);
		mPayWay = (TextView) view.findViewById(R.id.tsd_text1);
		mClear = (TextView) view.findViewById(R.id.tsd_service_clear);
		mTimeShow = (TextView) view.findViewById(R.id.tsd_service_time_show);

		mIvAddress.setOnClickListener(this);
		mIvPhone.setOnClickListener(this);
		mButton.setOnClickListener(this);
		mButton2.setOnClickListener(this);

	}

	private void setData() {
		mNo.setText(mActivity.getString(R.string.service_detail_text,
				mOrder.getmServiceNum()));
		mAddress.setText(mActivity.getString(R.string.service_detail_text4,
				mOrder.getmCustomerAddress()));
		ImageLoader.getInstance().displayImage(mOrder.getmServiceIcon(),
				mImageView, ImageLoaderUtil.mOrderServiceIconLoaderOptions);
		mName.setText(mActivity.getString(R.string.service_detail_text2,
				mOrder.getmCustomerName()));
		mPhone.setText(mActivity.getString(R.string.service_detail_text3,
				mOrder.getmCustomerPhone()));
		mTime.setText(mActivity.getString(R.string.order_service_text,
				mOrder.getmCustomerTime()));
		mTimeShow.setText(mActivity.getString(R.string.order_service_text2,
				mOrder.getmMinutes()));
		mServiceTime.setText(mActivity.getString(R.string.service_detail_text1,
				mOrder.getmOrderTime()));
		mProName.setText(mOrder.getmServiceName());
		mTotal.setText(mActivity.getString(R.string.service_detail_text9,
				mOrder.getmServicePrice()));
		mVoucher.setText(mActivity.getString(R.string.service_detail_text7,
				mOrder.getmVoucher()));
		mPrice.setText(mActivity
				.getString(R.string.service_detail_text5,
						(float) Math.round((Float.valueOf(mOrder
								.getmServicePrice()) + Float.valueOf(mOrder
								.getmVoucher())) * 100) / 100));
		setState(mOrder, mState, mButton, mButton2);
		mButton.setOnClickListener(new OrderClick(mActivity, mOrder, mButton
				.getText().toString(),1));
		mButton2.setOnClickListener(new OrderClick(mActivity, mOrder, mButton2
				.getText().toString(),1));
		mTimeStart.toStart(System.currentTimeMillis() - Utility.Date2Millis(mOrder.getmStartTime()));
		setPayWay(mPayWay, mOrder.getmPayWay());
		if("0".equals(mOrder.getmServiceType()) && "1".equals(mOrder.getmClear())){
    		mClear.setVisibility(View.VISIBLE);
    	}else{
    		mClear.setVisibility(View.GONE);
    	}
	}
	private void setPayWay(TextView textView, String state){
    	if("0".equals(state)){
    		textView.setText(getString(R.string.service_detail_text8,getString(R.string.recharge_text4)));
    	}else if("1".equals(state)){
    		textView.setText(getString(R.string.service_detail_text8,getString(R.string.recharge_text2)));
    	}else if("2".equals(state)){
    		textView.setText(getString(R.string.service_detail_text8,getString(R.string.recharge_text1)));
    	}else if("3".equals(state)){
    		textView.setText(getString(R.string.service_detail_text8,getString(R.string.recharge_text5)));
    	}
    }
	

	private void setState(TechOrder order, TextView textView, Button button,
			Button button2) {
		mTimeStart.setVisibility(View.GONE);
		int state = Integer.valueOf(order.getmServiceStatus());
		button.setVisibility(View.VISIBLE);
		button2.setVisibility(View.VISIBLE);
		if(state==1){
			textView.setText(R.string.tech_order_list_state);
			button2.setText(R.string.tech_order_list_btn);
			button.setText(R.string.tech_order_list_btn1);
		}else if(state==2){
			textView.setText(R.string.tech_order_list_state1);
			button.setText(R.string.tech_order_list_btn3);
			button2.setText(R.string.tech_order_list_btn2);
		}else if(state==3){
			textView.setText(R.string.tech_order_list_state2);
			button2.setVisibility(View.GONE);
			button.setText(R.string.tech_order_list_btn4);
			mTimeStart.setVisibility(View.VISIBLE);
		}else if(state==4 || state == 6){
			textView.setText(R.string.tech_order_list_state3);
			button.setText(R.string.tech_order_list_btn5);
			button2.setVisibility(View.GONE);
		}else if(state == 5){
			textView.setText(R.string.order_service_state4);
			button.setVisibility(View.GONE);
			button2.setVisibility(View.GONE);
		}else if(state == 0){
			textView.setText(R.string.order_service_state);
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == -1){
			textView.setText("订单已取消");
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == -2){
			textView.setText(R.string.order_service_state8);
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == 7){
			textView.setText(R.string.order_service_state7);
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else if(state == 9){
			textView.setText("用户已删除");
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}else{
			button.setVisibility(View.INVISIBLE);
			button2.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void requestData() {
		if (mType == 1) {
			toDone();
		} else {
			RequestParam param = new RequestParam();
			HttpURL url = new HttpURL();
			if (mType == 3) {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_REMOVE_URL);
				url.setmGetParamPrefix(JsonKey.ServiceKey.RID)
						.setmGetParamValues(mOrder.getmId());
				param.setmParserClassName(CommonParser.class.getName());
			} else if (mType == 4) {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_REFRSE_URL);
				url.setmGetParamPrefix(JsonKey.ServiceKey.RID)
						.setmGetParamValues(mOrder.getmId());
				param.setmParserClassName(CommonParser.class.getName());
			} else if (mType == 2) {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_START_URL);
				url.setmGetParamPrefix(JsonKey.ServiceKey.RID)
						.setmGetParamValues(mOrder.getmId());
				param.setmParserClassName(CommonParser.class.getName());
			} else if (mType == 5) {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_TAKE_URL);
				url.setmGetParamPrefix(JsonKey.ServiceKey.RID)
						.setmGetParamValues(mOrder.getmId());
				param.setmParserClassName(CommonParser.class.getName());
			} else {
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
						+ Constant.TECH_ORDER_DETAIL_URL);
				url.setmGetParamPrefix(JsonKey.ServiceKey.RID)
						.setmGetParamValues(mId);
				param.setmParserClassName(TechOrderDetailParser.class.getName());
			}
			param.setmIsLogin(true);
			param.setmId(mUser.getmId());
			param.setmToken(mUser.getmToken());
			param.setPostRequestMethod();
			param.setmHttpURL(url);
			RequestManager.getRequestData(getActivity(),
					createMyReqSuccessListener(), createMyReqErrorListener(),
					param);
		}
	}

	private void toDone() {
		RequestParams params = new RequestParams();
		params.addHeader("enctype", "multipart/form-data");
		params.addHeader("lt-token", mUser.getmToken());
		params.addHeader("lt-id", mUser.getmId());
		params.addBodyParameter("img", new File(mSelectItems));
		params.addBodyParameter(JsonKey.ServiceKey.RID, mOrder.getmId());
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Constant.YIHUIMALL_BASE_URL
				+ Constant.TECH_ORDER_DONE_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException exception, String arg1) {
						AppLog.Logd("Fly",
								"exception===" + exception.getMessage());
						if (!isDetached()) {
							mLoadHandler.removeMessages(Constant.NET_SUCCESS);
							mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
							SmartToast.makeText(getActivity(),
									getString(R.string.life_helper_send_fail),
									Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						AppLog.Logd("Fly", "info===" + info.result);
						if (!isDetached()) {
							mLoadHandler.removeMessages(Constant.NET_SUCCESS);
							mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
							// TODO头像修改成功后的操作
							JSONObject obj;
							try {
								obj = new JSONObject(info.result);
								if (obj != null) {
									if (obj.optInt(JsonKey.CODE) == 0) {
										SmartToast
												.makeText(
														getActivity(),
														getString(R.string.life_helper_send_success),
														Toast.LENGTH_LONG)
												.show();
										mType = 0;
										ManagerListener.newManagerListener().notifyOrderPayListener(mOrder);
										startReqTask(TechOrderDetailFragment.this);
									} else {
										SmartToast.makeText(getActivity(),
												obj.optString(JsonKey.MESSAGE),
												Toast.LENGTH_LONG).show();
									}
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				AppLog.Logd(object.toString());
				if (object instanceof TechOrder) {
					mOrder = (TechOrder) object;
					if (!isDetached()) {
						mServiceDetailHandler.removeMessages(MSG_DATA);
						mServiceDetailHandler.sendEmptyMessage(MSG_DATA);
					}
				} else if (object instanceof ResultInfo) {
					ResultInfo info = (ResultInfo) object;
					if(info.getmCode() == 0){
						if(mType == 3){
	            			if(info.getmCode()==0){
	        					showSmartToast(R.string.delete_success, Toast.LENGTH_SHORT);
	        					mActivity.finish();
	        					ManagerListener.newManagerListener().notifyOrderPayListener(mOrder);
	        				}else{
	        					if(info.getmMessage()!=null&&!TextUtils.isEmpty(info.getmMessage())&&!"null".equals(info.getmMessage())){
	        						showSmartToast(info.getmMessage(), Toast.LENGTH_SHORT);
	        					}else{
	        						showSmartToast(R.string.delete_error, Toast.LENGTH_SHORT);
	        					}
	        				}
	            		}else{
	            			ManagerListener.newManagerListener().notifyOrderPayListener(mOrder);
	            			mType = 0;
							startReqTask(TechOrderDetailFragment.this);
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
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				AppLog.Loge(" data failed to load" + error.getMessage());
			}
		};
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onOrderDetailRateListener(TechOrder order) {// 技师完成服务
		mType = 1;
		mOrder = order;
		if (!Utility.isSDCardExist(getActivity())) {
			AlertDialogUtils.displayAlert(getActivity(),
					getString(R.string.operate_failed),
					getString(R.string.error_nosdcard),
					getString(R.string.confirm));
		} else {
			mDialog = new TechFinishDialog(getActivity(), null,1);
			mDialog.show();
			mDialog.setCanceledOnTouchOutside(true);
		}
	}

	@Override
	public void onOrderDetailLogiticsListener(TechOrder order) {// 技师开始服务
		mType = 2;
		mOrder = order;
		startReqTask(TechOrderDetailFragment.this);
	}

	@Override
	public void onOrderDetailDeleteListener(TechOrder order) {
		mType = 3;
		mOrder = order;
		startReqTask(TechOrderDetailFragment.this);
	}

	@Override
	public void onOrderDetailCancelListener(TechOrder order) {
		mOrder = order;
		mType = 4;
		startReqTask(TechOrderDetailFragment.this);
	}

	@Override
	public void onOrderDetailPayListener(TechOrder order) {// 技师联系客服
		mType = 6;
		mOrder = order;
	}

	@Override
	public void onOrderDetailTakeListener(TechOrder order) {
		mType = 5;
		mOrder = order;
		startReqTask(TechOrderDetailFragment.this);
	}

	private void goCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mPhotoPath = Uri.fromFile(FileTool.createTempFile(
				getNowTimeWithNoSeparator(), ".jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoPath);
		startActivityForResult(intent, TAKE_BIG_LOCAL_PICTURE);
	}

	public String getNowTimeWithNoSeparator() {
		return new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
				.format(new Date());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constant.LOGIN_SUCCESS) {
			// setView();
			// ManagerListener.newManagerListener().notifyRefreshListener();
		}
		super.onActivityResult(requestCode, resultCode, data);
		String path = null;
		Uri mImageUri = null;
		if (data != null) {
			mImageUri = data.getData();
			if (mImageUri != null) {
				ContentResolver mContentResolver = getActivity()
						.getContentResolver();
				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);
				mCursor.moveToFirst();
				if (mCursor.getColumnIndex(MediaStore.Images.Media.DATA) != -1) {
					try {
						path = mCursor.getString(mCursor
								.getColumnIndex(MediaStore.Images.Media.DATA));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case IMAGE_TYPE_CAMERA:
				mSelectItems = null;
				if (path != null) {
					mSelectItems = path;
					Intent mediaScanIntent = new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					mediaScanIntent.setData(mImageUri);
					getActivity().sendBroadcast(mediaScanIntent);
					getActivity().sendBroadcast(
							new Intent(Constant.REFRESH_UPLOAD_GRIDVIEW_IMAGE)
									.putExtra(IntentBundleKey.REDIRECT_TYPE,
											true));
				}
				break;
			case TAKE_BIG_LOCAL_PICTURE:
				cropImageUri(mPhotoPath, IMAGE_TYPE_CAMERA);
				break;
			case TAKE_BIG_PICTURE:
				if (data != null) {
					mImageUri = data.getData();
					if (android.os.Build.VERSION.SDK_INT >= 19) {
						path = getPath(getActivity(), mImageUri);
						mImageUri = Uri.fromFile(new File(path));
					}
				}
				cropImageUri(mImageUri, UPLOAD_IMAGE);
				break;
			case UPLOAD_IMAGE:
				mSelectItems = null;
				if (path != null) {
					mSelectItems = path;
				}
				if (!TextUtils.isEmpty(mSelectItems)) {
					mDialog = new TechFinishDialog(getActivity(), "file:///"
							+ mSelectItems,1);
					mDialog.show();
					mDialog.setCanceledOnTouchOutside(true);
				}
				break;

			default:
				break;
			}
		}
	}

	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	private void cropImageUri(Uri uri, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 450);
		intent.putExtra("outputY", 450);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	@Override
	public void onDetailCamera() {
		goCamera();
	}

	@Override
	public void onDetailPhoto() {
		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		getImage.setType("image/*");
		startActivityForResult(getImage, TAKE_BIG_PICTURE);
	}

	@Override
	public void onDetailSubmit(String url) {
		if (url != null) {
			startReqTask(TechOrderDetailFragment.this);
		}
	}

}
