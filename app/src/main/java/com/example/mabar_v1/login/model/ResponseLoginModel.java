package com.example.mabar_v1.login.model;

import com.google.gson.annotations.SerializedName;

public class ResponseLoginModel{

	@SerializedName("code")
	private String code;

	@SerializedName("data")
	private Data data;

	@SerializedName("desc")
	private String desc;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}
}