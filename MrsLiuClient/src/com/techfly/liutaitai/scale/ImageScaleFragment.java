package com.techfly.liutaitai.scale;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.scale.resource.BaseImageResource;

public class ImageScaleFragment extends Fragment {

    private BaseImageResource mResource;
    private OnPhotoTapListener mTapListener;
    private int mPosition;
    private LinearLayout mLayout;
    private ProgressBar mProgressBar;
    private int mProgress;
    public Handler imageHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                if(!Thread.currentThread().isInterrupted()){
                    mProgressBar.setProgress(mProgress);
                }
            }else if(msg.what==2){
                mProgressBar.setVisibility(View.GONE);
                Thread.currentThread().interrupt();
            }
        }
        
    };
    
    
    
    private static String TAG = ImageScaleFragment.class.getSimpleName();

    public static ImageScaleFragment newInstance(BaseImageResource resource,
            OnPhotoTapListener listener, int position) {
        ImageScaleFragment fragment = new ImageScaleFragment();
        Bundle args = new Bundle();
        args.putSerializable("resource", resource);
        args.putInt("position", position);
        fragment.setOnPhotoTapListener(listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null) {
            mResource = (BaseImageResource) args.getSerializable("resource");
            mPosition = args.getInt("position");
        }
    }

    @Override
    public void onDestroyView() {
        View layout = getView();
        if (layout != null && layout instanceof FrameLayout) {
            PhotoView photoView = (PhotoView) layout
                    .findViewById(R.id.photoView);
            if (photoView == null) {
                mResource.releaseImage(photoView);
            }
        }
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(
                R.layout.photo_scale, container, false);
        mLayout=(LinearLayout) layout.findViewById(R.id.loading_progress);
        mProgressBar=(ProgressBar) layout.findViewById(R.id.img_progress);
        PhotoView photoView = (PhotoView) layout.findViewById(R.id.photoView);
        photoView.setOnPhotoTapListener(mTapListener);
        photoView.setAllowScaleToImageSize(true);
        mResource.displayImage(getActivity(), photoView, mPosition,mProgressBar,imageHandler,mLayout);
        Log.e(TAG, "addView>>" + photoView.hashCode());
        
        return layout;
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mTapListener = listener;
    }

    public void restore() {
        View layout = getView();
        if (layout != null && layout instanceof FrameLayout) {
            PhotoView photoView = (PhotoView) layout
                    .findViewById(R.id.photoView);
            photoView.setZoomable(false);
            photoView.setZoomable(true);
        }
    }
    
}
