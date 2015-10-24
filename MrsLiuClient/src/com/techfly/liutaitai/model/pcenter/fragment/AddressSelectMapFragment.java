package com.techfly.liutaitai.model.pcenter.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.adapter.ViewHolder;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class AddressSelectMapFragment extends CommonFragment implements
		BDLocationListener, OnGetGeoCoderResultListener,
		OnInfoWindowClickListener {

	private ImageView mIvBack;
	private TextView mTvLocateLocation;
	private TextView mTvLocateDetailLocation;
	private EditText mEditText;
	private ListView mListView;
	private CommonAdapter<SuggestionResult.SuggestionInfo> mSugAdapter = null;
	private ArrayList<SuggestionResult.SuggestionInfo> mDatas = new ArrayList<SuggestionResult.SuggestionInfo>();
	private SuggestionSearch mSuggestionSearch = null;
	private OnGetSuggestionResultListener mSuggestionListener;

	private View mView;
	private LinearLayout mLlInfoWindow;

	public LocationClient mLocationClient = null;
	private BDLocation mBDLocation;
	private String mLatitude;
	private String mLongitude;
	private String mCity;
	private String mCityName;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarker;
	private InfoWindow mInfoWindow;
	private Projection mProjection;
	public GeoCoder mSearch = null;
	private MyLocationData mStartLocationData = null;
	private OnInfoWindowClickListener mListener = this;
	/**
	 * 当前地点击点
	 */
	private LatLng currentPt;
	public BDLocationListener myListener = this;
	public Handler mHandler = new Handler() {

	};

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (mBDLocation != null && mBDLocation.getLatitude() != 0.0
					&& mBDLocation.getLongitude() != 0.0) {
				LatLng latLng = new LatLng(mBDLocation.getLatitude(),
						mBDLocation.getLongitude());
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(latLng));
			} else {
				mHandler.postDelayed(runnable, 1000);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mCity = SharePreferenceUtils.getInstance(getActivity()).;
		mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
		initLocationOption();
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

	}

	private void initLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setOpenGps(true);
		mLocationClient.setLocOption(option);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_addr_select_map, container,
				false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSearch.destroy();
		// 退出时销毁定位
		if (mLocationClient != null) {
			mLocationClient.stop();
		}
		mMapView.onDestroy();
		mMapView = null;
		if (mBaiduMap != null) {
			mBaiduMap = null;
		}
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		requestData();

	}

	private void initViews(View view) {
		mIvBack = (ImageView) view
				.findViewById(R.id.addr_select_map_header_left_icon);
		mIvBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});

		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionListener = new OnGetSuggestionResultListener() {

			@Override
			public void onGetSuggestionResult(SuggestionResult res) {
				if (res == null || res.getAllSuggestions() == null) {
					mDatas.clear();
					mListView.setVisibility(View.GONE);

					return;
				}

				mDatas.clear();
				mDatas.addAll(res.getAllSuggestions());
				mListView.setVisibility(View.VISIBLE);
				mSugAdapter.notifyDataSetChanged();

			}
		};
		mSuggestionSearch.setOnGetSuggestionResultListener(mSuggestionListener);

		mEditText = (EditText) view.findViewById(R.id.addr_select_map_et);
		mEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() <= 0) {
					mListView.setVisibility(View.GONE);
					return;
				}

				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(s.toString()).city(mCity));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(
								v.getApplicationWindowToken(), 0);
					}

					mSuggestionSearch
							.requestSuggestion((new SuggestionSearchOption())
									.keyword(mEditText.getText().toString())
									.city(mCity));

					return true;
				}
				return false;
			}
		});
		mSugAdapter = new CommonAdapter<SuggestionResult.SuggestionInfo>(
				getActivity(), mDatas, R.layout.item_poi_search) {

			@Override
			public void convert(ViewHolder holder, SuggestionInfo item,
					int position) {
				holder.setText(R.id.poi_search_item_tv_name, item.key);

			}
		};
		mListView = (ListView) view.findViewById(R.id.addr_select_map_list);
		mListView.setAdapter(mSugAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mListView.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();
				if (mMarker != null) {
					mMarker.remove();
				}
				// mBaiduMap.
				LatLng point = mDatas.get(position).pt;
				if (point == null) {
					SmartToast.makeText(getActivity(), "找不到该结果的位置，请选择其他结果",
							Toast.LENGTH_SHORT).show();
					return;
				}
				mLatitude = point.latitude + "";
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(point));

				// 构建Marker图标
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_location_big);
				OverlayOptions option = new MarkerOptions().position(point)
						.icon(bitmap);
				// 在地图上添加Marker，并显示
				mMarker = (Marker) mBaiduMap.addOverlay(option);
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
						.newLatLngZoom(point, 16);
				mBaiduMap.animateMapStatus(mapStatusUpdate);
				showInfoWindow();

			}
		});

		mTvLocateLocation = (TextView) view
				.findViewById(R.id.addr_select_map_tv_locate_addr);
		mTvLocateDetailLocation = (TextView) view
				.findViewById(R.id.addr_select_map_tv_locate_detail_addr);

		mView = LayoutInflater.from(getActivity()).inflate(
				R.layout.addr_select_map_info_window, null);
		mLlInfoWindow = (LinearLayout) mView
				.findViewById(R.id.info_window_parent);

		mMapView = (MapView) view.findViewById(R.id.addr_select_map_mapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
				if (mMarker != null) {
					mMarker.remove();
				}
				currentPt = arg0;
				mLatitude = currentPt.latitude + "";
				mLongitude = currentPt.longitude + "";
				requestData();
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(currentPt));

				// mBaiduMap.
				LatLng point = new LatLng(currentPt.latitude,
						currentPt.longitude);
				// 构建Marker图标
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.icon_location_big);
				OverlayOptions option = new MarkerOptions().position(point)
						.icon(bitmap);
				// 在地图上添加Marker，并显示
				mMarker = (Marker) mBaiduMap.addOverlay(option);
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
						.newLatLngZoom(point, 16);
				mBaiduMap.animateMapStatus(mapStatusUpdate);
				showInfoWindow();

			}
		});
		mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				mLocationClient.start();

			}
		});

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		if (mBDLocation == null) {
			mHandler.postDelayed(runnable, 1000);
		} else {
			LatLng latLng = new LatLng(mBDLocation.getLatitude(),
					mBDLocation.getLongitude());
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
		}

	}

	@Override
	public void requestData() {
		// if (mBDLocation != null && mBDLocation.getLatitude() != 0.0
		// && mBDLocation.getLongitude() != 0.0) {
		// RequestParam param = new RequestParam();
		// HttpURL url = new HttpURL();
		// url.setmBaseUrl(Constant.BASE_URL + Constant.CIRCLE_MAP_URL);
		// url.setmGetParamPrefix(JsonKey.CircleKey.CITYID)
		// .setmGetParamValues(mCity);
		// url.setmGetParamPrefix(JsonKey.CityKey.LAT).setmGetParamValues(
		// mLatitude);
		// url.setmGetParamPrefix(JsonKey.CityKey.LON).setmGetParamValues(
		// mLongitude);
		// param.setmHttpURL(url);
		// // param.setmParserClassName(CircleParser.class.getName());
		// param.setmParserClassName(MapCircleParser.class.getName());
		// RequestManager.getRequestData(getActivity(),
		// createMyReqSuccessListener(), createMyReqErrorListener(),
		// param);
		// } else {
		// mHandler.postDelayed(runnable, 1000);
		// }
	}

	private Response.Listener<Object> createMyReqSuccessListener() {
		return new Listener<Object>() {
			@Override
			public void onResponse(Object object) {
				// AppLog.Logd(object.toString());
				// if (!isDetached()) {
				// mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				// mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				// }
				//
				// if (object instanceof Circle) {
				// Circle circle = (Circle) object;
				// if (circle != null) {
				// mCircle = circle;
				// if (!TextUtils.isEmpty(circle.getmId())) {
				// mTvCircle.setText("<" + circle.getmName() + ">");
				// } else {
				// mTvCircle.setText("当前位置暂无商圈");
				// }
				// mCir = circle;
				// }
				// showInfoWindow();
				// } else {
				// mTvCircle.setText("当前位置暂无商圈");
				// }

				// ArrayList<Circle> list=(ArrayList<Circle>) object;
				// Circle circle=new Circle();
				// circle.setNear(true);
				// if(list.size()>0){
				// if(list.indexOf(circle)!=-1){
				// mCir=list.get(list.indexOf(circle));
				// AppLog.Loge("xll", mCir.getmName());
				// }
				// mCircle.setText(mCir.getmName());
				// }else{
				// mCircle.setText(R.string.map_hint);
				// }

			}
		};
	}

	protected void showInfoWindow() {
		LatLng llInfo = mMarker.getPosition();
		// Point p = mBaiduMap.getProjection().toScreenLocation(llInfo);
		// if (!isFIrst) {
		// p.y = p.y - 120;
		// p.x = p.x + 70;
		// } else {
		// isFIrst = false;
		// p.y = p.y - 12;
		// p.x = p.x + 7;
		// }

		mInfoWindow = new InfoWindow(
				BitmapDescriptorFactory.fromView(mLlInfoWindow), llInfo, -120,
				mListener);

		mBaiduMap.showInfoWindow(mInfoWindow);

	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				// AppLog.Loge(" data failed to load" + error.getMessage());
				// if (!isDetached()) {
				// mLoadHandler.removeMessages(Constant.NET_SUCCESS);
				// mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
				// SmartToast.makeText(getActivity(),
				// R.string.error_for_request, Toast.LENGTH_SHORT)
				// .show();
				// }
			}
		};
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		mBDLocation = location;
		if (location == null) {
			return;
		} else {
			mLatitude = location.getLatitude() + "";
			mLongitude = location.getLongitude() + "";
			mCity = location.getCity();
			AppLog.Logd("Shi", "mCity:::" + mCity);
			mStartLocationData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(mStartLocationData);
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
			LatLng point = new LatLng(location.getLatitude(),
					location.getLongitude());
			// 构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_location_big);
			OverlayOptions option = new MarkerOptions().position(point).icon(
					bitmap);
			// 在地图上添加Marker，并显示
			mMarker = (Marker) mBaiduMap.addOverlay(option);
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newLatLngZoom(point, 16);
			mBaiduMap.animateMapStatus(mapStatusUpdate);
			showInfoWindow();

			if (mLocationClient != null) {
				mLocationClient.stop();
			}
		}
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
		}
		// List<PoiInfo> list = arg0.getPoiList();
		// if (list != null && list.size() > 0) {
		// mTvLocateLocation.setText(arg0.getAddress());
		// } else {
		// mTvLocateLocation.setText(arg0.getAddress());
		// }
		mTvLocateLocation.setText(arg0.getAddress());
		mTvLocateDetailLocation.setText(arg0.getAddress());
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {

		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
		}
		List<PoiInfo> list = arg0.getPoiList();
		if (list != null && list.size() > 0) {
			mTvLocateLocation.setText(arg0.getPoiList().get(0).name);
		} else {
			mTvLocateLocation.setText(arg0.getBusinessCircle());
		}
		mTvLocateDetailLocation.setText(arg0.getAddress());
		// startReqTask(AddressSelectMapFragment.this);

	}

	@Override
	public void onInfoWindowClick() {

		Intent intent = new Intent();
		intent.putExtra(IntentBundleKey.ADDRESS_EXTRA, mTvLocateLocation
				.getText().toString());
		if (mMarker != null) {
			intent.putExtra(IntentBundleKey.LAT_LNG_EXTRA_LAT,
					mMarker.getPosition().latitude);
			intent.putExtra(IntentBundleKey.LAT_LNG_EXTRA_LNG,
					mMarker.getPosition().longitude);

		}
		getActivity().setResult(Activity.RESULT_OK, intent);
		getActivity().finish();
	}

	public void onBackPressed() {
		if (mListView.getVisibility() == View.VISIBLE) {
			mListView.setVisibility(View.GONE);
		} else {
			getActivity().finish();
		}
	}

}
