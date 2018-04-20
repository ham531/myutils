package com.exam.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMessyCode {
    private static String filePath = "C:/Users/xuyajun/Workspaces/MyEclipse 10";
    private static String[] suffixs = new String[] { ".java", ".xml", ".jsp", ".tld", ".html", ".htm", "css", "js" };
    private static String[] confirmedCharsets = new String[] { ".java,GBK", ".xml,UTF-8" };

    public static void main(String[] args) {
        checkMessyCode(new File(filePath));
    }

    private static void checkMessyCode(File srcFile) {
        if (srcFile.isDirectory()) {
            String files[] = srcFile.list();
            for (String file : files) {
                File srcFileFile = new File(srcFile, file);
                checkMessyCode(srcFileFile);
            }
        } else if (srcFile.isFile()) {
            String fileName = srcFile.getName();
            if (checkSuffix(fileName)) {
                doCheck(srcFile);
            }
        }
    }

    private static void doCheck(File srcFile) {
        String charset = getFileCharset(srcFile);
        for (String confirmedCharset : confirmedCharsets) {
            String[] contents = confirmedCharset.split(",");
            if (srcFile.getName().endsWith(contents[0])) {
                charset = contents[1];
            }
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), charset));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                if (isMessyCode(temp.trim())) {
                    System.out.println(srcFile + "-------" + temp);
                    br.close();
                    return;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkSuffix(String fileName) {
        for (String suffix : suffixs) {
            if (fileName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("/s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("/p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!(c == '<' || c == '>' || c == '*' || c == '+' || c == '=' || c == '|' || c == '\''|| c == '^'|| c == '/')) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
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
}
