package com.exam.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetModifiedFile {

    private static String originRootDir = "C:\\Users\\xuyajun\\Workspaces\\MyEclipse 8.5\\";
    private static String destRootDir = "E:\\文件备份\\";
    private static String projectName = "hpcvc";
    private static String webRoot = "WebRoot";
    private static String timeFrom = "2018-04-18 17:30";
    private static String warSuffix = ".war";
    private static String[] ignoredPrefixes = new String[] { "." };
    private static String[] ignoredSuffixes = new String[] {};
    private static String[] ignoredFiles = new String[] { "classes", "hibernate.cfg.xml", "hibernate.reveng.xml", "vssver2.scc" };

    private static List<String> srcDestList = new ArrayList<>();
    private static List<String> warDestList = new ArrayList<>();
    private static String originDir = originRootDir + projectName;
    private static String destDir = destRootDir + projectName;
    private static long timeFromL;
    private static long timeToL;

    static {
        try {
            timeFromL = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeFrom).getTime();
            timeToL = new Date().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File originRoot = new File(originDir);
        File destRoot = new File(destDir);
        if (!originRoot.exists()) {
            System.out.println("路径不存在：" + originDir);
            return;
        }
        if (destRoot.exists()) {
            deleteDir(destRoot);
            deleteDir(new File(destDir + warSuffix));
        }
        getFiles(originRoot, destRoot);
        System.out.println("源文件(" + srcDestList.size() + ")：");
        for (String srcDest : srcDestList) {
            System.out.println(srcDest);
        }
        System.out.println("部署包(" + warDestList.size() + ")：");
        for (String warDest : warDestList) {
            System.out.println(warDest);
        }
    }

    private static void getFiles(File originFile, File destFile) {
        if (checkIgnored(originFile.getName())) {
            return;
        }
        if (originFile.isDirectory()) {
            String childFiles[] = originFile.list();
            for (String childFile : childFiles) {
                File originChildFile = new File(originFile, childFile);
                File destChildFile = new File(destFile, childFile);
                getFiles(originChildFile, destChildFile);
            }
        } else if (originFile.isFile()) {
            long lastModified = originFile.lastModified();
            if (lastModified > timeFromL && lastModified < timeToL) {
                if (copyFile(originFile, destFile)) {
                    srcDestList.add(destFile.getAbsolutePath());
                    getWar(originFile);
                }
            }
        }
    }

    private static void getWar(File srcFile) {
        String warPath = srcFile.getPath();
        if (warPath.contains("\\src\\")) {
            warPath = warPath.replace("\\src\\", "\\" + webRoot + "\\WEB-INF\\classes\\");
        }
        if (warPath.endsWith(".java")) {
            warPath = warPath.replace(".java", ".class");
            File warFile = new File(warPath);
            String warFileName = warFile.getName();
            String[] fileNameList = warFile.getParentFile().list();
            for (String fileName : fileNameList) {
                if (fileName.replace(".class", "").startsWith(warFileName.replace(".class", "") + "$")) {
                    copyWar(warFile.getParent() + File.separator + fileName);
                }
            }
        }
        copyWar(warPath);
    }

    private static void copyWar(String originPath) {
        String copyFrom = originDir + File.separator + webRoot;
        String copyTo = destDir + warSuffix;
        String destPath = originPath.replace(copyFrom, copyTo);

        File warOriginFile = new File(originPath);
        File warDestFile = new File(destPath);
        if (!warOriginFile.exists()) {
            System.out.println("文件不存在：" + warOriginFile);
            return;
        }
        if (copyFile(warOriginFile, warDestFile)) {
            warDestList.add(warDestFile.getAbsolutePath());
        }
    }

    private static boolean checkIgnored(String originFileName) {
        for (String ignoredPrefix : ignoredPrefixes) {
            if (originFileName.startsWith(ignoredPrefix)) {
                return true;
            }
        }
        for (String ignoredSuffix : ignoredSuffixes) {
            if (originFileName.endsWith(ignoredSuffix)) {
                return true;
            }
        }
        for (String ignoredFile : ignoredFiles) {
            if (originFileName.equals(ignoredFile)) {
                return true;
            }
        }
        return false;
    }

    private static boolean copyFile(File originFile, File destFile) {
        File destParentFile = destFile.getParentFile();
        if (!destParentFile.exists()) {
            destParentFile.mkdirs();
        }
        try {
            InputStream in = new FileInputStream(originFile);
            OutputStream out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                deleteDir(new File(dir, child));
            }
        }
        dir.delete();
    }
}
