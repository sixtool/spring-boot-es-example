package com.twl.example.resp;

public class RespObject extends RespBase {
	private static final long serialVersionUID = 1L;
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public RespObject() {
		super.setSuccessResult();
	}
	
	@Override
	public String toString() {
		return super.toString() + " RespObject [data=" + data + "]";
	}

}
