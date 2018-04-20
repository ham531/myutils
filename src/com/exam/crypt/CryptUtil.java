package com.exam.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class CryptUtil {
	public static void main(String[] args) {
		String path = "e:/examfile";
		String fileName = "机动车.m";
		String srcFilePath = path + "/" + fileName;
		String path1 = path + "/" + fileName.substring(0, fileName.length()-4) + ".m";
		String path2 = path + "/" + fileName.substring(0, fileName.length()-2) + ".zip";
		if (fileName.endsWith(".zip")) {
			encryptFile(srcFilePath, path1, "2016");
		} else if (fileName.endsWith(".m")) {
			decryptFile(srcFilePath, path2, "2016");
		}
	}

	// 加密
	private static void encryptFile(String srcFilePath, String desFilePath,
			String key) {
		File srcFile = new File(srcFilePath);
		File desFile = new File(desFilePath);
		byte[] data = null;
		try {
			if (srcFile.exists()) {
				InputStream in = new FileInputStream(srcFile);
				OutputStream out = new FileOutputStream(desFile);
				DesEncrypt desEncrypt = new DesEncrypt(key);
				int length = in.available();
				data = new byte[length];
				in.read(data);
				data = desEncrypt.encrypt(data);
				out.write(data);
				out.flush();
				in.close();
				out.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 解密
	private static void decryptFile(String srcFilePath, String desFilePath,
			String key) {
		File srcFile = new File(srcFilePath);
		File desFile = new File(desFilePath);
		byte[] data = null;
		try {
			if (srcFile.exists()) {
				InputStream in = new FileInputStream(srcFile);
				OutputStream out = new FileOutputStream(desFile);
				DesDecrypt desEncrypt = new DesDecrypt(key);
				int length = in.available();
				data = new byte[length];
				in.read(data);
				data = desEncrypt.decrypt(data);
				out.write(data);
				out.flush();
				in.close();
				out.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
