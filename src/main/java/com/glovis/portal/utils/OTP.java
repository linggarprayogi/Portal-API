package com.glovis.portal.utils;

import java.util.Random;

public class OTP {

	/**
	 * generate OTP
	 * 
	 * @param length
	 * @return
	 */
	public char[] generateOTP(int length) {
		String numbers = "1234567890";
		Random random  = new Random();

		char[] otp     = new char[length];

		for (int i = 0; i < length; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp[i] = numbers.charAt(random.nextInt(numbers.length()));
		}

		return otp;
	}
}
