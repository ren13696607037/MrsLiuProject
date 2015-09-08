package com.techfly.liutaitai.util.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.shopcar.CallBackNullException;
import com.techfly.liutaitai.bizz.shopcar.ProductCountException;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.SharePreferenceUtils;

public class ProductUpdateView extends LinearLayout implements OnClickListener{
    Context mContext;
    ImageButton mAddIcon;
    ImageButton mMinusIcon;
    TextView mNumText;
    private Product mProduct;
    private int num;
    private int mUserId;
    private User mUser;
    public interface ShopCarCallBack {
        void onFail(String message);

        void onSuccess();

        void login();
    }

    
    private ShopCarCallBack mCallBack;
    private ProgressDialog mProgressDialog;
    
 

    public ProductUpdateView(Context context) {
        this(context, null);
    }

    public ProductUpdateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductUpdateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOrientation(HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.productUpdateView);
        a.recycle();

        LinearLayout convertView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.shop_product_amount,
                null);
        mAddIcon = (ImageButton) convertView.findViewById(R.id.imgbtn_product_add);
        mMinusIcon = (ImageButton) convertView.findViewById(R.id.imgbtn_product_minus);
        mNumText = (TextView) convertView.findViewById(R.id.txt_product_num);
        mAddIcon.setOnClickListener(this);
        mMinusIcon.setOnClickListener(this);
        this.removeAllViews();
        this.addView(convertView);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onAttachView(Product product) {
        mProduct = product;
        num = mProduct.getmAmount();
        initView();
    }

    private void initView() {
        if (null != mNumText && num > 1) {
            mNumText.setVisibility(View.VISIBLE);
            mMinusIcon.setVisibility(View.VISIBLE);
            mNumText.setText(String.valueOf(num));
            mMinusIcon.setImageResource(R.drawable.shop_car_minus_index_enable);
            mMinusIcon.setClickable(true); 
            if(num>=200){
                mAddIcon.setImageResource(R.drawable.shop_car_add_index_disable);
                mAddIcon.setClickable(false);
            }else{
                mAddIcon.setImageResource(R.drawable.shop_car_add_index_enable);
                mAddIcon.setClickable(true);
            }
        } else {
            mAddIcon.setVisibility(View.VISIBLE);
            mMinusIcon.setVisibility(View.VISIBLE);
            mMinusIcon.setImageResource(R.drawable.shop_car_minus_index_disable);
            mMinusIcon.setClickable(false); 
        }
        mNumText.setText(num + "");
        mNumText.setTextColor(mContext.getResources().getColor(R.color.color_blue));
        mNumText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shop_car_num_bg));
    }
    /**
     * 设置商品的数量
     * @param count
     */
    public void setProductCount(int count) {
        num = count;
        initView();
    }
    
    /**
     * 获取商品的数量
     * @param count
     */
    public int getProductCount() {
        return num;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
        case R.id.imgbtn_product_add:
            int maxCount = 200;
            if (num < maxCount) {
                num = mProduct.getmAmount() + 1;
                mProduct.setmAmount(num);
                initView();
            }else{
                num =200;
            }
            break;

        case R.id.imgbtn_product_minus:
            num = mProduct.getmAmount() - 1;
            mProduct.setmAmount(num);
            initView();
            break;

        default:
            break;
        }

    }
    /**
     * 认证用户是否登录
     * 
     * @return
     */
    private boolean authLogin() {
        User user = SharePreferenceUtils.getInstance(mContext).getUser();
        if (user != null && !TextUtils.isEmpty(user.getmId())) {
            mUser = user;
            mUserId = Integer.parseInt(user.getmId());
            if(mUserId>0){
                return true;
            }
            return true;
        } else {
            if(mCallBack!=null){
                mCallBack.login();
            }
        }
        return false;
    }
    /**
     * 请求加入购物车
     * 
     * @param callBack
     * @throws CallBackNullException
     * @throws ProductCountException
     */
    public void onReqPullToShopCart(ShopCarCallBack callBack) throws CallBackNullException, ProductCountException {
        if (callBack == null) {
            throw new CallBackNullException();
        }
        mCallBack = callBack;
        if (mProduct.getmAmount() > 0) {
            if (authLogin()) {
                requestData();
            }
        } else {
            throw new ProductCountException();
        }

    }

    /**
     * 请求加入购物车
     */
    private void requestData() {
        RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        
       
        param.setmIsLogin(true);
        param.setmId(mUser.getmId());
        param.setmToken(mUser.getmToken());
        url.setmGetParamPrefix("city");
        url.setmGetParamValues(SharePreferenceUtils.getInstance(mContext).getArea().getmId());

        url.setmGetParamPrefix(RequestParamConfig.GOODS_ID);
        url.setmGetParamValues(mProduct.getmId());

        url.setmGetParamPrefix(RequestParamConfig.NUM);
        url.setmGetParamValues(mProduct.getmAmount() + "");

        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ADD_TO_CART_REQUEST_URL);
        param.setmHttpURL(url);
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(mContext, creatSuccessListener(), creatErrorListener(), param);//
    }

    private Response.Listener<Object> creatSuccessListener() {
        return new Response.Listener<Object>() {

            @Override
            public void onResponse(Object obj) {
                ResultInfo info = (ResultInfo) obj;
                if (info.getmCode() == 0) {
                    ShopCar.getShopCar().updateproduct(false, mProduct, true);
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    mCallBack.onSuccess();
                } else {
                    mCallBack.onFail("加入购物车失败");
                }
            }
        };
    }

    private Response.ErrorListener creatErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                mCallBack.onFail("加入购物车失败");
            }
        };
    }

    protected void showDialog() {
        mProgressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.add_toshopcar_title),
                mContext.getString(R.string.add_toshopcar_message), false, false);

    }
}
