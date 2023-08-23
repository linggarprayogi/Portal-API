package com.glovis.portal.pojo;

import lombok.Data;

@Data
public class ResponseModel {

	private boolean status;
	private String  message;
	private Object  data;

	// constructor kosong
	public ResponseModel() {
	}

	// jika ok dan menampilkan
	public ResponseModel(boolean status, String message, Object data) {
		this.status  = status;
		this.message = message;
		this.data    = data;
	}

	// jika error tidak menampilkan data
	public ResponseModel(boolean status, String message) {
		this.status  = status;
		this.message = message;
	}
}
