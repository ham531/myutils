package com.exam.file;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FindPackageInJar {
    private static File file = new File("C:/Users/xuyajun/Workspaces/MyEclipse 10");
    private static String keyword = "dareway";
    private static File latestFile;

    public static void main(String[] args) {
        findPackageInJar(file);
    }

    private static void findPackageInJar(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (String child : children) {
                findPackageInJar(new File(file, child));
            }
        }
        if (file.isFile()) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                try {
                    JarFile jar = new JarFile(file);
                    Enumeration<JarEntry> enumJar = jar.entries();
                    while (enumJar.hasMoreElements()) {
                        JarEntry je = enumJar.nextElement();
                        if (je.getName().contains(keyword)) {
                            if (latestFile != file) {
                                System.out.println(file);
                                latestFile = file;
                            }
                        }
                    }
                    jar.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
