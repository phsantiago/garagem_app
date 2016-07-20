package com.garagem.nupark.dto;

public class RetornoDto {
	private int retCode;
	private String message;
	
	public RetornoDto() {
	}

	public RetornoDto(int retCode,String message){
		this.retCode = retCode;
		this.message = message;
	}
	
	public int getRetCode() {
		return retCode;
	}
	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
