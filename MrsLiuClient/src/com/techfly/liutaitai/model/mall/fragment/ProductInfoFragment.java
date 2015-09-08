package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.shopcar.CallBackNullException;
import com.techfly.liutaitai.bizz.shopcar.ProductCountException;
import com.techfly.liutaitai.model.mall.activities.StandardActivity;
import com.techfly.liutaitai.model.mall.bean.Comments;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.parser.NewProductInfoParser;
import com.techfly.liutaitai.model.pcenter.activities.RateListActivity;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.UIHelper;
import com.techfly.liutaitai.util.adapter.CommonAdapter;
import com.techfly.liutaitai.util.adapter.ViewHolder;
import com.techfly.liutaitai.util.fragment.CommonFragment;
import com.techfly.liutaitai.util.view.ListViewForScrollView;
import com.techfly.liutaitai.util.view.ProductUpdateView;
import com.techfly.liutaitai.util.view.ProductUpdateView.ShopCarCallBack;
import com.techfly.liutaitai.util.view.RollViewPager;
import com.techfly.liutaitai.util.view.RollViewPager.OnPagerClickCallback;

public class ProductInfoFragment extends CommonFragment implements
        ShopCarCallBack {

    public static final int FLAG_SECKILL = 4;// 限时秒杀
    public static final int FLAG_TUAN_GOU = 5;// 团购
    private static final int FLAG_GET_STANDARD = 0x110;
    private String mId;
    private Product mProduct;
    private CommonFragment mFragment;
    private Context mContext;

    private RollViewPager mViewPager;
    private TextView mPagerTitle;
    private LinearLayout mDotLinear;
    private RelativeLayout mPagerRelative;
    private ArrayList<View> dots; // 图片标题正文的那些点
    private List<ImageView> imageViews; // 滑动的图片集合
    private String[] titles;
    private ArrayList<String> mTitles;
    public ArrayList<String> mImageUrls;
    // private ArrayList<HomePager> mPagerDatas;

    private TextView mShopCar;
    private TextView mAddShopCar;
    private TextView mShopNow;
    private TextView mProductName;
    private TextView mProductPrice;
    private TextView mProductOldPrice;
    private TextView mProductRebate;
    private TextView mProductCollect;
    private LinearLayout mProductCollectLinear;
    private ImageView mProductCollectStar;
    private ProductUpdateView mProductUpdateCount;
    private TextView mProductStoreCount;
    private LinearLayout mProductStandardLinear;
    private TextView mProductStandard;
    private LinearLayout mProductListHeaderLinear;
    private TextView mProductEvaCount;
    private TextView mProductEvaPercent;
    private View view1, view2, view3;

    private ListViewForScrollView mListView;
    private ArrayList<Comments> mArrayList = new ArrayList<Comments>();
    private CommonAdapter<Comments> mAdapter;

    private TextView mPicAndTextDetail;
    private String mProductId = "1";
    private String mStandard;
    private boolean isSelectStandard = false;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mFragment = this;
        Intent intent = getActivity().getIntent();
        mProductId = intent.getStringExtra(IntentBundleKey.ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater
                .inflate(R.layout.fragment_productinfo, container, false);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initHeader();
        initViews(view);
        startReqTask(this);
    }

    private void initHeader() {
        // TODO Auto-generated method stub
        setTitleText(R.string.product_info);
        setRightMoreIcon(R.drawable.share, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享易汇");
                shareIntent.putExtra(Intent.EXTRA_TEXT, SharePreferenceUtils
                        .getInstance(getActivity()).getShareContent()
                        + SharePreferenceUtils.getInstance(getActivity())
                                .getShareUrl());
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(shareIntent, "分享易汇"));
            }
        });
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        initPager(view);
        mShopCar = (TextView) view.findViewById(R.id.product_info_shop_car);
        mShopCar.setEnabled(false);
        mShopCar.setOnClickListener(getClickListener());
        mAddShopCar = (TextView) view
                .findViewById(R.id.product_info_shop_car_add);
        mAddShopCar.setOnClickListener(getClickListener());
        mAddShopCar.setEnabled(false);
        mShopNow = (TextView) view.findViewById(R.id.product_info_shop_now);
        mShopNow.setOnClickListener(getClickListener());
        mShopNow.setEnabled(false);

        mProductName = (TextView) view
                .findViewById(R.id.product_info_product_name);
        mProductName.setOnClickListener(getClickListener());
        mProductName.setText("德芙巧克力");
        mProductPrice = (TextView) view.findViewById(R.id.product_info_price);
        mProductPrice.setText("￥54.00");
        mProductOldPrice = (TextView) view
                .findViewById(R.id.product_info_old_price);
        mProductOldPrice.setText("￥88.00");
        ;
        mProductRebate = (TextView) view.findViewById(R.id.product_info_rebate);
        mProductRebate.setText("折扣6.0折");
        mProductCollect = (TextView) view
                .findViewById(R.id.product_info_collect);
        mProductCollect.setText("收藏");
        mProductCollectLinear = (LinearLayout) view
                .findViewById(R.id.product_info_collect_linear);
        mProductCollectLinear.setOnClickListener(getClickListener());
        mProductCollectStar = (ImageView) view
                .findViewById(R.id.product_info_collect_star);

        mProductUpdateCount = (ProductUpdateView) view
                .findViewById(R.id.product_info_product_update_count);

        mProductStoreCount = (TextView) view
                .findViewById(R.id.product_info_store_count);
        mProductStoreCount.setText("库存1005件");
        mProductStandardLinear = (LinearLayout) view
                .findViewById(R.id.product_info_linear_standard);
        mProductStandardLinear.setOnClickListener(getClickListener());
        // mProductStandardLinear.setVisibility(View.GONE);
        mProductStandard = (TextView) view
                .findViewById(R.id.product_info_standard);
        mProductListHeaderLinear = (LinearLayout) view
                .findViewById(R.id.product_info_linear_list_header);
        mProductListHeaderLinear.setOnClickListener(getClickListener());
        mProductEvaCount = (TextView) view
                .findViewById(R.id.product_info_eva_count);
        mProductEvaPercent = (TextView) view
                .findViewById(R.id.product_info_eva_percent);

        mListView = (ListViewForScrollView) view
                .findViewById(R.id.product_info_listview);
        // mListView.setHeaderDividersEnabled(true);
        // mListView.setFooterDividersEnabled(true);
        mAdapter = new CommonAdapter<Comments>(getActivity(), mArrayList,
                R.layout.item_product_info) {

            @Override
            public void convert(ViewHolder holder, Comments item, int position) {
                // TODO Auto-generated method stub
                holder.setText(R.id.product_info_item_name, item.getmName());
                holder.setText(R.id.product_info_item__time, item.getmTime());
                holder.setText(R.id.product_info_item__content,
                        item.getmContent());

            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(getItemClickListener());

        mPicAndTextDetail = (TextView) view
                .findViewById(R.id.product_info_text_and_pic_detail);
        mPicAndTextDetail.setOnClickListener(getClickListener());

        view1 = (View) view.findViewById(R.id.product_info_view_1);
        view2 = (View) view.findViewById(R.id.product_info_view_2);
        view3 = (View) view.findViewById(R.id.product_info_view_3);

    }

    private OnItemClickListener getItemClickListener() {
        // TODO Auto-generated method stub
        OnItemClickListener mListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // TODO Auto-generated method stub

            }
        };
        return mListener;
    }

    private void initPager(View view) {
        // TODO Auto-generated method stub
        mTitles = new ArrayList<String>();
        mImageUrls = new ArrayList<String>();
        mPagerRelative = (RelativeLayout) view
                .findViewById(R.id.product_info_relative);
        LayoutParams params = (LayoutParams) mPagerRelative.getLayoutParams();
        params.height = Constant.SCREEN_WIDTH * 11 / 16;
        mPagerRelative.setLayoutParams(params);

        mDotLinear = (LinearLayout) view
                .findViewById(R.id.product_info_dot_linear);
        mViewPager = (RollViewPager) view
                .findViewById(R.id.product_info_viewpager);
        mViewPager.setPagerCallback(new OnPagerClickCallback() {

            @Override
            public void onPagerClick(int position) {
                UIHelper.toPicAndTextActivity(getActivity(), mProductId);
            }
        });
    }

    private void startRollPager() {
        if (mImageUrls != null) {
            if (dots == null) {
                dots = new ArrayList<View>();
            }
            dots.clear();
            mDotLinear.removeAllViews();
            for (int i = 0; i < mImageUrls.size(); i++) {
                ImageView dot = new ImageView(mContext);
                LayoutParams params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.leftMargin = 2;
                params.rightMargin = 2;
                dot.setLayoutParams(params);
                if (i != 0) {
                    dot.setBackgroundResource(R.drawable.ic_dot_unselected);
                } else {
                    dot.setBackgroundResource(R.drawable.ic_dot_selected);
                }
                mDotLinear.addView(dot);
                dots.add(dot);
            }
        }

        mViewPager.setUriList(mImageUrls);
        mViewPager.setDot(dots, R.drawable.ic_dot_selected,
                R.drawable.ic_dot_unselected);
        if (mViewPager.getAdapter() != null) {
            mViewPager.getAdapter().notifyDataSetChanged();
        }
        mViewPager.removeCallback();
        mViewPager.resetCurrentItem();
        mViewPager.setHasSetAdapter(false);
        mViewPager.startRoll();

    }

    private boolean authLogin() {
        User user = SharePreferenceUtils.getInstance(getActivity()).getUser();
        if (user != null && !TextUtils.isEmpty(user.getmId())) {
            int id = Integer.parseInt(user.getmId());
            if (id > 0) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    private OnClickListener getClickListener() {
        // TODO Auto-generated method stub
        OnClickListener mListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (v.getId()) {
                case R.id.product_info_shop_car:
                    UIHelper.toShopCarActivity(getActivity());
                    break;
                case R.id.product_info_shop_car_add:// 添加购物车
                    if (mProduct.getmStoreCount() <= 0) {
                        showSmartToast(R.string.store_count_not_enough,
                                Toast.LENGTH_LONG);
                        break;
                    }
                    if (mProductStandardLinear.getVisibility() == View.VISIBLE
                            && mProduct.getmProductId() == -1) {
                        SmartToast.makeText(getActivity(),
                                R.string.standard_unselect, Toast.LENGTH_SHORT)
                                .show();
                        break;
                    }
                    try {
                        mProductUpdateCount
                                .onReqPullToShopCart(ProductInfoFragment.this);
                    } catch (CallBackNullException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ProductCountException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case R.id.product_info_shop_now:// 立即购买
                    if (mProduct.getmStoreCount() <= 0) {
                        showSmartToast(R.string.store_count_not_enough,
                                Toast.LENGTH_LONG);
                        break;
                    }
                    if (mProductStandardLinear.getVisibility() == View.VISIBLE
                            && mProduct.getmProductId() == -1) {
                        SmartToast.makeText(getActivity(),
                                R.string.standard_unselect, Toast.LENGTH_SHORT)
                                .show();
                        break;
                    }
                    if (authLogin()) {
                        UIHelper.toTakingOrderActivity(getActivity(), mProduct);
                    } else {
                        UIHelper.toLoginActivity(getActivity());
                    }
                    break;
                case R.id.product_info_product_name:
                case R.id.product_info_text_and_pic_detail:
                    UIHelper.toPicAndTextActivity(getActivity(), mProductId);
                    break;
                case R.id.product_info_linear_list_header:// 跳转到评论界面
                    Intent intent = new Intent(getActivity(),
                            RateListActivity.class);
                    intent.putExtra(IntentBundleKey.PRODUCT_ID, mProduct);
                    startActivity(intent);
                    break;
                case R.id.product_info_linear_standard:
                    if (mProduct == null) {
                        break;
                    }
                    Intent i = new Intent();
                    i.setClass(getActivity(), StandardActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(IntentBundleKey.PRODUCT, mProduct);
                    i.putExtras(b);
                    startActivityForResult(i,
                            ProductInfoFragment.FLAG_GET_STANDARD);
                    // StandardDialog sd = new
                    // StandardDialog(ProductInfoFragment.this, mProduct);
                    // sd.show();
                    break;

                default:
                    break;
                }
            }
        };
        return mListener;
    }

    @Override
    public void requestData() {
        // TODO Auto-generated method stub

        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.GOODS_DETAIL);
        url.setmGetParamPrefix1("id");
        url.setmGetParamValues1(String.valueOf(mProductId));
        if (!TextUtils.isEmpty(mId)) {
            url.setmGetParamPrefix2("principal");
            url.setmGetParamValues2(mId);
        }
        // url.setmBaseUrl("http://www.hylapp.com:10001/apis/goods/detail?pid=1533");
        param.setmHttpURL(url);
        param.setmParserClassName(NewProductInfoParser.class.getName());
        // param.setmParserClassName(ProductInfoParser.class.getName());
        RequestManager
                .getRequestData(getActivity(), createMyReqSuccessListener(),
                        createMyReqErrorListener(), param);

    }

    private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                if (object instanceof Product) {
                    Product p = (Product) object;
                    if (p != null) {
                        mProduct = p;
                        mShopCar.setEnabled(true);
                        mShopNow.setEnabled(true);
                        mAddShopCar.setEnabled(true);
                        if (p.getmImgArray() == null
                                || p.getmImgArray().size() == 0) {
                            mPagerRelative.setVisibility(View.GONE);
                        } else {
                            mPagerRelative.setVisibility(View.VISIBLE);
                            mImageUrls.clear();
                            mImageUrls.addAll(p.getmImgArray());
                            startRollPager();
                        }
                        mProductUpdateCount.onAttachView(mProduct);
                        mProductName.setText(p.getmName());
                        mProductPrice
                                .setText(getString(R.string.product_info_money)
                                        + p.getmPrice());
                        mProductOldPrice
                                .setText(getString(R.string.product_info_money)
                                        + p.getmMarketPrice());
                        mProductRebate
                                .setText(getString(R.string.product_info_rebate_1)
                                        + p.getmRebate()
                                        + getString(R.string.product_info_rebate_2));
                        mProductStoreCount
                                .setText(getString(R.string.product_info_store_count_1)
                                        + p.getmStoreCount()
                                        + getString(R.string.product_info_store_count_2));
                        mProductEvaCount
                                .setText(getString(R.string.product_info_eva_count_1)
                                        + p.getmCommentCount()
                                        + getString(R.string.product_info_eva_count_2));
                        mProductEvaPercent
                                .setText(getString(R.string.product_info_eva_percent_1)
                                        + p.getmCommentReputably());

                        if (p.getmStandardClassList() == null
                                || p.getmStandardClassList().size() == 0) {
                            mProductStandardLinear.setVisibility(View.GONE);
                        } else {
                            mProductStandardLinear.setVisibility(View.VISIBLE);
                        }

                        if (mArrayList != null && p.getmCommentsList() != null) {
                            mArrayList.addAll(p.getmCommentsList());
                        }
                        if (mArrayList == null || mArrayList.size() == 0) {
                            mProductListHeaderLinear.setVisibility(View.GONE);
                            mListView.setVisibility(View.GONE);
                            view1.setVisibility(View.GONE);
                            view2.setVisibility(View.GONE);
                            view3.setVisibility(View.GONE);
                        } else {
                            mProductListHeaderLinear
                                    .setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.VISIBLE);
                            view1.setVisibility(View.VISIBLE);
                            view2.setVisibility(View.VISIBLE);
                            view3.setVisibility(View.VISIBLE);
                        }
                        if (p.ismIsCollect()) {
                            mProductCollectLinear
                                    .setBackgroundResource(R.drawable.shape_collect_pressed);
                            mProductCollect.setText("已收藏");
                            mProductCollect.setTextColor(getResources()
                                    .getColor(R.color.TextColorOrange));
                            mProductCollectStar
                                    .setImageResource(R.drawable.collect_focus);
                        } else {
                            mProductCollectLinear
                                    .setBackgroundResource(R.drawable.shape_collect_normal);
                            mProductCollect.setText("收藏");
                            mProductCollect.setTextColor(getResources()
                                    .getColor(R.color.color_blue));
                            mProductCollectStar
                                    .setImageResource(R.drawable.collect_nor);
                        }
                    }

                } else if (object instanceof Integer) {
                    int code = (Integer) object;
                    if (code == 0x111) {
                        SmartToast.makeText(getActivity(),
                                R.string.product_info_collect_success,
                                Toast.LENGTH_SHORT).show();
                        mProductCollectLinear
                                .setBackgroundResource(R.drawable.shape_collect_pressed);
                        mProductCollect.setText("已收藏");
                        mProductCollect.setTextColor(getResources().getColor(
                                R.color.TextColorOrange));
                        mProductCollectStar
                                .setImageResource(R.drawable.collect_focus);
                        if (mProduct != null) {
                            mProduct.setmIsCollect(true);
                        }
                    } else if (code == 0x112) {
                        SmartToast.makeText(getActivity(),
                                R.string.product_info_delete_collect_success,
                                Toast.LENGTH_SHORT).show();
                        mProductCollectLinear
                                .setBackgroundResource(R.drawable.shape_collect_normal);
                        mProductCollect.setText("收藏");
                        mProductCollect.setTextColor(getResources().getColor(
                                R.color.color_blue));
                        mProductCollectStar
                                .setImageResource(R.drawable.collect_nor);
                        if (mProduct != null) {
                            mProduct.setmIsCollect(false);
                        }
                    } else {
                        SmartToast.makeText(getActivity(),
                                R.string.product_info_error_collect,
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
    public void onFail(String message) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSuccess() {
        showSmartToast("成功加入购物车", Toast.LENGTH_LONG);
    }

    @Override
    public void login() {
        UIHelper.toLoginActivity(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        AppLog.Logd("Shi", "resultCode:::" + resultCode);
        if (requestCode == ProductInfoFragment.FLAG_GET_STANDARD) {
            if (resultCode == -1 || resultCode == 0) {
                if (mProduct != null && data != null) {
                    mStandard = "";
                    mStandard = data
                            .getStringExtra(IntentBundleKey.PRODUCT_STANDARD);
                    if (!TextUtils.isEmpty(mStandard)) {
                        mProduct.setmProductId(data.getIntExtra(
                                IntentBundleKey.PRODUCT_ID, -1));
                        mProductStandard.setText(mStandard.trim());
                    }

                    mProductUpdateCount.setProductCount(data.getIntExtra(
                            IntentBundleKey.PRODUCT_COUNT, 1));

                }
            }
        }
    }

}
