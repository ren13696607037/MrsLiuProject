package com.techfly.liutaitai;

import java.util.ArrayList;
import java.util.List;

public class TabSwitchListener {
    private static TabSwitchListener mTabSwitchListener;
    private List<TabSwitchCallBack> mTabSwitchCbList = new ArrayList<TabSwitchCallBack>();
    private static Object object = new Object();
    public static TabSwitchListener getTabSwitchLisManager(){
        if(mTabSwitchListener==null){
            synchronized (object) {
                if(null==mTabSwitchListener){
                    mTabSwitchListener = new TabSwitchListener();
                }
            }
        }
        return mTabSwitchListener;
    }
    
    public void onRegisterTabSwitchListener(TabSwitchCallBack onTabSwitchCb){
        if(!mTabSwitchCbList.contains(onTabSwitchCb)){
            mTabSwitchCbList.add(onTabSwitchCb);
        }
    }
    
    public void onUnRegisterTabSwitchListener(TabSwitchCallBack onTabSwitchCb){
        if(mTabSwitchCbList.contains(onTabSwitchCb)){
            mTabSwitchCbList.remove(onTabSwitchCb);
        }
    }
    
    public void onTagSwitch(String tag){
        
        for(TabSwitchCallBack callBack : mTabSwitchCbList){
            callBack.switchTab(tag);
        }
    }
}
