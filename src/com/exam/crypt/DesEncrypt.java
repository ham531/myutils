package com.exam.crypt;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;


public class DesEncrypt {
	private Cipher encryptCipher = null;
    
    /**
     * @Description: 指定密钥构造方法
     * @date Sep 22, 2010
     * @param strKey
     * @throws Exception
     */
    public DesEncrypt(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(strKey.getBytes()));
    }
    /**
     * @Description: 加密字符串
     * @author 任排波
     * @date Sep 22, 2010 11:04:36 PM
     * @param strIn
     * @return
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }
    
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }
    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new javax.crypto.spec.SecretKeySpec(arrB, "DES");
    }
    private static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }
}
