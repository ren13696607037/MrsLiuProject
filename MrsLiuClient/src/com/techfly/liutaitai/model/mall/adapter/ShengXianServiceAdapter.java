package com.techfly.liutaitai.model.mall.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.mall.bean.Product;
import com.techfly.liutaitai.model.pcenter.bean.User;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.ImageLoaderUtil;
import com.techfly.liutaitai.util.RequestParamConfig;
import com.techfly.liutaitai.util.SharePreferenceUtils;
import com.techfly.liutaitai.util.SmartToast;
import com.techfly.liutaitai.util.UIHelper;

public class ShengXianServiceAdapter extends BaseAdapter{
    private List<Product> mList;
    private Context mContext;
    private int mUserId;
    private User mUser;
    private Product mProduct;
    public interface AddShopCarCallBack{
        void onSuccess();
    }
    private AddShopCarCallBack mCallBack;
    public ShengXianServiceAdapter(Context context,List<Product> list,AddShopCarCallBack cb){
      mList = list;   
      mContext = context;
      mCallBack =cb;
    }
    
    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):new Product();
    }
    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if(mList.size()==0){
            return convertView;
        }
        if(convertView==null){
            holder=new ViewHolder();
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_shengxian_service, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.service_img);
            holder.personNum = (TextView) convertView.findViewById(R.id.service_person);
            holder.serviceName = (TextView) convertView.findViewById(R.id.service_name);
            holder.servicePrice = (TextView) convertView.findViewById(R.id.service_price);
            holder.shopCarImg =(ImageView) convertView.findViewById(R.id.add_shopcar);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
     
        ImageLoader.getInstance().displayImage(mList.get(position).getmImg(), holder.imageView,ImageLoaderUtil.mGridIconLoaderOptions);
        
        holder.personNum.setText(mList.get(position).getmSale()+"人购买");
        
        holder.serviceName.setText(mList.get(position).getmName());
        
        holder.servicePrice.setText("￥"+mList.get(position).getmPrice());
        
        holder.shopCarImg.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                
                if (authLogin()) {
                    mProduct = mList.get(position);
                    requestData();
                }
                }
        });
        
        return convertView;
        
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
            UIHelper.toLoginActivity(mContext);
        }
        return false;
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
        url.setmGetParamValues("1");

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
                  mCallBack.onSuccess();
                  SmartToast.makeText(mContext, "加入购物车成功", Toast.LENGTH_LONG).show();
                } else {
                  SmartToast.makeText(mContext, "加入购物车失败", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private Response.ErrorListener creatErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load" + error.getMessage());
                SmartToast.makeText(mContext, "加入购物车失败", Toast.LENGTH_LONG).show();
            }
        };
    }
    
    public List<Product> getmList() {
        return mList;
    }

    public void setmList(List<Product> mList) {
        this.mList = mList;
    }
    class ViewHolder{
        private TextView serviceName;
        private ImageView imageView;
        private ImageView shopCarImg;
        private TextView personNum;
        private TextView servicePrice;
       
    }

}
