package com.hylsmart.yihui.util.activities;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import com.hylsmart.yihui.R;
import com.hylsmart.yihui.bizz.shopcar.ShopCar;
import com.hylsmart.yihui.dao.DbAdapter;
import com.hylsmart.yihui.dao.Persistence;
import com.hylsmart.yihui.dao.ShopCarDbAdapter;
import com.hylsmart.yihui.model.mall.bean.Product;
import com.hylsmart.yihui.util.AppManager;

public class BaseHomeActivity extends BaseActivity{
    @Override
    public void onBackPressed() {
        boolean isReturn = showConfirmToast();
        if (isReturn)
            return;
        DbAdapter adapter = new ShopCarDbAdapter(this);
        List<Persistence> list = new ArrayList<Persistence>();
        if(ShopCar.getShopCar().getShopproductList()!=null){
            for(Product product :ShopCar.getShopCar().getShopproductList()){
                list.add(product);
            }
        }
        ShopCar.getShopCar().emptyproductes(this);
        adapter.addDataList(list);
        AppManager.getAppManager().finishAllActivity();
        super.onBackPressed();
    }

    private long mExitTime;

    public boolean showConfirmToast() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - mExitTime > 2000) {
            Toast.makeText(this, R.string.double_back_exit_ap, Toast.LENGTH_SHORT).show();
            mExitTime = secondTime;
            return true;
        }
        return false;
    }
}
