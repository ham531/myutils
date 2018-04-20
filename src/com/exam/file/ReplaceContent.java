package com.exam.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ReplaceContent {

    private static String filePath = "C:/Users/xuyajun/Workspaces/MyEclipse 10/tp";
    private static String[] suffixs = new String[] {};
    private static String[] replaceWords = new String[] {};
    private static String[] confirmedCharsets = new String[] {};

    public static void main(String[] args) {
        replaceContent(new File(filePath));
    }

    private static void replaceContent(File srcFile) {
        if (srcFile.isDirectory()) {
            String files[] = srcFile.list();
            for (String file : files) {
                File srcFileFile = new File(srcFile, file);
                replaceContent(srcFileFile);
            }
        } else if (srcFile.isFile()) {
            String fileName = srcFile.getName();
            if (checkSuffix(fileName)) {
                doReplace(srcFile);
            }
        }
    }

    private static void doReplace(File srcFile) {
        String charset = getFileCharset(srcFile);
        StringBuilder result = new StringBuilder();
        for (String confirmedCharset : confirmedCharsets) {
            String[] contents = confirmedCharset.split(",");
            if (srcFile.getName().endsWith(contents[0])) {
                charset = contents[1];
            }
        }
        boolean isMatched = false;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), charset));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                for (String replaceWord : replaceWords) {
                    String[] contents = replaceWord.split(",");
                    if (temp.indexOf(contents[0]) >= 0) {
                        temp = temp.replace(contents[0], contents[1]);
                        isMatched = true;
                        System.out.println(srcFile + "------{" + contents[0] + ":" + contents[1] + "}" + "[" + charset + "]");
                    }
                }
                result.append(temp).append("\n");
            }
            br.close();
            if (isMatched) {
                srcFile.setWritable(true);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srcFile), charset));
                bw.write(result.toString());
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFileCharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                bis.close();
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    private static boolean checkSuffix(String fileName) {
        for (String suffix : suffixs) {
            if (fileName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
