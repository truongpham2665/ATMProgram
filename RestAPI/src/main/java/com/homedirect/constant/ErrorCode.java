package com.homedirect.constant;

public class ErrorCode {
	public static final int SUCCESS = 1;
	public static final String SUCCESS_MES = "Thành công";

	public static final int INVALID_INPUT = 2;
	public static final String INVALID_INPUT_MES = "Dữ liệu nhập không hợp lệ";

	public static final int INVALID_DATA = 3;
	public static final String INVALID_DATA_MES = "Dữ liệu không chính xác";

	public static final int NOT_FOUND = 4;
	public static final String NOT_FOUND_MES = "Không tìm thấy dữ liệu";

	public static final int EXECUTE_FAIL = 5;
	public static final String EXECUTE_FAIL_MES = "Lỗi trong quá trình xử lý";

	public static final int MISS_DATA = 7;
	public static final String MISS_DATA_MES = "Không được để trống";

	public static final int UNKNOWN = 99;
	public static final String UNKNOWN_MES = "Lỗi không xác định";

}
