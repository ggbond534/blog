package com.blog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密工具类
 *
 * <p>提供静态方法对字符串进行 MD5 哈希加密，用于密码存储。</p>
 * <p>注意：MD5 已不被视为安全的密码哈希算法，生产环境建议使用 BCrypt 或 Argon2。</p>
 *
 * @author blog
 * @version 1.0.0
 */
public class Md5Util {

    /**
     * 使用 MD5 算法对输入字符串进行哈希加密
     *
     * <p>加密流程：</p>
     * <ol>
     *   <li>获取 MessageDigest("MD5") 实例</li>
     *   <li>对输入字符串的字节进行摘要计算</li>
     *   <li>将每个字节转换为两位十六进制字符串</li>
     *   <li>拼接所有十六进制字符串得到最终 MD5 值</li>
     * </ol>
     *
     * @param input 需要加密的原始字符串
     * @return 32位小写十六进制 MD5 字符串；如果 input 为 null 则返回 null
     */
    public static String md5(String input) {
        // 空值保护：null 输入直接返回 null
        if (input == null) {
            return null;
        }
        try {
            // 获取 MD5 消息摘要算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 对输入字符串的字节数组进行摘要计算
            byte[] digest = md.digest(input.getBytes());
            // 将每个字节转换为两位十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                // b & 0xff 将 byte 转为无符号整数，%02x 格式化为两位十六进制
                sb.append(String.format("%02x", b & 0xff));
            }
            // 返回 32 位 MD5 字符串
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            // MD5 是 Java 标准算法，理论上不会抛出此异常
            throw new RuntimeException("MD5 加密失败", ex);
        }
    }
}
