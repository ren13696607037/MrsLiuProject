package com.techfly.liutaitai.model.pcenter.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.parser.OrderListParser;
import com.techfly.liutaitai.bizz.parser.TechOrderParser;
import com.techfly.liutaitai.model.pcenter.activities.OrderDetailActivity;
import com.techfly.liutaitai.model.pcenter.activities.RateActivity;
import com.techfly.liutaitai.model.pcenter.activities.SearchLogisticsActivity;
import com.techfly.liutaitai.model.pcenter.adapter.MyOrderAdapter;
import com.techfly.liutaitai.model.pcenter.bean.MyOrder;
import com.techfly.liutaitai.model.pcenter.bean.TechOrder;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.FileTool;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.ManagerListener;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.ManagerListener.OrderCancelListener;
import com.techfly.liutaitai.util.ManagerListener.OrderDeleteListener;
import com.techfly.liutaitai.util.ManagerListener.OrderLogiticsListener;
import com.techfly.liutaitai.util.ManagerListener.OrderPayListener;
import com.techfly.liutaitai.util.ManagerListener.OrderRateListener;
import com.techfly.liutaitai.util.ManagerListener.OrderTakeListener;
import com.techfly.liutaitai.util.ManagerListener.TechFinishDialogListener;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.fragment.CreateOrderPayCommonFragment;
import com.techfly.liutaitai.util.fragment.OrderPayFragment;
import com.techfly.liutaitai.util.view.TechFinishDialog;
import com.techfly.liutaitai.util.view.XListView;
import com.techfly.liutaitai.util.view.XListView.IXListViewListener;

