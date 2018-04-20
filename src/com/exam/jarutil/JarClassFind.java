package com.exam.jarutil;

import java.util.*;
import java.io.*;

public class JarClassFind {
    public static int count = 0;

    static public void main(String[] args) {
        /* 
         * if (args.length < 2) { showHowToUsage(); return; } 
         */
        String className = "HSSFSheet"; // 要查询的class,要带包名的类名  
        String libPath = "C:/Users/xuyajun/Workspaces/MyEclipse 8.5/hpcvc/WebRoot/WEB-INF/lib"; // 所要查找的JAR包的目录  

        String absoluteclassname = className.replace('.', '/') + ".class";

        System.out.println("Find class [" + className + "] in Path [" + libPath + "] Results:");
        FindClassInLocalSystem(libPath, absoluteclassname);
        if (JarClassFind.count == 0) {
            System.out.println("Error:Can't Find Such Jar File!");
        }
        System.out.println("Find Process Ended! Total Results:" + JarClassFind.count);
    }

    private static void FindClassInLocalSystem(String path, String classname) {
        if (path.charAt(path.length() - 1) != '/') {
            path += '/';
        }
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Error: Path not Existed! Please Check it out!");
            return;
        }
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
            File temp = new File(path + filelist[i]);
            if ((temp.isDirectory() && !temp.isHidden() && temp.exists())) {
                FindClassInLocalSystem(path + filelist[i], classname);
            } else {
                if (filelist[i].endsWith("jar")) {
                    try {
                        java.util.jar.JarFile jarfile = new java.util.jar.JarFile(path + filelist[i]);
                        for (Enumeration e = jarfile.entries(); e.hasMoreElements();) {
                            String name = e.nextElement().toString();
                            if (name.equals(classname) || name.indexOf(classname) > -1) {
                                System.out.println("No." + ++JarClassFind.count);
                                System.out.println("Jar Package:" + path + filelist[i]);
                                System.out.println(name);
                            }
                        }
                    } catch (Exception eee) {
                    }
                }
            }
        }
    }

    public static void showHowToUsage() {
        System.out.println("Usage: Java -cp. JarClassFind <source path> <source class name>");
        System.out.println("Usage: Java -classpath. JarClassFind <source path> <source class name>");
        System.out.println("");
        System.out.println("<source path>:\t\tPath to Find eg:D:/Jbuilder");
        System.out.println("<source class name>:\tClass to Find eg:java.applet.Applet");
    }
}
