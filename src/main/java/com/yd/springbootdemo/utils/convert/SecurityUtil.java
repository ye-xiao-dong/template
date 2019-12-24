package com.yd.springbootdemo.utils.convert;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 安全模块（如各类加密函数，指标加密等）
 *
 * @author： 叶小东
 * @date： 2019/12/24 17:43
 */
public class SecurityUtil {

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    private static final String IV = "!@yw#10jqka#wd@!";

    private static MessageDigest md5Digest;
    private static char[] md5Char = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密指标代码 这里用的是AES的加密，模式是CBC，带偏移，用'\0'填充字符
     *
     * @param formulaCode 指标代码
     * @param userId      用户id
     * @return
     */
    public static String encryptFormulaCode(String formulaCode, String charset, Integer userId) throws Exception {
        byte[] codeBytes = formulaCode.getBytes(charset);
        byte[] password = encryptOfMD5Len16(userId.toString()).getBytes(charset);
        byte[] content = new byte[(codeBytes.length / 16 + (codeBytes.length % 16 == 0 ? 0 : 1)) * 16];
        for (int i = 0; i < codeBytes.length; i++) {
            content[i] = codeBytes[i];
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(password, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes(charset)));
        String result = Base64.getEncoder().encodeToString(cipher.doFinal(content));
        return result;
    }

    /**
     * 字符串MD5编码
     *
     * @param content 编码内容
     * @return 编码结果
     */
    public static String encryptOfMD5(String content) {
        byte[] bytes = content.getBytes();
        byte[] md = md5Digest.digest(bytes);
        char[] result = new char[md.length << 1];
        int k = 0;
        for (int i = 0; i < md.length; i++) {
            result[k++] = md5Char[md[i] >>> 4 & 0xf];
            result[k++] = md5Char[md[i] & 0xf];
        }
        return new String(result);
    }

    /**
     * 字符串MD5编码（16位长度）
     *
     * @param content 编码内容
     * @return 编码结果
     */
    public static String encryptOfMD5Len16(String content) {
        String result = encryptOfMD5(content);
        return result.substring(8, 24);
    }

    /**
     * base64编码函数
     *
     * @param str     需要编码的主体
     * @param charset 字符串的编码
     * @return 编码的结果
     * @throws Exception 编码翻皮水
     */
    public static String strEncryptBase64(String str, String charset) throws Exception {
        byte[] textByte = str.getBytes(charset);
        String result = BASE64_ENCODER.encodeToString(textByte);
        return result;
    }

    /**
     * base64解码函数
     *
     * @param str     需要解码的主体
     * @param charset 字符串的编码
     * @return 解码的结果
     * @throws Exception 解码翻皮水
     */
    public static String strDecryptBase64(String str, String charset) throws Exception {
        byte[] textByte = str.getBytes(charset);
        String result = new String(BASE64_DECODER.decode(textByte), charset);
        return result;
    }
}



