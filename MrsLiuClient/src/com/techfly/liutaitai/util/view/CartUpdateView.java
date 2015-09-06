package com.techfly.liutaitai.util.view;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bizz.shopcar.ShopCar;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.util.AlertDialogUtils;

public class CartUpdateView extends LinearLayout implements OnClickListener{
    Context mContext;
    ImageButton mAddIcon;
    ImageButton mMinusIcon;
    TextView mNumText;
    private Product mproduct;
    private boolean mIsWithDialog;
    private Dialog mDialog;

    public CartUpdateView(Context context) {
        this(context, null);
    }

    public CartUpdateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartUpdateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setOrientation(HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.productUpdateView);
        mIsWithDialog= a.getBoolean(0, false);
        a.recycle();
        LinearLayout convertView =
                      (LinearLayout) LayoutInflater.from(context).inflate(R.layout.shop_product_amount, null);
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
   
    

    public void onAttachView(Product product){
       List<Product> list =ShopCar.getShopCar().getShopproductList();
        if(list!=null&&list.contains(product)){
                mproduct =list.get(list.indexOf(product));
        }else{
                mproduct = product;
        }
        int num = mproduct.getmAmount();
        mNumText.setTextColor(mContext.getResources().getColor(R.color.color_blue));
        if(mproduct.ismEditable()){
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
        	ShopCar.getShopCar().updateproduct(true,mproduct, true);
            break;
        
        case R.id.imgbtn_product_minus:
          		 if(mproduct.getmAmount()<=1){
                     if(mIsWithDialog){
                         // dialog show
                        mDialog = AlertDialogUtils.displayMyAlertChoice(mContext, R.string.giveup, R.string.product_shop_del_confirm_message, R.string.confirm, mOnClickListener
                             , R.string.giveup, mOnClickListener2);
                        mDialog.show();
                     }else{
                    	ShopCar.getShopCar().updateproduct(true,mproduct, false);   
                     }
                 }else{
                		ShopCar.getShopCar().updateproduct(true,mproduct, false);   
                 }
          	
           
            break;
        
        default:
            break;
        }
        
    }
    private OnClickListener mOnClickListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            ShopCar.getShopCar().updateproduct(true,mproduct, false);  
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
    
    
}
