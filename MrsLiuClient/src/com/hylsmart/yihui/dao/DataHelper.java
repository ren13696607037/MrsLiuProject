package com.hylsmart.yihui.dao;

import java.util.List;

public class DataHelper implements IDbAdapter {
	private DbAdapter mAdapter;

	public DbAdapter getmAdapter() {
		return mAdapter;
	}

	public void setmAdapter(DbAdapter mAdapter) {
		this.mAdapter = mAdapter;
	}

	@Override
	public List<Persistence> getDataList() {
		return mAdapter.getDataList();
	}

	@Override
	public void addData(Persistence data) {
		mAdapter.addData(data);
	}

	@Override
	public void updateData(Persistence data) {
		mAdapter.updateData(data);
	}

	@Override
	public void deleteData(String unique) {
		mAdapter.deleteData(unique);
	}

    @Override
    public void deleteData() {
        mAdapter.deleteData();
        
    }

    @Override
    public void addDataList(List<Persistence> data) {
       mAdapter.addDataList(data);
        
    }

}
