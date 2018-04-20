package com.exam.file;

import java.io.File;

public class Test {

    /** @Description: 
     * @Author lujx
     * @Date 2016-11-28
     * @throws
     * @param args
     */
    public static void main(String[] args) {
        File file = new File("E:/文件备份/czc.war/WEB-INF/classes/com/dareway/ostm/webapp/action/competency/bpo/");
        if(!file.exists()) {
            file.mkdirs();
        }
    }

}