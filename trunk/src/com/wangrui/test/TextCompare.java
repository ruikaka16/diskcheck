package com.wangrui.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class TextCompare {
    public static void main(String[] args) throws Exception {
        File f1 = new File("c:/22.txt");
        File f2 = new File("c:/42.txt");

        System.out.println(equalsFile(f1, f2));
    }

    public static boolean equalsFile(File f1, File f2) throws Exception {
        // 不判断这两个文件是否存在
        if (f1.length() != f2.length()) {
            return false;
        }

        FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2);

        // 严格比较它们的字节
        int len = -1;
        byte[] bs1 = new byte[1024];
        byte[] bs2 = new byte[1024];
        while ((len = fis1.read(bs1)) != -1) {
            fis2.read(bs2, 0, len);
            boolean equals = Arrays.equals(bs1, bs2);// 直接用系统的方法来比较两个数组
            if (!equals) {
                fis1.close();
                fis2.close();
                return false;
            }
        }
        fis1.close();
        fis2.close();
        return true;
    }
}
