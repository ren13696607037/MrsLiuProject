package com.techfly.liutaitai.util.view;



import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.ManagerListener;

public class TechFinishDialog extends Dialog{
	private Context mContext;
	private Button mButton;
	private Button mButton2;
	private ImageView mImageView;
	private String mUrl = null;
	

	public TechFinishDialog(Context context, int theme) {
		super(context, R.style.loading_dialog);
	}
	public TechFinishDialog(Context context,String url){
		super(context);
		this.mContext=context;
		this.mUrl = url; 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window theWindow = getWindow();
		theWindow.setGravity(Gravity.CENTER);
		setContentView(R.layout.dialog_tech_finish);
		WindowManager.LayoutParams lp = theWindow.getAttributes();
		theWindow.setAttributes(lp);
		theWindow.setBackgroundDrawable(new BitmapDrawable());
		theWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		initView();
	}
	private void initView(){
		mButton = (Button) findViewById(R.id.finish_btn);
		mButton2 = (Button) findViewById(R.id.finish_btn1);
		mImageView = (ImageView) findViewById(R.id.finish_img);
		
		if(mUrl != null){
			mButton.setVisibility(View.GONE);
			mImageView.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(mUrl, mImageView);
			mButton2.setText(R.string.submit);
		}else{
			mButton.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.GONE);
		}
		
		mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ManagerListener.newManagerListener().notifyDialogCameraListener();
				dismiss();
			}
		});
		mButton2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mButton2.getText().toString().equals(mContext.getString(R.string.submit))){
					ManagerListener.newManagerListener().notifyDialogSubmitListener(mUrl);
					dismiss();
				}else{
					ManagerListener.newManagerListener().notifyDialogPhotoListener();
					dismiss();
				}
			}
		});
		
	}

}

