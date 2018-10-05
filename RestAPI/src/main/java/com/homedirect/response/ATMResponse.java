package com.homedirect.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ATMResponse<T> {

	private int code;
	private String message;
	private T data;
}
