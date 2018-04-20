package com.exam.classfile;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ClassFileInfo {

    private static String classPath = "C:/Users/xuyajun/workspace/study/bin/com/demo/classfile/Test.class";
    private static Map<Integer, String> utf8_map = new HashMap<>();
    private static int cursor = 0;

    public static void main(String[] args) {

        byte[] fileBytes = getFileBytes(classPath);

        printMagic(fileBytes);

        printVersion(fileBytes);

        printConstantPool(fileBytes);

        printAccessFlags(fileBytes);

        printThisClass(fileBytes);

        printSuperClass(fileBytes);

        printInterface(fileBytes);

        printFields(fileBytes);

        printMethods(fileBytes);
    }

    private static void printMagic(byte[] fileBytes) {
        System.out.println("-------------magic---------------");
        String magic = getHexString(fileBytes, cursor, cursor += 4);
        System.out.println("magic = " + magic);
        if (!magic.equals("CAFEBABE")) {
            System.exit(0);
        }
    }

    private static void printVersion(byte[] fileBytes) {
        System.out.println("-------------version---------------");
        String minorVersion = getHexString(fileBytes, cursor, cursor += 2);
        String majorVersion = getHexString(fileBytes, cursor, cursor += 2);
        System.out.println("minor_version = " + hex2Int(minorVersion));
        System.out.println("major_version = " + hex2Int(majorVersion));
    }

    private static void printConstantPool(byte[] fileBytes) {
        System.out.println("-------------constant_pool---------------");
        int constantPoolCount = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
        for (int i = 1; i < constantPoolCount; i++) {
            int tag = hex2Int(getHexString(fileBytes, cursor, cursor += 1));
            switch (tag) {
                case 1:
                    int length = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    String utf8 = hex2Str(getHexString(fileBytes, cursor, cursor += length));
                    System.out.println(i + ":" + utf8 + "(Utf8)");
                    utf8_map.put(i, utf8);
                    break;
                case 3:
                    int integerVal = Integer.parseInt(getHexString(fileBytes, cursor, cursor += 4), 16);
                    System.out.println(i + ":" + integerVal + "(Integer)");
                    break;
                case 4:
                    String floatHex = getHexString(fileBytes, cursor, cursor += 4);
                    float floatVal = Float.intBitsToFloat(Integer.parseInt(floatHex, 16));
                    System.out.println(i + ":" + floatVal + "(Float)");
                    break;
                case 5:
                    long longVal = Long.parseLong(getHexString(fileBytes, cursor, cursor += 8), 16);
                    System.out.println(i + ":" + longVal + "(Long)");
                    i++;
                    break;
                case 6:
                    String doubleHex = getHexString(fileBytes, cursor, cursor += 8);
                    double doubleVal = Double.longBitsToDouble(Long.parseLong(doubleHex, 16));
                    System.out.println(i + ":" + doubleVal + "(Double)");
                    i++;
                    break;
                case 7:
                    int classIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println(i + ":" + classIndex + "(Class)");
                    break;
                case 8:
                    int stringIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println(i + ":" + stringIndex + "(String)");
                    break;
                case 9:
                    int fieldrefIndex1 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    int fieldrefIndex2 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println(i + ":" + fieldrefIndex1 + "," + fieldrefIndex2 + "(Fieldref)");
                    break;
                case 10:
                    int methodrefIndex1 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    int methodrefIndex2 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println(i + ":" + methodrefIndex1 + "," + methodrefIndex2 + "(Fieldref)");
                    break;
                case 11:
                    int interfaceMethodrefIndex1 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    int interfaceMethodrefIndex2 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println(i + ":" + interfaceMethodrefIndex1 + "," + interfaceMethodrefIndex2 + "(InterfaceMethodref)");
                    break;
                case 12:
                    int nameAndTypeIndex1 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    int nameAndTypeIndex2 = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println(i + ":" + nameAndTypeIndex1 + "," + nameAndTypeIndex2 + "(NameAndType)");
                    break;
                default:
                    break;
            }
        }
    }

    private static void printAccessFlags(byte[] fileBytes) {
        System.out.println("-------------access_flags---------------");
        String accessFlags = getAccessFlags(getHexString(fileBytes, cursor, cursor += 2));
        System.out.println("access_flags = " + accessFlags);
    }

    private static void printThisClass(byte[] fileBytes) {
        System.out.println("-------------this_class---------------");
        int thisClassIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
        System.out.println("this_class = " + thisClassIndex);
    }

    private static void printSuperClass(byte[] fileBytes) {
        System.out.println("-------------super_class---------------");
        int superClassIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
        System.out.println("super_class = " + superClassIndex);
    }

    private static void printInterface(byte[] fileBytes) {
        System.out.println("-------------interfaces---------------");
        int interfaceCount = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
        for (int i = 0; i < interfaceCount; i++) {
            int interfacesIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
            System.out.println("interfaces = " + interfacesIndex);
        }
    }

    private static void printFields(byte[] fileBytes) {
        System.out.println("-------------fields---------------");
        int fieldsCount = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
        for (int i = 0; i < fieldsCount; i++) {
            String accessFlags = getFieldAccess(getHexString(fileBytes, cursor, cursor += 2));
            int nameIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
            int descriptorIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
            System.out.println(i + 1 + ": access_flags = " + accessFlags + "; name_index = " + nameIndex + "; descriptor_index = " + descriptorIndex);
            printAttributes(fileBytes);
            System.out.println();
        }
    }

    private static void printMethods(byte[] fileBytes) {
        System.out.println("-------------methods---------------");
        int methodsCount = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
        for (int i = 0; i < methodsCount; i++) {
            String accessFlags = getMethodAccess(getHexString(fileBytes, cursor, cursor += 2));
            int nameIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
            int descriptorIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
            System.out.println(i + 1 + ": access_flags = " + accessFlags + "; name_index = " + nameIndex + "; descriptor_index = " + descriptorIndex);
            printAttributes(fileBytes);
            System.out.println();
        }
    }

    private static void printAttributes(byte[] fileBytes) {
        int attributesCount = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
        for (int i = 0; i < attributesCount; i++) {
            int attributeNameIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
            int attributeLength = hex2Int(getHexString(fileBytes, cursor, cursor += 4));
            String attributeName = utf8_map.get(attributeNameIndex);
            System.out.println("<" + attributeName + ">: ");
            switch (attributeName) {
                case "Code":
                    int maxStack = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    int maxLocals = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    int codeLength = hex2Int(getHexString(fileBytes, cursor, cursor += 4));
                    String code = getHexString(fileBytes, cursor, cursor += codeLength);
                    int exceptionTableLength = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println("max_stack = " + maxStack + "; max_locals = " + maxLocals + "; code_length = " + codeLength + "; code = " + code);
                    for (int j = 0; j < exceptionTableLength; j++) {
                        int startPc = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int endPc = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int handlerPc = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int catchType = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        System.out.println("start_pc = " + startPc + "; end_pc = " + endPc + "; handler_pc = " + handlerPc + "; catch_type = " + catchType);
                    }
                    printAttributes(fileBytes);
                    break;
                case "Exceptions":
                    int numberOfException = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    for (int j = 0; j < numberOfException; j++) {
                        int exceptionTableIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        System.out.println("exception_table_index = " + exceptionTableIndex);
                    }
                    break;
                case "LineNumberTable":
                    int lineNumberTableLength = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    for (int j = 0; j < lineNumberTableLength; j++) {
                        int startPc = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int lineNumber = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        System.out.println("start_pc = " + startPc + "; lineNumber = " + lineNumber);
                    }
                    break;
                case "LocalVariableTable":
                    int localVariableTableLength = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    for (int j = 0; j < localVariableTableLength; j++) {
                        int startPc = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int length = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int nameIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int descriptorIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int index = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        System.out.println("start_pc = " + startPc + "; length = " + length + "; name_index = " + nameIndex + "; descriptor_index = " + descriptorIndex + "; index = " + index);
                    }
                    break;
                case "SourceFile":
                    int sourceFileIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println("source_file_index = " + sourceFileIndex);
                    break;
                case "ConstantValue":
                    int constantValueIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    System.out.println("constant_value_index = " + constantValueIndex);
                    break;
                case "InnerClasses":
                    int numberOfClasses = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                    for (int j = 0; j < numberOfClasses; j++) {
                        int innerClassInfoIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int outerClassInfoIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        int innerNameIndex = hex2Int(getHexString(fileBytes, cursor, cursor += 2));
                        String innerClassAccessFlags = getHexString(fileBytes, cursor, cursor += 2);
                        System.out.println("inner_class_info_index = " + innerClassInfoIndex + "; outer_class_info_index = " + outerClassInfoIndex + "; inner_name_index = " + innerNameIndex
                                + "; inner_class_access_flags = " + innerClassAccessFlags);
                    }
                    break;
                case "Deprecated":
                    break;
                case "Synthetic":
                    break;
                default:
                    String info = getHexString(fileBytes, cursor, cursor += attributeLength);
                    System.out.println("info = " + info);
                    break;
            }
        }
    }

    private static String getHexString(byte[] fileBytes, int from, int to) {
        return fileBytes2HexString(Arrays.copyOfRange(fileBytes, from, to));
    }

    private static byte[] getFileBytes(String filePath) {
        byte[] fileBytes = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            fileBytes = new byte[fileInputStream.available()];
            fileInputStream.read(fileBytes);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    private static String fileBytes2HexString(byte[] bytes) {
        String hexString = "";
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString += hex.toUpperCase();
        }
        return hexString;
    }

    private static String hex2Str(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    private static int hex2Int(String hex) {
        return Integer.parseInt(hex, 16);
    }

    private static String getAccessFlags(String code) {
        StringBuilder result = new StringBuilder();
        if ((hex2Int(code) & 0x0001) == 0x0001) {
            result.append("[PUBLIC]");
        }
        if ((hex2Int(code) & 0x0010) == 0x0010) {
            result.append("[FINAL]");
        }
        if ((hex2Int(code) & 0x0020) == 0x0020) {
            result.append("[SUPER]");
        }
        if ((hex2Int(code) & 0x0200) == 0x0200) {
            result.append("[INTERFACE]");
        }
        if ((hex2Int(code) & 0x0400) == 0x0400) {
            result.append("[ABSTRACT]");
        }
        if ((hex2Int(code) & 0x1000) == 0x1000) {
            result.append("[SYNTHETIC]");
        }
        if ((hex2Int(code) & 0x2000) == 0x2000) {
            result.append("[ANNOTATION]");
        }
        if ((hex2Int(code) & 0x4000) == 0x4000) {
            result.append("[ENUM]");
        }
        if ("".equals(result.toString())) {
            return code;
        }
        return result.toString();
    }

    private static String getFieldAccess(String code) {
        StringBuilder result = new StringBuilder();
        if ((hex2Int(code) & 0x0001) == 0x0001) {
            result.append("[PUBLIC]");
        }
        if ((hex2Int(code) & 0x0002) == 0x0002) {
            result.append("[PRIVATE]");
        }
        if ((hex2Int(code) & 0x0004) == 0x0004) {
            result.append("[PROTECTED]");
        }
        if ((hex2Int(code) & 0x0008) == 0x0008) {
            result.append("[STATIC]");
        }
        if ((hex2Int(code) & 0x0010) == 0x0010) {
            result.append("[FINAL]");
        }
        if ((hex2Int(code) & 0x0040) == 0x0040) {
            result.append("[VOLATILE]");
        }
        if ((hex2Int(code) & 0x0080) == 0x0080) {
            result.append("[TRANSIENT]");
        }
        if ((hex2Int(code) & 0x1000) == 0x1000) {
            result.append("[SYNTHETIC]");
        }
        if ((hex2Int(code) & 0x4000) == 0x4000) {
            result.append("[ENUM]");
        }
        if ("".equals(result.toString())) {
            return code;
        }
        return result.toString();
    }

    private static String getMethodAccess(String code) {
        StringBuilder result = new StringBuilder();
        if ((hex2Int(code) & 0x0001) == 0x0001) {
            result.append("[PUBLIC]");
        }
        if ((hex2Int(code) & 0x0002) == 0x0002) {
            result.append("[PRIVATE]");
        }
        if ((hex2Int(code) & 0x0004) == 0x0004) {
            result.append("[PROTECTED]");
        }
        if ((hex2Int(code) & 0x0008) == 0x0008) {
            result.append("[STATIC]");
        }
        if ((hex2Int(code) & 0x0010) == 0x0010) {
            result.append("[FINAL]");
        }
        if ((hex2Int(code) & 0x0020) == 0x0020) {
            result.append("[SYNCHRONIZED]");
        }
        if ((hex2Int(code) & 0x0040) == 0x0040) {
            result.append("[BRIDGE]");
        }
        if ((hex2Int(code) & 0x0080) == 0x0080) {
            result.append("[VARARGS]");
        }
        if ((hex2Int(code) & 0x0100) == 0x0100) {
            result.append("[NATIVE]");
        }
        if ((hex2Int(code) & 0x0400) == 0x0400) {
            result.append("[ABSTRACT]");
        }
        if ((hex2Int(code) & 0x0800) == 0x0800) {
            result.append("[STRICT]");
        }
        if ((hex2Int(code) & 0x1000) == 0x1000) {
            result.append("[SYNTHETIC]");
        }
        if ("".equals(result.toString())) {
            return code;
        }
        return result.toString();
    }
}
