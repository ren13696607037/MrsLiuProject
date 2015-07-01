package com.techfly.liutaitai.dialog;

import com.techfly.liutaitai.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

/**
 * 
 * @author yuancheng
 * 
 * @version 2013-7-9 上午8:42:39
 */

public class CustormProgressDialog extends ProgressDialog {

    public interface IDownloadDialogListener {
        void onKeyBack();
    }

    private IDownloadDialogListener mListener;

    public CustormProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustormProgressDialog(Context context) {
        super(context);
    }

    public void setDownloadDialogListener(IDownloadDialogListener l) {
        this.mListener = l;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mListener != null) {
                mListener.onKeyBack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
