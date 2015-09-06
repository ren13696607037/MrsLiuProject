package com.techfly.liutaitai.util.view;

import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;

public class ShopCartChangeView extends LinearLayout implements OnClickListener{
	    Context mContext;
	    ImageButton mAddIcon;
	    ImageButton mMinusIcon;
	    TextView mNumText;
	    private ProgressBar mProgressBar;
	    private Product mProduct;
	    private boolean mIsWithDialog;
	    private Dialog mDialog;
	    private String mUserId;
	    private ProgressDialog mProgressDialog;
	    private boolean mIsAddOperation;

	    public ShopCartChangeView(Context context) {
	        this(context, null);
	    }

	    public ShopCartChangeView(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }

	    public ShopCartChangeView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        mContext = context;
	        setOrientation(HORIZONTAL);
	        TypedArray a = context.obtainStyledAttributes(attrs,
	                R.styleable.productUpdateView);
	        mIsWithDialog= a.getBoolean(0,true);
	        a.recycle();
	        
	        LinearLayout convertView =
	                      (LinearLayout) LayoutInflater.from(context).inflate(R.layout.shop_product_amount, null);
	        mAddIcon = (ImageButton) convertView.findViewById(R.id.imgbtn_product_add);
	        mMinusIcon = (ImageButton) convertView.findViewById(R.id.imgbtn_product_minus);
	        mNumText = (TextView) convertView.findViewById(R.id.txt_product_num);
	        mProgressBar = (ProgressBar) convertView.findViewById(R.id.progressbar);
	        mAddIcon.setOnClickListener(this);
	        mMinusIcon.setOnClickListener(this);
	        mUserId = SharePreferenceUtils.getInstance(mContext).getUser().getmId()+"";
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
	   
	    
	public void onAttachView(Product product){
	    List<Product> list =ShopCar.getShopCar().getShopproductList();
        if(list!=null&&list.contains(product)){
                mProduct =list.get(list.indexOf(product));
        }else{
            mProduct = product;
        }
        int num = mProduct.getmAmount();
        mNumText.setTextColor(mContext.getResources().getColor(R.color.color_blue));
        if(mProduct.ismEditable()){
            if (null != mNumText && num > 1) {
                mAddIcon.setVisibility(View.VISIBLE);
                mMinusIcon.setVisibility(View.VISIBLE);
                mNumText.setVisibility(View.VISIBLE);
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
           mNumText.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shop_car_num_bg));
        }else{
            mMinusIcon.setVisibility(View.GONE);
            mAddIcon.setVisibility(View.GONE);
            mNumText.setText("x"+num + "");
            mNumText.setTextColor(mContext.getResources().getColor(R.color.TextColorBLACK_NORMAL));
            mNumText.setBackgroundDrawable(null);
            
        }
    
	    }
    
	    @Override
	    public void onClick(View view) {
	        int id = view.getId();
	        switch (id) {
	        case R.id.imgbtn_product_add:
	        	 mIsAddOperation =true;
	        	 if(mProduct.getmAmount()<mProduct.getmStoreCount()){
	        		 reqProCountChange();
	        	 }else{
	        		 SmartToast.showText(mContext, "库存不足");
	        	 }
	            break;
	        case R.id.imgbtn_product_minus:
	        	  mIsAddOperation =false;
	          		 if(mProduct.getmAmount()<=1){
	                     if(mIsWithDialog){
	                         // dialog show
	                        mDialog = AlertDialogUtils.displayMyAlertChoice(mContext, R.string.giveup, R.string.product_shop_del_confirm_message, R.string.confirm, mOnClickListener
	                             , R.string.giveup, mOnClickListener2);
	                        mDialog.show();
	                     }else{
	                    	 reqPushProFromCart();
	                     }
	                 }else{
	                	    reqProCountChange();
	                 }
	            break;
	        
	        default:
	            break;
	        }
	        
	    }
	    private OnClickListener mOnClickListener = new OnClickListener() {
	        
	        @Override
	        public void onClick(View arg0) {
	            reqPushProFromCart();
	            mDialog.dismiss();
	        }
	    };
	    
	  
	 private OnClickListener mOnClickListener2 = new OnClickListener() {
	        
	        @Override
	        public void onClick(View arg0) {
	            // TODO Auto-generated method stub
	            mDialog.dismiss();
	        }
	    };
	 /**
	  * 删除购物车中某个商品
	  */
	 private void reqPushProFromCart(){
		    showDialog();
		    RequestParam param = new RequestParam();
			HttpURL url = new HttpURL();
			url.setmGetParamPrefix(RequestParamConfig.MEMBER_ID);
			url.setmGetParamValues(mUserId+"");
			
			url.setmGetParamPrefix(RequestParamConfig.CARDS_ID);
			url.setmGetParamValues(mProduct.getmId());
			
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.DELETE_TO_CART_REQUEST_URL);
			param.setmHttpURL(url);
			param.setmParserClassName(CommonParser.class.getName());
			RequestManager.getRequestData(mContext, creatSuccessListener(), creatErrorListener(), param);// 
	 }
	   /**
      * 请求购物车某个商品数量发生改变
      */
		private void reqProCountChange() {
			showUpdateDialog();
			RequestParam param = new RequestParam();
			HttpURL url = new HttpURL();
			url.setmGetParamPrefix(RequestParamConfig.MEMBER_ID);
			url.setmGetParamValues(mUserId+"");
			
			url.setmGetParamPrefix(RequestParamConfig.GOODS_ID);
			url.setmGetParamValues(mProduct.getmId());
			
			url.setmGetParamPrefix(RequestParamConfig.FLAG);
			if(mIsAddOperation){
				url.setmGetParamValues(RequestParamConfig.PLUS);
			}else{
				url.setmGetParamValues(RequestParamConfig.REDUCE);
			}
			url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.UPDATE_TO_CART_REQUEST_URL);
			param.setmHttpURL(url);
			param.setmParserClassName(CommonParser.class.getName());
			RequestManager.getRequestData(mContext, creatSuccessListener(), creatErrorListener(), param);// 
		}
		
		private Response.Listener<Object> creatSuccessListener(){
			return new Response.Listener<Object>() {

				@Override
				public void onResponse(Object obj) {
					ResultInfo info = (ResultInfo) obj;
					if(mProgressDialog!=null){
						mProgressDialog.dismiss();
					}
					if(info.getmCode() ==0){
						if(mIsAddOperation){
							removeUpdateBar();
							ShopCar.getShopCar().updateproduct(true,mProduct, true);
						}else{
							if(mProduct.getmAmount()>=1){
								removeUpdateBar();
							}
							ShopCar.getShopCar().updateproduct(true,mProduct, false);   
						}
						
					}else{
						removeUpdateBar();
						SmartToast.makeText(mContext, "更新失败", Toast.LENGTH_LONG).show();;
					}
				}
			};
		}
		
		private Response.ErrorListener creatErrorListener(){
			return new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					AppLog.Loge(" data failed to load" + error.getMessage());
					if(mProgressDialog!=null){
						mProgressDialog.dismiss();
					}
					removeUpdateBar();
					SmartToast.makeText(mContext, "更新失败", Toast.LENGTH_LONG).show();;
				}
			};
		}
	
		
		protected void   showDialog(){
	        mProgressDialog =ProgressDialog.show(mContext, 
	        		mContext.getString(R.string.delete_toshopcar_title), mContext.getString(R.string.delete_toshopcar_message), false, false);
	      
	 }
		protected void   showUpdateDialog(){
            mProgressBar.setVisibility(View.VISIBLE);
	        mAddIcon.setEnabled(false);
	        mMinusIcon.setEnabled(false);
	        mAddIcon.setClickable(false);
	        mMinusIcon.setClickable(false);
	      
	 }
		private void removeUpdateBar(){
			   mProgressBar.setVisibility(View.GONE);
		       mAddIcon.setEnabled(true);
		       mMinusIcon.setEnabled(true);
		       mAddIcon.setClickable(true);
		       mMinusIcon.setClickable(true);
		}
    
}
