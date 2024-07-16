package com.ztt.constant;

import java.io.Serializable;

public enum CloudAction implements Serializable {


	API_ACTION(2, "API", "ACTION"),


	FILES_UPLOAD(1, "UPLOAD", "ACTION");
	private final int code;

	private final String action;

	private final String key;

	CloudAction(int code, String action, String key) {
		this.code = code;
		this.action = action;
		this.key = key;
	}

	public int getCode() {
		return code;
	}

	public String getAction() {
		return action;
	}

	public String getKey() {
		return key;
	}
}
