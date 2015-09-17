package com.techfly.liutaitai.model.mall.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.model.mall.parser.PicTextDetailParser;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class PicAndTextDetailFragment extends CommonFragment {

	private CommonFragment mFragment;

	private TextView mWebView;
	private LinearLayout mGroup;
	private String mDesc = "暂无";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mFragment = this;
		Intent intent = getActivity().getIntent();
		mDesc = intent.getStringExtra(JsonKey.AdvertisementKey.GOODSID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_pic_text, container, false);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//		if (mWebView != null) {
//			 mWebView.getSettings().setLoadWithOverviewMode(false);
//		     mWebView.getSettings().setUseWideViewPort(false);
//		     mWebView.getSettings().setJavaScriptEnabled(false);
//		     mWebView.getSettings().setSupportZoom(false);
//		     mWebView.getSettings().setBuiltInZoomControls(false);
//		     mWebView.destroy();
//		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		initHeader();
		initViews(view);
//		startReqTask(this);
	}

	private void initHeader() {
		// TODO Auto-generated method stub
		setTitleText(R.string.pic_and_text);
		setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
//		setRightMoreIcon(R.drawable.share, new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent shareIntent = new Intent(Intent.ACTION_SEND);
//				shareIntent.setType("text/plain");
//				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享易汇");
//				shareIntent.putExtra(Intent.EXTRA_TEXT, "分享易汇的内容");
//				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(Intent.createChooser(shareIntent, "分享易汇"));
//			}
//		});
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		// TODO Auto-generated method stub
		mGroup = (LinearLayout) view.findViewById(R.id.pic_text_group);
		mWebView = (TextView) view.findViewById(R.id.pic_text_webview);
//		mWebView.setWebViewClient(getClient());
//		mWebView.getSettings().setLoadWithOverviewMode(true);
//		mWebView.getSettings().setUseWideViewPort(true);
//		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.getSettings().setSupportZoom(false);
//		mWebView.getSettings().setBuiltInZoomControls(false);
//		mWebView.getSettings()
//				.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 可能的话不要超过屏幕宽度
//		mWebView.setInitialScale(150);
//		AppLog.Logd("Fly", "mDesc==="+mDesc);
//		mWebView.getSettings().setDefaultTextEncodingName("UTF-8");  
//		mWebView.loadData(Html.fromHtml(mDesc).toString(), "text/html", "UTF-8");
		mWebView.setText(Html.fromHtml(mDesc));
	}

	private WebViewClient getClient() {
		// TODO Auto-generated method stub
		WebViewClient mClient = new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				startLoading(mFragment);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
			}

		};
		return mClient;
	}

	@Override
	public void requestData() {
		// TODO Auto-generated method stub
		RequestParam param = new RequestParam();
		HttpURL url = new HttpURL();
		url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.GOODS_PIC_TEXT
				+ mDesc);
		param.setmHttpURL(url);
		param.setmParserClassName(PicTextDetailParser.class.getName());
		RequestManager
				.getRequestData(getActivity(), createMyReqSuccessListener(),
						createMyReqErrorListener(), param);
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
//				mLoadHandler.removeMessages(Constant.NET_SUCCESS);
//				mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				if (object instanceof String) {
					String html = (String) object;
					if (!TextUtils.isEmpty(html)) {
						Html.fromHtml(html).toString();
//						mWebView.loadDataWithBaseURL(null, html, "text/html",
//								"UTF-8", null);
					} else {
						SmartToast.makeText(getActivity(), "未能获取到正确数据",
								Toast.LENGTH_SHORT).show();
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
				mLoadHandler.removeMessages(Constant.NET_FAILURE);
				mLoadHandler.sendEmptyMessage(Constant.NET_FAILURE);
				showMessage(R.string.loading_fail);
			}
		};
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ViewGroup view = (ViewGroup) getActivity().getWindow().getDecorView();
		view.removeAllViews();
	}
	
	

}
