package com.exam.file;

import java.io.File;

public class DeleteFile {

    //private static String srcPath = "C:/Users/xuyajun/Workspaces/MyEclipse 10";
    private static String srcPath = "C:\\Users\\xuyajun\\Desktop\\20180402 试题难度测评（二期）\\vss\\住院医师试题难度评测系统";
    private static String[] directoryNames = new String[] {};
    private static String[] fileNames = new String[] {};
    private static String[] suffixs = new String[] {".vspscc",".scc"};

    public static void main(String[] args) {
        File srcFile = new File(srcPath);
        deleteFiles(srcFile);
    }

    private static void deleteFiles(File srcFile) {
        if (srcFile.isDirectory()) {
            for (String directoryName : directoryNames) {
                if (directoryName.equals(srcFile.getName())) {
                    deleteDirectory(srcFile);
                    deleteEmptyParent(srcFile);
                }
            }
            String files[] = srcFile.list();
            if (files != null) {
                for (String file : files) {
                    File srcFileFile = new File(srcFile, file);
                    deleteFiles(srcFileFile);
                }

            }
        } else if (srcFile.isFile()) {
            boolean check = false;
            String srcFileName = srcFile.getName();
            for (String fileName : fileNames) {
                if (srcFileName.equals(fileName)) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                for (String suffix : suffixs) {
                    if (srcFileName.endsWith(suffix)) {
                        check = true;
                        break;
                    }
                }
            }
            if (check) {
                srcFile.delete();
                System.out.println(srcFile);
                deleteEmptyParent(srcFile);
            }
        }
    }

    private static void deleteEmptyParent(File srcFile) {
        File parentDirectory = srcFile.getParentFile();
        if (parentDirectory.list().length == 0) {
            parentDirectory.delete();
            System.out.println(parentDirectory);
            deleteEmptyParent(parentDirectory);
        }
    }

    private static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        boolean isDelete = dir.delete();
        System.out.println(dir);
        return isDelete;
    }
}
