package com.exam.crypt;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;


public class DesDecrypt {
    private Cipher decryptCipher = null;
    
    /**
     * @Description: 指定密钥构造方法
     * @date Sep 22, 2010
     * @param strKey
     * @throws Exception
     */
    public DesDecrypt(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, getKey(strKey.getBytes()));
    }
    /**
     * @Description: 解密字符串
     * @date Sep 22, 2010 11:05:19 PM
     * @param strIn
     * @return
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }
    
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }
    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return new javax.crypto.spec.SecretKeySpec(arrB, "DES");
    }
    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
}
