package com.techfly.liutaitai.model.mall.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
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
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.mall.activities.ProductCommentActivity;
import com.techfly.liutaitai.model.mall.bean.Comments;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.mall.parser.NewProductInfoParser;
import com.techfly.liutaitai.model.mall.parser.ShopCartParser;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.model.shopcar.adapter.ShopCarAdapter;
import com.techfly.liutaitai.model.shopcar.fragment.ShopCarHomeFragment;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.scale.ImageEntity;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.SharePreferenceUtils;
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
    private LinearLayout mDotLinear;
    private RelativeLayout mPagerRelative;
    private ArrayList<View> dots; // 图片标题正文的那些点
    public ArrayList<String> mImageUrls;
    // private ArrayList<HomePager> mPagerDatas;

    private FrameLayout mShopCar;
    private TextView mShopCarNum;
    private TextView mAddShopCar;
    private TextView mShopNow;
    private TextView mProductName;
    private TextView mProductPrice;
    private TextView mDescTv;
    private ProductUpdateView mProductUpdateCount;
    private ListViewForScrollView mListView;
    private TextView mPicAndTextDetail;
    private ArrayList<Comments> mArrayList = new ArrayList<Comments>();
    private CommonAdapter<Comments> mAdapter;
    private String mProductId = "1";
    private int type;
    private String mProImg;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = activity;
        type = activity.getIntent().getIntExtra(IntentBundleKey.TYPE,0);
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mFragment = this;
        Intent intent = getActivity().getIntent();
        mProductId = intent.getStringExtra(IntentBundleKey.ID);
        mProImg= intent.getStringExtra(IntentBundleKey.IMAGE_PATH);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_productinfo, container, false);
    }

    @Override
    public void onDestroy() {
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
//        setRightMoreIcon(R.drawable.share, new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享易汇");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, SharePreferenceUtils
//                        .getInstance(getActivity()).getShareContent()
//                        + SharePreferenceUtils.getInstance(getActivity())
//                                .getShareUrl());
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(shareIntent, "分享易汇"));
//            }
//        });
        setLeftHeadIcon(Constant.HEADER_TITLE_LEFT_ICON_DISPLAY_FLAG);
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        initPager(view);
        mShopCar = (FrameLayout) view.findViewById(R.id.product_info_shop_car);
        mShopCar.setEnabled(false);
        mShopCar.setOnClickListener(getClickListener());
        
        view.findViewById(R.id.phone).setOnClickListener(getClickListener());
        
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
        mDescTv = (TextView) view .findViewById(R.id.product_info_desc);
        

        mProductUpdateCount = (ProductUpdateView) view
                .findViewById(R.id.product_info_product_update_count);

        mShopCarNum = (TextView) view.findViewById(R.id.shop_car_num2);

        mListView = (ListViewForScrollView) view
                .findViewById(R.id.product_info_listview);
        // mListView.setHeaderDividersEnabled(true);
        // mListView.setFooterDividersEnabled(true);
        mAdapter = new CommonAdapter<Comments>(getActivity(), mArrayList,
                R.layout.item_ratelist) {
            @Override
            public void convert(ViewHolder holder, Comments item, int position) {
                holder.setText(R.id.iratelist_name, item.getmName());
                holder.setText(R.id.iratelist_time, item.getmTime());
                holder.setText(R.id.iratelist_content,
                        item.getmContent());
                holder.setRating(R.id.iratelist__bar,Float.parseFloat(item.getmStarScore()));

            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(getItemClickListener());

        mPicAndTextDetail = (TextView) view
                .findViewById(R.id.product_info_text_and_pic_detail);
        mPicAndTextDetail.setOnClickListener(getClickListener());
        view
        .findViewById(R.id.product_info_comment).setOnClickListener(getClickListener());;
       

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
//                UIHelper.toPicAndTextActivity(getActivity(), mProductId);
                UIHelper.showImage(mContext, position, (ArrayList<ImageEntity>) mProduct.getmImageEntity(), false);
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
                    if (authLogin()) {
                        UIHelper.toShopCarActivity(getActivity(),type);
                    } else {
                        UIHelper.toLoginActivity(getActivity());
                    }
                    break;
                case R.id.product_info_shop_car_add:// 添加购物车
//                    if (mProduct.getmStoreCount() <= 0) {
//                        showSmartToast(R.string.store_count_not_enough,
//                                Toast.LENGTH_LONG);
//                        break;
//                    }
                    try {
                        mProductUpdateCount
                                .onReqPullToShopCart(ProductInfoFragment.this);
                    } catch (CallBackNullException e) {
                        
                        e.printStackTrace();
                    } catch (ProductCountException e) {
                    
                        e.printStackTrace();
                    }
                    break;
                case R.id.product_info_shop_now:// 立即购买
//                    if (mProduct.getmStoreCount() <= 0) {
//                        showSmartToast(R.string.store_count_not_enough,
//                                Toast.LENGTH_LONG);
//                        break;
//                    }
                    if (authLogin()) {
                        UIHelper.toTakingOrderActivity(getActivity(), mProduct,type);
                    } else {
                        UIHelper.toLoginActivity(getActivity());
                    }
                    break;
                case R.id.product_info_product_name:
                case R.id.product_info_text_and_pic_detail:
                    UIHelper.toPicAndTextActivity(getActivity(), mProduct.getmDesc(),null);
                    break;
                case R.id.product_info_comment:
                    if(mArrayList.size()>0){
                        UIHelper.toSomeIdActivity(ProductInfoFragment.this,ProductCommentActivity.class.getName(),mProductId,type);
                    }else{
                        showSmartToast("还没有商品的评论", Toast.LENGTH_LONG);
                    }
                 
                    break;
                case R.id.phone:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse("tel:" + getActivity().getString(R.string.common_phone_dial_num)));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                default:
                    break;
                }
            }
        };
        return mListener;
    }
   
    public void requestShopCarNum() {
       
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
        param.setmId(user .getmId());
        param.setmToken(user .getmToken());
        
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL
                + Constant.SHOP_CARD_REQUEST_URL);
        url.setmGetParamPrefix("city");
        url.setmGetParamValues(SharePreferenceUtils.getInstance(getActivity()).getArea().getmId());
        url.setmGetParamPrefix("type");
        url.setmGetParamValues(type+"");
        param.setmHttpURL(url);
        param.setmParserClassName(ShopCartParser.class.getName());
        RequestManager.getRequestData(getActivity(), creatSuccessListener(),
                creatErrorListener(), param);//
    }
    private Response.Listener<Object> creatSuccessListener() {
        return new Response.Listener<Object>() {

            @Override
            public void onResponse(Object obj) {
                mLoadHandler.removeMessages(Constant.NET_SUCCESS);
                mLoadHandler.sendEmptyMessage(Constant.NET_SUCCESS);
                if(ShopCar.getShopCar().getShopproductList()!=null){
                    mShopCarNum.setText(ShopCar.getShopCar().getShopAmountSum()+"");
                }else{
                    mShopCarNum.setText("0");
                }
               
            }
        };
    }

    private Response.ErrorListener creatErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());

            }
        };
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
                if (object instanceof Product) {
                    requestShopCarNum();
                    Product p = (Product) object;
                    if (p != null) {
                        mProduct = p;
                        mProduct .setmImg(mProImg);
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
                        mDescTv.setText(Html.fromHtml(p.getmContent()));
                        if (mArrayList != null && p.getmCommentsList() != null) {
                            mArrayList.clear();
                            mArrayList.addAll(p.getmCommentsList());
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
        requestShopCarNum();
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
                    mProductUpdateCount.setProductCount(data.getIntExtra(
                            IntentBundleKey.PRODUCT_COUNT, 1));

                }
            }
        }
    }

}
