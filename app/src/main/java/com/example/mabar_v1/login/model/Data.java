package com.example.mabar_v1.login.model;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("access_token")
	private String accessToken;

	@SerializedName("token_type")
	private String tokenType;

	@SerializedName("expires_in")
	private int expiresIn;

	@SerializedName("user")
	private User user;

	public void setAccessToken(String accessToken){
		this.accessToken = accessToken;
	}

	public String getAccessToken(){
		return accessToken;
	}

	public void setTokenType(String tokenType){
		this.tokenType = tokenType;
	}

	public String getTokenType(){
		return tokenType;
	}

	public void setExpiresIn(int expiresIn){
		this.expiresIn = expiresIn;
	}

	public int getExpiresIn(){
		return expiresIn;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}
}