package com.fengpei.web.tool;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class EncryptUtils {
    public static String salt = "EIpWsyfiy@R@X#qn17!StJNdZK1fFF8iV6ffN!goZkqt#JxO";

    public static String sha256(byte[] cipherBytes) throws Exception {
        // 获取实例（SHA-512同理）
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digest = messageDigest.digest(cipherBytes);
        // 输出为16进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] StringToUTF8(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }
}