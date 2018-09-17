package com.homedirect.util;

import com.homedirect.util.Notification;

public class Input {
	
	public static Double inputAmount() {
		Double money = null;
		try {
			System.out.print("Nhap so tien: ");
			money = Double.parseDouble(Notification.sc.nextLine());
		} catch (Exception e) {
			System.out.println("So tien khong duoc nhap ky tu!");
			money = null;
		}
		return money;
	}

	public static String inputString(String m) {
		System.out.print(m);
		String s = Notification.sc.nextLine();
		return s;
	}

	public static String inputString() {
		String s = Notification.sc.nextLine();
		return s;
	}

	public static String inputAccountNumber() {
		System.out.print("Nhap so tai khoan: ");
		String accountNumber = Notification.sc.nextLine();
		return accountNumber;
	}
}
