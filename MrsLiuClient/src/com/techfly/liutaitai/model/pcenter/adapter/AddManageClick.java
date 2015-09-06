package com.techfly.liutaitai.model.pcenter.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.bean.ResultInfo;
import com.techfly.liutaitai.bizz.parser.CommonParser;
import com.techfly.liutaitai.model.pcenter.activities.ChangeAddressActivity;
import com.techfly.liutaitai.model.pcenter.bean.AddressManage;
import com.techfly.liutaitai.model.pcenter.fragment.AddressManageFragment;
import com.techfly.liutaitai.net.HttpURL;
import com.techfly.liutaitai.net.RequestManager;
import com.techfly.liutaitai.net.RequestParam;
import com.techfly.liutaitai.util.AlertDialogUtils;
import com.techfly.liutaitai.util.AppLog;
import com.techfly.liutaitai.util.Constant;
import com.techfly.liutaitai.util.IntentBundleKey;
import com.techfly.liutaitai.util.JsonKey;
import com.techfly.liutaitai.util.SmartToast;

public class AddManageClick implements OnClickListener{
	private Context mContext;
	private AddressManageFragment mFragment;
	private String mId;
	private String mString;
	private AddManageAdapter mAdapter;
	private AddressManage mAddManage;
	public AddManageClick(Context context,String id,String string,AddManageAdapter adapter,AddressManageFragment fragment,AddressManage addManage){
		this.mContext=context;
		this.mId=id;
		this.mString=string;
		this.mAdapter=adapter;
		this.mFragment=fragment;
		this.mAddManage=addManage;
	}
	
	private Response.Listener<Object> createMyReqSuccessListener() {
        return new Listener<Object>() {
            @Override
            public void onResponse(Object object) {
                AppLog.Logd(object.toString());
                ResultInfo info=(ResultInfo) object;
                if(info.getmCode()==0){
                	SmartToast.makeText(mContext, R.string.delete_success, Toast.LENGTH_SHORT).show();
                	mAdapter.notifyDataSetChanged();
                	if(mAdapter.getListener()!=null){
                		mAdapter.getListener().onDelete();
                	}
                }else{
                	SmartToast.makeText(mContext, info.getmMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
       return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Loge(" data failed to load"+error.getMessage());
            }
       };
    }

	@Override
	public void onClick(View arg0) {
		if("".equals(mString)){
			Intent intent=new Intent(mContext,ChangeAddressActivity.class);
			intent.putExtra(IntentBundleKey.ADDRESS_EXTRA,Constant.ADDRESS_INTENT);
	        intent.putExtra(IntentBundleKey.CHANGEADD_ID, mAddManage);
	        mFragment.startActivityForResult(intent,Constant.ADDRESS_INTENT);
		}else{
				final Dialog dialog = new Dialog(mContext, R.style.MyDialog);
				AlertDialogUtils.displayMyAlertChoice(mContext,dialog, R.string.delete_dialog, R.string.delete_dialog, R.string.confirm, new View.OnClickListener() {
	                
	                @Override
	                public void onClick(View arg0) {
	                	delete();
	                	dialog.dismiss();
	                }
	            }, R.string.giveup, null).show();
		}
	}
	private void delete(){
		RequestParam param = new RequestParam();
        HttpURL url = new HttpURL();
        url.setmBaseUrl(Constant.YIHUIMALL_BASE_URL + Constant.ADDRESS_DELETE_URL);
        url.setmGetParamPrefix(JsonKey.AddressKey.ID).setmGetParamValues(mId);
        param.setmHttpURL(url);
        param.setPostRequestMethod();
        param.setmParserClassName(CommonParser.class.getName());
        RequestManager.getRequestData(mContext, createMyReqSuccessListener(), createMyReqErrorListener(), param);
	}

}
