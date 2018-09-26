package com.homedirect.message;

public class MessageException {
	
	public static String success() {
		return "Success";
	}
	
	public static String usernameIsValid() {
		return "username không hợp lệ";
	}
	
	public static String passwordIsValid() {
		return "password không hợp lệ";
	}
	
	public static String missField() {
		return "yêu cầu nhập đầy đủ thông tin ";
	}
	
	public static String accountIsExist() {
		return "Tài khoản đã tồn tại";
	}
	
	public static String loginFalse() {
		return "Đăng nhập thất bại";
	}
	
	public static String depositFalse() {
		return "Nạp tiền thất bại \n So tien phai lon hon 0 va la boi so cua 10,000";
	}
	
	public static String withdrawFalse() {
		return "Rút tiền thất bại \n So tien phai lon hon 0 va la boi so cua 10,000";
	}
	
	public static String transferFalse() {
		return "Giao dịch thất bại";
	}
	
	public static String haveNotTransaction() {
		return "Không có giao dịch nào";
	}
	
	public static String changePassFalse() {
		return "Đổi password thất bại";
	}
	
	public static String notFound() {
		return "Không tìm thấy";
	}
}
