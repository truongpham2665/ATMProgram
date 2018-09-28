package com.homedirect.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.homedirect.constant.ErrorCode;
import com.homedirect.exception.ATMException;
import com.homedirect.response.ATMResponse;

// thêm log cho Exception
// sửa lại hàm ATMResponse
public abstract class AbstractController<P> {

	final static Logger logger = Logger.getLogger(AccountController.class);
	protected @Autowired P processor;

	protected <O> ATMResponse<O> toResponse(O data) {
		ATMResponse<O> response = new ATMResponse<O>();
		if (data instanceof ATMException) {
			ATMException e = (ATMException) data;
			response.setCode(e.getCode());
			response.setMessage(e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
				logger.error(e.getMessage());
			}
			return response;
		}

		if (data instanceof Exception) {
			Exception e = (Exception) data;
			response.setCode(ErrorCode.UNKNOWN);
			response.setMessage(e.getMessage());
			if (logger.isDebugEnabled()) {
				logger.debug(e);
				logger.error(e.getMessage());
			}
			return response;
		}

		response.setCode(ErrorCode.SUCCESS);
		response.setMessage(ErrorCode.SUCCESS_MES);
		response.setData(data);
		return response;
	}
}
