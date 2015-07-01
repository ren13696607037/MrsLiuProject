package com.hylsmart.yihui.util.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.util.Constant;
import com.hylsmart.yihui.util.Utility;


public class LoadingDialog extends Dialog {
    private View mView;
    private View mView2;
    private Activity mActivity;
    private long LOADING_TIMEOUT = 5 * 1000; // 10s timeout
    private Handler mTimerHander = null;
    private TimerRunner mTimerRunner = null;
    private RelativeLayout mLoadingFail=null;
    private LinearLayout mLoadingView = null;
    private ImageView mRefreshBtn = null;
    private TextView mTxtNotify = null;
    private boolean bInit = false;
    private boolean bDismiss = false;
    private static ViewGroup mRootView;
    
    public enum NETWORK_ERROR_TYPE {
        NETWORK_ERROR, SERVER_ERROR, NO_DATA
    }
    public interface LoadingDialogListener{
        void onDataRefresh();
    }
    private static LoadingDialogListener mListener;
    
    public void setDialogListener(LoadingDialogListener listener){
        this.mListener=listener;
     }

    public LoadingDialog(Context context, int style) {
        super(context, style);
        mActivity=(Activity) context;
    }

    public LoadingDialog(Context context) {
        super(context);
        mActivity=(Activity) context;
    }
    
    
    /** 
     * 得到自定义的progressDialog 
     * @param context 
     * @param msg 
     * @return 
     */  
    public void createLoadingDialog(Fragment context, String msg,LoadingDialogListener listener,ViewGroup viewGroup) {  
    
        LayoutInflater inflater = LayoutInflater.from(context.getActivity());  
        mView= inflater.inflate(R.layout.view_loading, null);// 得到加载view  
        mLoadingView=(LinearLayout) mView.findViewById(R.id.loading_progress);// 加载布局
        mTimerHander = new Handler();
       
        ImageView spaceshipImage = (ImageView) mView.findViewById(R.id.img);  
        TextView tipTextView = (TextView) mView.findViewById(R.id.tipTextView);// 提示文字  
        // 加载动画  
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
                context.getActivity(), R.anim.loading_anim);  
        // 使用ImageView显示动画  
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
        tipTextView.setText(msg);// 设置加载信息  
  
        LoadingDialog loadingDialog = new LoadingDialog(context.getActivity(),R.style.loading_dialog);// 创建自定义样式dialog  
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setDialogListener(listener);
        loadingDialog.show(viewGroup,mView);
    }  
    
    /** 
     * 得到自定义的progressDialog 
     * @param context 
     * @param msg 
     * @return 
     */  
    public void createLoadingDialog(Fragment context,LoadingDialogListener listener,ViewGroup viewGroup) {  
        LayoutInflater inflater = LayoutInflater.from(context.getActivity());  
        mView2= inflater.inflate(R.layout.view_loading_fail, null);// 得到加载view  
        mLoadingFail=(RelativeLayout) mView2.findViewById(R.id.loading_fail);
        mRefreshBtn=(ImageView) mView2.findViewById(R.id.refresh_text);
        mTxtNotify = (TextView) mView2.findViewById(R.id.txt_loading_note);
        mRefreshBtn.setClickable(true);
        mRefreshBtn.setOnClickListener(new OnClickListener());
        mTimerHander = new Handler();
        
        LoadingDialog loadingDialog = new LoadingDialog(context.getActivity());// 创建自定义样式dialog  
        loadingDialog.setDialogListener(listener);
        loadingDialog.show(viewGroup,mView2);
        reTry();
    }  
    
    public void show(ViewGroup rootView,View view) {
        int a=(int) mActivity.getResources().getDimension(R.dimen.header_bar_height);
        int b=Utility.getStatusBarHeight(mActivity);
        if (!bInit) {
            mRootView = rootView;
            if (mRootView == null) {
                mRootView= (ViewGroup) mActivity.findViewById(android.R.id.content);
            }
            if(android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.GINGERBREAD_MR1){
                mRootView.addView(view, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }else{
                FrameLayout.LayoutParams params=null;
                    params=new FrameLayout.LayoutParams(Constant.SCREEN_WIDTH, (int) (Constant.SCREEN_HEIGHT-a-b));
                    params.topMargin=a;
                    params.leftMargin=0;
                    params.rightMargin=Constant.SCREEN_WIDTH;
                    params.bottomMargin=Constant.SCREEN_HEIGHT-a;
                mRootView.addView(view, params);
            }
            bInit = true;
        }
    }
    
    public class TimerRunner implements Runnable{

        @Override
        public void run() {
            if(mTimerRunner != null) {
                mTimerHander.removeCallbacks(mTimerRunner) ; 
                mTimerRunner = null;
            }
        }
        
    }
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }
    private void reTry(){
        if(!isNetworkAvailable(mActivity)){
            loadFail(NETWORK_ERROR_TYPE.NETWORK_ERROR);
        }else{
            if(mTimerRunner == null) {
                mTimerRunner = new TimerRunner();
            }
            if(mTimerHander ==null){
                mTimerHander=new Handler();
            }
            mTimerHander.postDelayed(mTimerRunner,LOADING_TIMEOUT) ;
        }
    }
    public void loadFail(NETWORK_ERROR_TYPE type) {
        String failMsg = "";
        switch(type) {
        case NETWORK_ERROR:
            failMsg = mActivity.getString(R.string.loading_fail_network);
            break;
        case SERVER_ERROR:
            failMsg = mActivity.getString(R.string.loading_fail_server);
            break;
        case NO_DATA:
            mTxtNotify.setText(R.string.loading_fail_nodata);
            mTimerHander.removeCallbacks(mTimerRunner);
            failMsg = mActivity.getString(R.string.loading_fail_nodata);
            break;
        }
        Toast.makeText(mActivity, failMsg, Toast.LENGTH_SHORT).show();
    }
    private class OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
            case R.id.refresh_text:
                if (mListener != null) {
                    mListener.onDataRefresh();
                }
                break;
            default:
                break;
            }
        }
        
    }
    public void dismiss(boolean b){
       if(!bDismiss||b){
           if(mView!=null){
               mRootView.removeView(mView);
           }
           if(mView2!=null){
               mRootView.removeView(mView2);
           }
           bDismiss=true;
           if(mTimerRunner!=null){
               mTimerHander.removeCallbacks(mTimerRunner) ; 
               mTimerRunner = null;
           }
       }
    }
    

}
