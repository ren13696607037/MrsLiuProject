package com.techfly.liutaitai.scale;


import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.techfly.liutaitai.R;

public class onImageClickListener implements OnClickListener{
    private Context mContext;
    private ImageView mView;
    private int mPosition;
    private int mSize;
    private GalleryAdapter mAdapter;
    public onImageClickListener(Context context,ImageView imageView,int index,int size,GalleryAdapter adapter){
        this.mContext=context;
        this.mView=imageView;
        this.mPosition=index;
        this.mSize=size;
        this.mAdapter=adapter;
    }

    @Override
    public void onClick(View arg0) {
       switch (mView.getId()) {
       case R.id.left_gallery:
           if(mPosition!=0){
               mPosition--;
               mAdapter.updateText(mPosition);
           }
           break;
       case R.id.right_gallery:
           if(mPosition!=mSize-1){
               mPosition++;
               mAdapter.updateText(mPosition);
           }
           break;

    default:
        break;
    }
    }
    
}