public class MyOrderAllFragment extends CreateOrderPayCommonFragment implements OnItemClickListener,IXListViewListener,OrderCancelListener,OrderDeleteListener,OrderLogiticsListener,OrderRateListener,OrderPayListener,OrderTakeListener,TechFinishDialogListener{
	private TextView mTextView;
	private XListView mListView;
	private ArrayList<TechOrder> mList=new ArrayList<TechOrder>();
	private MyOrderAdapter mAdapter;
	private final int MSG_LIST=0x101;
	private final int MSG_DLELTE=0x102;
	private final int MSG_CANCEL=0x103;
	private int mPage=1;
	private int mSize=10;
	private boolean isRefresh=true;
	private ResultInfo mInfo;
	private TechOrder mOrder;
	private User mUser;
	private int mType;
	private TechFinishDialog mDialog;
	private static final int TAKE_BIG_PICTURE = 0x901;
	private static final int TAKE_BIG_LOCAL_PICTURE = TAKE_BIG_PICTURE + 1;
	private static final int UPLOAD_IMAGE = TAKE_BIG_LOCAL_PICTURE + 1;
	// 获取照片type
	public static final int IMAGE_TYPE_CAMERA = 0; // 摄像头拍照页面
	private String mSelectItems;
	private Uri mPhotoPath;
	BroadcastReceiver mImageWallChangeBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getBooleanExtra(IntentBundleKey.REDIRECT_TYPE, false)
					&& null != intent
							.getStringExtra(IntentBundleKey.IMAGE_PATH)) {
				AppLog.Loge("xll", intent
						.getStringExtra(IntentBundleKey.IMAGE_PATH));
				mSelectItems = intent
						.getStringExtra(IntentBundleKey.IMAGE_PATH);
			}
			if (!TextUtils.isEmpty(mSelectItems)) {
				mDialog = new TechFinishDialog(getActivity(), "file:///"+mSelectItems);
				mDialog.show();
				mDialog.setCanceledOnTouchOutside(true);
			}
		}
	};
	public Handler mAllHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LIST:
				if(mList.size()==0){
					setNoData();
				}
				mAdapter.updateList(mList);
				break;
			case MSG_DLELTE:
				if(mInfo.getmCode()==0){
					showSmartToast(R.string.delete_success, Toast.LENGTH_SHORT);
					startReqTask(MyOrderAllFragment.this);
				}else{
					if(mInfo.getmMessage()!=null&&!TextUtils.isEmpty(mInfo.getmMessage())&&!"null".equals(mInfo.getmMessage())){
						showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
					}else{
						showSmartToast(R.string.delete_error, Toast.LENGTH_SHORT);
					}
				}
				break;
			case MSG_CANCEL:
				if(mInfo.getmCode()==0){
					showSmartToast(R.string.cancel_success, Toast.LENGTH_SHORT);
					startReqTask(MyOrderAllFragment.this);
				}else{
					if(mInfo.getmMessage()!=null&&!TextUtils.isEmpty(mInfo.getmMessage())&&!"null".equals(mInfo.getmMessage())){
						showSmartToast(mInfo.getmMessage(), Toast.LENGTH_SHORT);
					}else{
						showSmartToast(R.string.cancel_error, Toast.LENGTH_SHORT);
					}
				}
				break;
			default:
				break;
			}
		}
		
	};
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = SharePreferenceUtils.getInstance(getActivity()).getUser();
        startReqTask(MyOrderAllFragment.this);
        ManagerListener.newManagerListener().onRegisterOrderDeleteListener(this);
        ManagerListener.newManagerListener().onRegisterOrderLogiticsListener(this);
        ManagerListener.newManagerListener().onRegisterOrderRateListener(this);
        ManagerListener.newManagerListener().onRegisterOrderCancelListener(this);
        ManagerListener.newManagerListener().onRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onRegisterOrderTakeListener(this);
        ManagerListener.newManagerListener().onRegisterTechFinishDialogListener(this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderall, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManagerListener.newManagerListener().onUnRegisterOrderDeleteListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderLogiticsListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderRateListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderCancelListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderPayListener(this);
        ManagerListener.newManagerListener().onUnRegisterOrderTakeListener(this);
        ManagerListener.newManagerListener().onUnRegisterTechFinishDialogListener(this);
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
    
    private void onInitView(View view){
    	IntentFilter intentFilter = new IntentFilter(
				Constant.REFRESH_UPLOAD_GRIDVIEW_IMAGE);
		getActivity().registerReceiver(mImageWallChangeBroadcastReceiver,
				intentFilter);
    	mListView=(XListView) view.findViewById(R.id.all_list);
    	mTextView=(TextView) view.findViewById(R.id.all_text);
    	mTextView.setText(R.string.order_pay_text);
    	mAdapter=new MyOrderAdapter(getActivity(), mList);
    	mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
    }

	@Override
	public void requestData() {
		if(mType == 1){
			toDone();
		}else{
			RequestParam param = new RequestParam();
	        HttpURL url = new HttpURL();
			if(mType == 3){
		        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_REMOVE_URL);
		        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
		        param.setmParserClassName(CommonParser.class.getName());
			}else if(mType == 4){
		        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_REFRSE_URL);
		        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
		        param.setmParserClassName(CommonParser.class.getName());
			}else if(mType == 2){
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_START_URL);
		        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
		        param.setmParserClassName(CommonParser.class.getName());
			}else if(mType == 5){
				url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_TAKE_URL);
		        url.setmGetParamPrefix(JsonKey.ServiceKey.RID).setmGetParamValues(mOrder.getmId());
		        param.setmParserClassName(CommonParser.class.getName());
			}else{
		        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.TECH_ORDER_LIST_URL);
		        url.setmGetParamPrefix(JsonKey.TechnicianKey.TYPE).setmGetParamValues("0");
		        url.setmGetParamPrefix(JsonKey.MyOrderKey.SIZE).setmGetParamValues(mSize+"");
		        url.setmGetParamPrefix(JsonKey.VoucherKey.PAGE).setmGetParamValues(mPage+"");
		        param.setmParserClassName(TechOrderParser.class.getName());
			}
			param.setmIsLogin(true);
			param.setmId(mUser.getmId());
			param.setmToken(mUser.getmToken());
			param.setPostRequestMethod();
	        param.setmHttpURL(url);
	        RequestManager.getRequestData(getActivity(), createMyReqSuccessListener(), createMyReqErrorListener(), param);
		}
    }
	private void toDone(){
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
										startReqTask(MyOrderAllFragment.this);
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
                AppLog.Logd(object.toString());
                if(!isDetached()){
                	if(object instanceof ArrayList){
                		ArrayList<TechOrder> list=(ArrayList<TechOrder>) object;
                        if(isRefresh){
                        	mList.addAll(list);
                        }else{
                        	mList.clear();
                        	mList.addAll(list);
                        }
                        if (list == null || list.size() == 0) {
                        	
        				} else if (list.size() < 10) {
        					mListView.setVisibility(View.VISIBLE);
        				    mTextView.setVisibility(View.GONE);
        					mListView.setPullLoadEnable(false);
        				} else {
        					mListView.setVisibility(View.VISIBLE);
        					mTextView.setVisibility(View.GONE);
        					mListView.setPullLoadEnable(true);
        				}
        				mListView.stopLoadMore();
        				mListView.stopRefresh();
                    	mAllHandler.removeMessages(MSG_LIST);
                    	mAllHandler.sendEmptyMessage(MSG_LIST);
                	}else if(object instanceof ResultInfo){
                		ResultInfo info = (ResultInfo) object;
                		if(mType == 1){
                			if(info.getmCode() == 0){
                				mType = 0;
                				startReqTask(MyOrderAllFragment.this);
                			}
                		}else if(mType == 2){
                			if(info.getmCode() == 0){
                				mType = 0;
                				startReqTask(MyOrderAllFragment.this);
                			}
                		}else if(mType == 3){
                			if(info.getmCode() == 0){
                				mType = 0;
                				startReqTask(MyOrderAllFragment.this);
                			}
                		}else if(mType == 4){
                			if(info.getmCode() == 0){
                				mType = 0;
                				startReqTask(MyOrderAllFragment.this);
                			}
                		}else if(mType == 5){
                			if(info.getmCode() == 0){
                				mType = 0;
                				startReqTask(MyOrderAllFragment.this);
                			}
                		}
                	}
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
                if(!isDetached()){
                    mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                    mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                }
            }
       };
    }
    private void setNoData(){
		mListView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(getResources().getString(R.string.order_all_text));
	}

	@Override
	public void onRefresh() {
		mAllHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPage = 1;
				mList.clear();
				requestData();
			}
		}, 0);
	}

	@Override
	public void onLoadMore() {
		mAllHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPage += 1;
				requestData();
			}
		}, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TechOrder order=(TechOrder) parent.getAdapter().getItem(position);
		Intent intent=new Intent(getActivity(),OrderDetailActivity.class);
		intent.putExtra(IntentBundleKey.ORDER_ID, order.getmId());
		startActivityForResult(intent, Constant.DETAIL_INTENT);
	}

	@Override
	public void onOrderRateListener(TechOrder order) {//技师完成服务
		mType = 1;
		mOrder = order;
		if (!Utility.isSDCardExist(getActivity())) {
			AlertDialogUtils.displayAlert(getActivity(),
					getString(R.string.operate_failed),
					getString(R.string.error_nosdcard),
					getString(R.string.confirm));
		} else {
			mDialog = new TechFinishDialog(getActivity(), null);
			mDialog.show();
			mDialog.setCanceledOnTouchOutside(true);
		}
	}

	@Override
	public void onOrderLogiticsListener(TechOrder order) {//技师开始服务
		mType = 2;
		mOrder=order;
		isRefresh=false;
		startReqTask(MyOrderAllFragment.this);
//		Intent intent=new Intent(getActivity(),SearchLogisticsActivity.class);
//		intent.putExtra(IntentBundleKey.ORDER_ID, order);
//		startActivity(intent);
	}

	@Override
	public void onOrderDeleteListener(TechOrder order) {
		mType = 3;
		mOrder=order;
		isRefresh=false;
		startReqTask(MyOrderAllFragment.this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(mList.size()==0){
			isRefresh=false;
			startReqTask(MyOrderAllFragment.this);
		}else{
			
		}
	}

	@Override
	public void onOrderCancelListener(TechOrder order) {
		mOrder=order;
		isRefresh=false;
		mType = 4;
		startReqTask(MyOrderAllFragment.this);
	}

	@Override
	public String onEncapleOrderInfo(HttpURL url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onOrderCreateSuccess(String orderId, String money,
			String proName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOrderPayListener(TechOrder order) {//技师联系客服
		mType = 6;
		mOrder = order;
	}

	@Override
	public void onOrderTakeListener(TechOrder order) {
		mType = 5;
		mOrder = order;
		startReqTask(MyOrderAllFragment.this);
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
//			setView();
//			ManagerListener.newManagerListener().notifyRefreshListener();
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
				if (mCursor.getColumnIndex(MediaStore.Images.Media.DATA) !=-1) {
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
					mDialog = new TechFinishDialog(getActivity(), "file:///"+mSelectItems);
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
	public void onCamera() {
		goCamera();
	}

	@Override
	public void onPhoto() {
		Intent getImage = new Intent(
				Intent.ACTION_GET_CONTENT);
		getImage.setType("image/*");
		startActivityForResult(getImage,
				TAKE_BIG_PICTURE);
	}

	@Override
	public void onSubmit(String url) {
		if(url != null){
			startReqTask(MyOrderAllFragment.this);
		}
	}


}
