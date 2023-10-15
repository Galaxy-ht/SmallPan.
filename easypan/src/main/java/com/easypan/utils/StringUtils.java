package com.easypan.utils;

import com.easypan.entity.constants.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Tao
 * @since 2023/08/19
 */

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    
    /**
     * 生成随机数
     * @param Integer count
     * @return java.lang.String
    */
    public static String getRandomNumber(Integer count) {
        return RandomStringUtils.random(count, false, true);
    }

    public static boolean isEmpty(String str) {

        if (null == str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)) {
            return true;
        } else return "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {

        if (null == str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)) {
            return false;
        } else return !"".equals(str.trim());
    }

    public static String encodeByMd5(String originalString) {
        return isEmpty(originalString) ? null : DigestUtils.md5Hex(originalString);
    }

    public static boolean pathIsOk(String filePath) {
        if (isEmpty(filePath)) {
            return false;
        }
        return !filePath.contains("../") && !filePath.contains(".\\");
    }

    public static String getFileSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return fileName.substring(index);
    }

    public static String getFileNameNoSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        }
        fileName = fileName.substring(0, index);
        return fileName;
    }

    public static String getRandomString(Integer count) {
        return RandomStringUtils.random(count, true, true);
    }

    public static String rename(String fileName) {
        // 获取不加后缀的文件名
        String fileNameReal = getFileNameNoSuffix(fileName);
        // 获取文件的后缀名
        String suffix = getFileSuffix(fileName);
        return fileNameReal + "_" + getRandomString(Constants.LENGTH_5) + suffix;
    }
}
