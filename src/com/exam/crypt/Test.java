package com.exam.crypt;

public class Test {
    public static void main(String[] args) {
        String str = "aaa";
        String str2 = "abc";
        try {
            str = new DesEncrypt("exam2018").encrypt("2@qq.com");
            System.out.println(str);
            str2 = new DesDecrypt("exam2018").decrypt("88888");
            System.out.println(str2);
        } catch (Exception e) {
        }
    }
}
