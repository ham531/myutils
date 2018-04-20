package com.exam.file;

import java.io.File;

public class RenamePackage {

    private static String filePath = "C:/Users/xuyajun/Workspace/runner";
    private static String oldPackageName = "running";
    private static String newPackageName = "runner";

    public static void main(String[] args) {
        renameDirectory(new File(filePath));
    }

    private static void renameDirectory(File srcFile) {
        if (srcFile.isDirectory()) {
            String files[] = srcFile.list();
            if (oldPackageName.equals(srcFile.getName())) {
                File destFile = new File(srcFile.getParent() + "/" + newPackageName);
                srcFile.renameTo(destFile);
                files = destFile.list();
                System.out.println("Rename: " + srcFile);
            }
            for (String file : files) {
                File srcFileFile = new File(srcFile, file);
                renameDirectory(srcFileFile);
            }
        }
    }
}
