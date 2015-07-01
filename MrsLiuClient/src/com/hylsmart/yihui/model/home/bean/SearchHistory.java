package com.hylsmart.yihui.model.home.bean;

import com.hylsmart.yihui.dao.Persistence;

public class SearchHistory extends Persistence {

	private int id;
	private int type;
	private String name;
    private String proId;

	public SearchHistory(int id, int type, String name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}

	public SearchHistory() {
	}
	public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SearchHistory) {
			SearchHistory history = (SearchHistory) o;
			if (history.name.equals(this.name) && history.type == this.type) {
				return true;
			}
		}
		return false;
	}
	
}
