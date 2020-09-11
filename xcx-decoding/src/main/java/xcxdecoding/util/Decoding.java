package xcxdecoding.util;

/**
 * 微信小程序解码工具类
 */

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class Decoding {

    // 算法名称
    static  final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    static  final String algorithmStr = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;
    boolean isInited = false;

    //默认对称解密算法初始向量 iv
    static byte[] iv = { 0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38 };

    public static void init(byte[] keyBytes) {

        // 如果密钥不足16位,那么就补足.
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr, "BC");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("解密失败"+e);
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.out.println("解密失败"+e);
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            System.out.println("解密失败"+e);
            e.printStackTrace();
        }
    }
    /**
     * 解密方法
     *
     * @param encryptedData
     *        要解密的字符串
     * @param keyBytes
     *        解密密钥
     * @param ivs
     *        自定义对称解密算法初始向量 iv
     * @return
     */

    public static byte[] decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes,byte[] ivs) {
        byte[] encryptedText = null;
        init(keyBytes);
//      System.out.println("IV：" + new String(ivs));
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            System.out.println("解密失败"+e);
            e.printStackTrace();
        }
        return encryptedText;
    }
}
