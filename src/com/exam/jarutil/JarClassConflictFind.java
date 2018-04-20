package com.exam.jarutil;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassConflictFind {
    public static int count = 0;
    public static Map<String, String> classMap = new HashMap<>();

    static public void main(String[] args) {

        String libPath = "D:/jboss/jboss-4.2.2/server/default/deploy/hpcvc.war/WEB-INF/lib"; // 所要查找的JAR包的目录 

        FindClassInLocalSystem(libPath);

        if (JarClassConflictFind.count == 0) {
            System.out.println("Error:Can't Find Conflict Jar Files!");
        }
        System.out.println("Find Process Ended! Total Results:" + JarClassConflictFind.count);
    }

    private static void FindClassInLocalSystem(String path) {
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
                FindClassInLocalSystem(path + filelist[i]);
            } else {
                if (filelist[i].endsWith("jar")) {
                    try {
                        JarFile jarfile = new JarFile(path + filelist[i]);
                        for (Enumeration<JarEntry> e = jarfile.entries(); e.hasMoreElements();) {
                            String name = e.nextElement().toString();
                            if (name.endsWith(".class") && jarfile.getName().contains("xml-apis.jar")) {
                                if (classMap.containsKey(name)) {
                                    System.out.println("No." + ++JarClassConflictFind.count);
                                    System.out.println(classMap.get(name));
                                    System.out.println(path + filelist[i]);
                                    System.out.println(name + "\n");
                                } else {
                                    classMap.put(name, path + filelist[i]);
                                }
                            }
                        }
                        jarfile.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
