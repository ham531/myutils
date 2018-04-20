package com.exam.icbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

	public static void main(String[] args) {
		String srcPath = "E:/examfile/考生成绩-1729-20161024-105935.m";
		String destPath = "E:/temp/sssd/考生成绩-1729-20161024-105935.m";
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		try {
			copyFile(srcFile, destFile);
			copyFolder(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 复制一个文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	private static void copyFile(File srcFile, File destFile) throws IOException {
		File desParentFile = destFile.getParentFile();
		if (!desParentFile.exists()) {
			desParentFile.mkdirs();
			copyFile(srcFile, destFile);
		} else {
			InputStream in = new FileInputStream(srcFile);
			OutputStream out = new FileOutputStream(destFile);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
			
		}
	}

	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	private static void copyFolder(File srcFile, File destFile) throws IOException {
		if (srcFile.isDirectory()) {
			if (!destFile.exists()) {
				destFile.mkdir();
			}
			String files[] = srcFile.list();
			for (String file : files) {
				File srcFileFile = new File(srcFile, file);
				File destFileFile = new File(destFile, file);
				// 递归复制
				copyFolder(srcFileFile, destFileFile);
			}
		} else {
			InputStream in = new FileInputStream(srcFile);
			OutputStream out = new FileOutputStream(destFile);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}
}
