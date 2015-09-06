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
import android.app.Dialog;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.techfly.liutaitai.bizz.parser.AddressManageParser;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.shopcar.OnShopCarLisManager;
import com.techfly.liutaitai.model.pcenter.activities.AddressManageActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangeNickActivity;
import com.techfly.liutaitai.model.pcenter.activities.ChangePassActivity;
import com.techfly.liutaitai.model.pcenter.activities.ForgetNextActivity;
import com.techfly.liutaitai.model.pcenter.activities.PcenterInfoActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.CircleImageView;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.FileTool;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.Utility;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class PcenterInfoFragment extends CommonFragment implements OnClickListener{
	private PcenterInfoActivity mActivity;
	private RelativeLayout mRlNick;
	private RelativeLayout mRlChange;
	private CircleImageView mCircleImageView;
	private User mUser;
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
			if (!intent.getBooleanExtra(IntentBundleKey.REDIRECT_TYPE, false)
					&& null != intent
							.getStringExtra(IntentBundleKey.IMAGE_PATH)) {
				mSelectItems = intent
						.getStringExtra(IntentBundleKey.IMAGE_PATH);
			}
			if (!TextUtils.isEmpty(mSelectItems)) {
				startReqTask(PcenterInfoFragment.this);
			}
		}
	};
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(PcenterInfoActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
//        startReqTask(PcenterInfoFragment.this);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pcenterinfo, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mImageWallChangeBroadcastReceiver);
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
    	setTitleText(R.string.pinfo_title);
    	setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    	IntentFilter intentFilter = new IntentFilter(
				Constant.REFRESH_UPLOAD_GRIDVIEW_IMAGE);
		mActivity.registerReceiver(mImageWallChangeBroadcastReceiver,
				intentFilter);
    	mRlChange = (RelativeLayout) view.findViewById(R.id.info_change);
    	mRlNick = (RelativeLayout) view.findViewById(R.id.info_nick);
    	mCircleImageView = (CircleImageView) view.findViewById(R.id.info_header);
    	
    	mRlChange.setOnClickListener(this);
    	mRlNick.setOnClickListener(this);
    	mCircleImageView.setOnClickListener(this);
    	
    	ImageLoader.getInstance().displayImage(mUser.getmImage(), mCircleImageView);
    }

	@Override
	public void requestData() {
		RequestParams params = new RequestParams();
		params.addHeader("enctype", "multipart/form-data");
		params.addHeader("lt-token", mUser.getmToken());
		params.addHeader("lt-id", mUser.getmId());  
		params.addBodyParameter("img", new File(mSelectItems));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, Constant.YIHUIMALL_BASE_URL
				+ Constant.CHANGE_INFO_URL, params,
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
//										mUser.setmImage(obj.optString(JsonKey.DATA));
										mUser.setmImage("file:///"
												+ new File(mSelectItems)
														.getPath());
										SmartToast
												.makeText(
														getActivity(),
														getString(R.string.life_helper_send_success),
														Toast.LENGTH_LONG)
												.show();
									} else {
										SmartToast.makeText(getActivity(),
												obj.optString(JsonKey.MESSAGE),
												Toast.LENGTH_LONG).show();
									}
								}
								SharePreferenceUtils.getInstance(getActivity())
										.saveUser(mUser);
							} catch (JSONException e) {
								e.printStackTrace();
							}
//							setView();
						}
					}
				});
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.info_change:
			startActivity(new Intent(mActivity,ChangePassActivity.class));
			break;
		case R.id.info_nick:
			startActivity(new Intent(mActivity,ChangeNickActivity.class));
			break;
		case R.id.info_header:
			showDialog();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		mUser=SharePreferenceUtils.getInstance(mActivity).getUser();
//        startReqTask(PcenterInfoFragment.this);
	}
	private void showDialog() {
		if (!Utility.isSDCardExist(mActivity)) {
			AlertDialogUtils.displayAlert(mActivity,
					getString(R.string.operate_failed),
					getString(R.string.error_nosdcard),
					getString(R.string.confirm));
		} else {
			new AlertDialog.Builder(mActivity).setItems(
					new String[] {
							getString(R.string.life_helper_camera_photo),
							getString(R.string.life_helper_image_photo) },
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {
								goCamera();
							} else {
								Intent getImage = new Intent(
										Intent.ACTION_GET_CONTENT);
								getImage.setType("image/*");
								startActivityForResult(getImage,
										TAKE_BIG_PICTURE);

							}
						}
					}).show();
		}
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
				ContentResolver mContentResolver = mActivity
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
					startReqTask(this);
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

}
