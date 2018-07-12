package com.twl.example.resp;

import java.io.Serializable;


public class RespBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 返回结果 **/
	private String result;

	/** 结果代码 **/
	private String code;

	/** 描述 **/
	private String msg;


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public RespBase setSuccessResult() {
		setResult(EnumSystem.SYSTEM_SUCCESS.getResult());
		setCode(EnumSystem.SYSTEM_SUCCESS.getCode());
		setMsg(EnumSystem.SYSTEM_SUCCESS.getMsg());
		return this;
	}

	public RespBase setFailResult() {
		setResult(EnumSystem.SYSTEM_FAILED.getResult());
		setCode(EnumSystem.SYSTEM_FAILED.getCode());
		setMsg(EnumSystem.SYSTEM_FAILED.getMsg());
		return this;
	}
	
	public RespBase setFailResult(String msg) {
		setResult(EnumSystem.SYSTEM_FAILED.getResult());
		setCode(EnumSystem.SYSTEM_FAILED.getCode());
		setMsg(msg);
		return this;
	}

	public RespBase setSuccessResult(String msg) {
		setResult(EnumSystem.SYSTEM_SUCCESS.getResult());
		setCode(EnumSystem.SYSTEM_SUCCESS.getCode());
		setMsg(msg);
		return this;
	}

	public RespBase setSessionOut() {
		setResult(EnumSystem.SESSION_OUT.getResult());
		setCode(EnumSystem.SESSION_OUT.getCode());
		setMsg(EnumSystem.SESSION_OUT.getMsg());
		return this;
	}
	public RespBase setSystemException() {
		setResult(EnumSystem.SYSTEM_EXCEPTION.getResult());
		setCode(EnumSystem.SYSTEM_EXCEPTION.getCode());
		setMsg(EnumSystem.SYSTEM_EXCEPTION.getMsg());
		return this;
	}

	public RespBase setSystemException(String msg) {
		setResult(EnumSystem.SYSTEM_EXCEPTION.getResult());
		setCode(EnumSystem.SYSTEM_EXCEPTION.getCode());
		setMsg(msg);
		return this;
	}
	
	public RespBase setIllegalResult() {
		setResult(EnumSystem.ILLEGAL.getResult());
		setCode(EnumSystem.ILLEGAL.getCode());
		setMsg(EnumSystem.ILLEGAL.getMsg());
		return this;
	}
	public RespBase setIllegalResult(String msg) {
		setResult(EnumSystem.ILLEGAL.getResult());
		setCode(EnumSystem.ILLEGAL.getCode());
		setMsg(msg);
		return this;
	}

	@Override
	public String toString() {
		return "RespBase [result=" + result + ", code=" + code + ", msg=" + msg + "]";
	}
	
}
