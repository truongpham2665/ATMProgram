package com.homedirect.transformer;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryption {
	public static String toMD5(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	} 
}
