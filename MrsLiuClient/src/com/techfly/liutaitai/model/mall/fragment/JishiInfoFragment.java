package com.techfly.liutaitai.model.mall.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techfly.liutaitai.R;
import com.techfly.liutaitai.util.fragment.CommonFragment;

public class JishiInfoFragment extends CommonFragment{

    @Override
    public void requestData() {
       
        
    }

    @Override
    public void onAttach(Activity activity) {
        
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_listview,
                container, false);
        return view;
    }

    @Override
    public void onDestroy() {
       
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
    }

}
