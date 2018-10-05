package com.homedirect.request;

import lombok.Getter;

@Getter
public class ChangePassRequest {

	private int id;
	private String oldPassword;
	private String newPassword;
}
