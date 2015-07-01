package com.techfly.liutaitai.bizz.shopcar;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;


public class OnShopCarLisManager {
    private static OnShopCarLisManager mShopCarManagerListener;
    private List<OnShopCarListener> mShopCarListenerList = new ArrayList<OnShopCarListener>();
    private static Object object = new Object();
    public static OnShopCarLisManager getShopCarLisManager(){
        if(mShopCarManagerListener==null){
            synchronized (object) {
                if(null==mShopCarManagerListener){
                    mShopCarManagerListener = new OnShopCarLisManager();
                }
            }
        }
        return mShopCarManagerListener;
    }
    
    public void onRegisterShopCarListener(OnShopCarListener onOrderproductListener){
        if(!mShopCarListenerList.contains(onOrderproductListener)){
            mShopCarListenerList.add(onOrderproductListener);
        }
    }
    
    public void onUnRegisterShopCarListener(OnShopCarListener onOrderproductListener){
        if(mShopCarListenerList.contains(onOrderproductListener)){
            mShopCarListenerList.remove(onOrderproductListener);
        }
    }
    
    public void onNotifyShopCarChange(Bundle bundle){
        
        for(OnShopCarListener listener : mShopCarListenerList){
            listener.notify(bundle);   
        }
    }
}
