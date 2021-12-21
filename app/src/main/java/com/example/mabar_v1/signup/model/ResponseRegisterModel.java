package com.example.mabar_v1.signup.model;

import com.google.gson.annotations.SerializedName;

public class ResponseRegisterModel{

	@SerializedName("code")
	private String code;

	@SerializedName("desc")
	private String desc;

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}
}