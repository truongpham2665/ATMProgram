package com.homedirect.message;

public enum MyException {
	
	SUCCESS(01, "thành công"), 
	LOGIN_FALSE(02, "Đăng nhập thất bại"), 
	DUPLICATE(03, "Đã tồn tại"),
	CHANGEPASS_FALSE(04, "Đổi mật khẩu không thành công!"), 
	INPUT_ISVALID(05, "input không hợp lệ"),
	CREATE_FALSE(06, "thêm mới thất bại");
	
	private final int code;
	private final String description;

	private MyException(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}
}
