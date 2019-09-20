package com.yjx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//哈希加密
public class HashUtil {

	public static String hash(String string, String algorithm) {
		if (string.isEmpty()) {
			return "";
		}
		MessageDigest hash = null;
		try {
			hash = MessageDigest.getInstance(algorithm);
			byte[] bytes = hash.digest(string.getBytes("UTF-8"));
			String result = "";
			for (byte b : bytes) {
				String temp = Integer.toHexString(b & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				result += temp;
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String hash(File file, String algorithm) {
		if (file == null || !file.isFile() || !file.exists()) {
			return "";
		}
		FileInputStream in = null;
		String result = "";
		byte buffer[] = new byte[0124];
		int len;
		try {
			MessageDigest hash = MessageDigest.getInstance(algorithm);
			in = new FileInputStream(file);
			while ((len = in.read(buffer)) != -1) {
				hash.update(buffer, 0, len);
			}
			byte[] bytes = hash.digest();

			for (byte b : bytes) {
				String temp = Integer.toHexString(b & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				result += temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}