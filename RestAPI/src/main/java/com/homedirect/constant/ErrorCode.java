package com.homedirect.constant;

public class ErrorCode {
	public static final int SUCCESS = 1;
	public static final String SUCCESS_MES = "Success";

	public static final int INVALID_INPUT_USERNAME = 2;
	public static final String INVALID_INPUT_USERNAME_MES = "\nUsername must be greater 3 "
															+ "\n less 15 characters "
															+ "\n Haven't special character"
															+ "\n No whitespace allowed";
	
	
	public static final int INVALID_INPUT_PASSWORD = 3;
	public static final String INVALID_INPUT_PASWORD_MES = "\nUsername must at least 8 characters "
															+ "\n Must have at least one special character "
															+ "\n Must have at least a lower case letter"
															+ "\n Must have at least an upper case letter"
															+ "\n No whitespace allowed";

	public static final int NOT_FOUND = 4;
	public static final String NOT_FOUND_MES = "Don't find data have id";
	
	public static final int NOT_FOUND_USERNAME = 5;
	public static final String NOT_FOUND_USERNAME_MES = "Don't find data have username";
	
	public static final int USERNAME_EXIST = 6;
	public static final String USERNAME_EXIST_MES = "Username already exists";

	public static final int INVALID_AMOUNT = 7;
	public static final String INVALID_AMOUNT_MES = "Balance don't enough";

	public static final int INVALID_USERNAME = 8;
	public static final String INVALID_USERNAME_MES = "Wrong Username";
	
	public static final int DUPLICATE_PASSWORD = 9;
	public static final String DUPLICATE_PASSWORD_MES = "NewPassword can't be like current password";
	
	public static final int INVALID_PASSWORD = 10;
	public static final String INVALID_PASWORD_MES = "Wrong Password";

	public static final int MISS_DATA = 11;
	public static final String MISS_DATA_MES = "You must fill all information";

	public static final int INVALID_AMOUNT_DEPOSIT = 12;
	public static final String INVALID_DEPOSIT_MES = "\nAmount must be greater 0 \n Be multiple by 10000";

	public static final int INVALID_AMOUNT_WITHDRAW = 13;
	public static final String INVALID_WITHDRAW_MES = "\nAmount must be greater 0 \n Less 10000000 \n Be multiple by 10000";
	
	public static final int DUPLICATE_INPUT = 14;
	public static final String DUPLICATE_INPUT_MES = "You can't transfer youself";
	
	public static final int UNKNOWN = 99;
	public static final String UNKNOWN_MES = "Error";

}
