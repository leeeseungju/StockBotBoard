package com.example.stockboard.domain;

public class SearchLog {
    private int idx;
    private String userId;
    private String message;
    private String searchedAt;
    
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSearchedAt() {
		return searchedAt;
	}
	public void setSearchedAt(String searchedAt) {
		this.searchedAt = searchedAt;
	}
    
    
}