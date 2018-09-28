package com.homedirect.constant;

public class ErrorCode {
	public static final int SUCCESS = 1;
	public static final String SUCCESS_MES = "Thành công";

	public static final int INVALID_INPUT_USERNAME = 2;
	public static final String INVALID_INPUT_USERNAME_MES = "Username không hợp lệ";
	
	public static final int INVALID_INPUT_PASSWORD = 3;
	public static final String INVALID_INPUT_PASWORD_MES = "Password không hợp lệ";

	public static final int NOT_FOUND = 4;
	public static final String NOT_FOUND_MES = "Không tìm thấy dữ liệu có id";
	
	public static final int NOT_FOUND_USERNAME = 5;
	public static final String NOT_FOUND_USERNAME_MES = "Không tìm thấy dữ liệu có username";
	
	public static final int USERNAME_EXIST = 6;
	public static final String USERNAME_EXIST_MES = "Username đã tồn tại";

	public static final int EXECUTE_FAIL = 7;
	public static final String EXECUTE_FAIL_MES = "Lỗi trong quá trình xử lý";

	public static final int INVALID_USERNAME = 8;
	public static final String INVALID_USERNAME_MES = "Username không chính xác";

	public static final int MISS_DATA = 9;
	public static final String MISS_DATA_MES = "Yêu cầu nhập đủ thông tin";

	public static final int INVALID_PASSWORD = 10;
	public static final String INVALID_PASWORD_MES = "Password không chính xác";

	public static final int INVALID_AMOUNT_DEPOSIT = 11;
	public static final String INVALID_DEPOSIT_MES = "Số tiền phải lớn hơn 0 \n Là bội số của 10000";

	public static final int INVALID_AMOUNT_WITHDRAW = 12;
	public static final String INVALID_WITHDRAW_MES = "Số tiền phải lớn hơn 0 \n Nhỏ hơn 10000000 \n Là bội số của 10000";
	
	public static final int DUPLICATE_INPUT = 13;
	public static final String DUPLICATE_INPUT_MES = "Không thể chuyển cho chính mình";
	
	public static final int UNKNOWN = 99;
	public static final String UNKNOWN_MES = "Lỗi không xác định";

}
