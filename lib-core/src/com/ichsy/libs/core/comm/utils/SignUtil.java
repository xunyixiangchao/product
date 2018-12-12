package com.ichsy.libs.core.comm.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 * Created by liuyuhang on 2016/12/2.
 */

public class SignUtil {

    public static class AES {
        public static String encrypt(String seed, String cleartext) throws Exception {
            byte[] rawKey = getRawKey(seed.getBytes());
            byte[] result = encrypt(rawKey, cleartext.getBytes());
            return toHex(result).toLowerCase(Locale.CHINA);
//            return result.toString();
        }

        public static String decrypt(String seed, String encrypted) throws Exception {
            byte[] rawKey = getRawKey(seed.getBytes());
            byte[] enc = toByte(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
        }

        private static byte[] getRawKey(byte[] seed) throws Exception {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            sr.setSeed(seed);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            return raw;
        }


        private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(clear);
        }

        private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(encrypted);
        }

        public static String toHex(String txt) {
            return toHex(txt.getBytes());
        }

        public static String fromHex(String hex) {
            return new String(toByte(hex));
        }

        private static byte[] toByte(String hexString) {
            int len = hexString.length() / 2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++)
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
            return result;
        }

        private static String toHex(byte[] buf) {
            if (buf == null)
                return "";
            StringBuffer result = new StringBuffer(2 * buf.length);
            for (byte aBuf : buf) {
                appendHex(result, aBuf);
            }
            return result.toString();
        }

        private final static String HEX = "0123456789ABCDEF";

        private static void appendHex(StringBuffer sb, byte b) {
            sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
        }

    }

    public static class DES {
        //        private static final Logger logger = LoggerFactory.getLogger(DesUtils.class);
        private final static String DES = "DES";
        private final static String CIPHER_ALGORITHM = "DES/ECB/NoPadding";

        /**
         * 加密String明文输入,String密文输出
         *
         * @param strData String明文
         * @param strKey  秘钥
         * @return String密文
         * @throws UnsupportedEncodingException
         * @throws DesException
         */
        public static String encrypt(String strData, String strKey) throws UnsupportedEncodingException {
            byte[] byteKey = strKey.getBytes("utf-8");
            byte[] byteMi = null;
            byte[] byteMing = null;
            byte[] buf = strData.getBytes("utf-8");
            int len = 8 - buf.length % 8;
            byteMing = new byte[buf.length + len];
            System.arraycopy(buf, 0, byteMing, 0, buf.length);
            byteMi = encrypt(byteMing, byteKey);
            return Base64Util.encode(byteMi);
        }

        /**
         * 解密 以String密文输入,String明文输出
         *
         * @param strData String明文
         * @param strKey  秘钥
         * @return String明文
         * @throws Exception
         */
        public static String decrypt(String strMi, String strKey) throws Exception {
            byte[] byteKey = strKey.getBytes("utf-8");
            byte[] bytebase64 = null;
            String strMing = null;
            try {
                bytebase64 = Base64Util.decode(strMi);
                strMing = new String(decrypt(bytebase64, byteKey), "utf-8");
            } catch (Exception e) {
//                logger.error("DES解密错误", e);
                throw new Exception("DES解密错误", e);
            }
            return strMing.trim();
        }

        /**
         * 生成密钥
         *
         * @return
         * @throws NoSuchAlgorithmException
         */
    /*
    private static byte[] initKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(DES);
        kg.init(56);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }
    */

        /**
         * 加密
         *
         * @param src 数据源
         * @param key 密钥，长度必须是8的倍数
         * @return 返回加密后的数据
         * @throws DesException
         */
        private static byte[] encrypt(byte[] src, byte[] key) {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            try {
                // 从原始密匙数据创建DESKeySpec对象
                DESKeySpec dks = new DESKeySpec(key);
                // 创建一个密匙工厂，然后用它把DESKeySpec转换成
                // 一个SecretKey对象
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
                SecretKey securekey = keyFactory.generateSecret(dks);
                // Cipher对象实际完成加密操作,NoPadding为填充方式 默认为PKCS5Padding
                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
                // 用密匙初始化Cipher对象
                cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
                // 现在，获取数据并加密
                // 正式执行加密操作
                return cipher.doFinal(src);
            } catch (NoSuchAlgorithmException e) {
                // LOG.error("算数错误", e);
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // LOG.error("无效key错误", e);
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                // LOG.error("无效key戳无", e);
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // LOG.error("填充错误", e);
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                // LOG.error("非法数据块", e);
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // LOG.error("错误的填充", e);
                e.printStackTrace();
            }
            return null;
        }


        /**
         * 解密
         *
         * @param src 数据源
         * @param key 密钥，长度必须是8的倍数
         * @return 返回解密后的原始数据
         * @throws DesException
         * @throws Exception
         */
        private static byte[] decrypt(byte[] src, byte[] key) throws Exception {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            try {
                // 从原始密匙数据创建一个DESKeySpec对象
                DESKeySpec dks = new DESKeySpec(key);
                // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
                // 一个SecretKey对象
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
                SecretKey securekey = keyFactory.generateSecret(dks);
                // Cipher对象实际完成解密操作
                Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
                // 用密匙初始化Cipher对象
                cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
                // 现在，获取数据并解密
                // 正式执行解密操作
                return cipher.doFinal(src);
            } catch (NoSuchAlgorithmException e) {
//                logger.error("算数错误", e);
                throw new Exception("DES解密错误:算数错误", e);
            } catch (InvalidKeyException e) {
//                logger.error("无效key错误", e);
                throw new Exception("DES解密错误:无效key错误", e);
            } catch (InvalidKeySpecException e) {
//                logger.error("无效key戳无", e);
                throw new Exception("DES解密错误:无效key戳无", e);
            } catch (NoSuchPaddingException e) {
//                logger.error("填充错误", e);
                throw new Exception("DES解密错误:填充错误", e);
            } catch (IllegalBlockSizeException e) {
//                logger.error("非法数据块", e);
                throw new Exception("DES解密错误:非法数据块", e);
            } catch (BadPaddingException e) {
//                logger.error("错误的填充", e);
                throw new Exception("DES解密错误:错误的填充", e);
            }
        }
    }

//    public static class RSA {
//        //密钥对
//        private KeyPair keyPair = null;
//
//        /**
//         * 初始化密钥对
//         */
//        public RSAUtil(){
//            try {
//                this.keyPair = this.generateKeyPair();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        /**
//         * 生成密钥对
//         * @return KeyPair
//         * @throws Exception
//         */
//        private KeyPair generateKeyPair() throws Exception {
//            try {
//                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",new org.bouncycastle.jce.provider.BouncyCastleProvider());
//                //这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
//                final int KEY_SIZE = 1024;
//                keyPairGen.initialize(KEY_SIZE, new SecureRandom());
//                KeyPair keyPair = keyPairGen.genKeyPair();
//                return keyPair;
//            } catch (Exception e) {
//                throw new Exception(e.getMessage());
//            }
//
//        }
//
//        /**
//         * 生成公钥
//         * @param modulus
//         * @param publicExponent
//         * @return RSAPublicKey
//         * @throws Exception
//         */
//        private RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {
//
//            KeyFactory keyFac = null;
//            try {
//                keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
//            } catch (NoSuchAlgorithmException ex) {
//                throw new Exception(ex.getMessage());
//            }
//            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
//            try {
//                return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
//            } catch (InvalidKeySpecException ex) {
//                throw new Exception(ex.getMessage());
//            }
//
//        }
//
//        /**
//         * 生成私钥
//         * @param modulus
//         * @param privateExponent
//         * @return RSAPrivateKey
//         * @throws Exception
//         */
//        private RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
//            KeyFactory keyFac = null;
//            try {
//                keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
//            } catch (NoSuchAlgorithmException ex) {
//                throw new Exception(ex.getMessage());
//            }
//            RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
//            try {
//                return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
//            } catch (InvalidKeySpecException ex) {
//                throw new Exception(ex.getMessage());
//            }
//        }
//
//        /**
//         * 加密
//         * @param key 加密的密钥
//         * @param data 待加密的明文数据
//         * @return 加密后的数据
//         * @throws Exception
//         */
//        public byte[] encrypt(Key key, byte[] data) throws Exception {
//            try {
//                Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
//                cipher.init(Cipher.ENCRYPT_MODE, key);
//                //获得加密块大小，如:加密前数据为128个byte，而key_size=1024 加密块大小为127 byte,加密后为128个byte;
//                //因此共有2个加密块，第一个127 byte第二个为1个byte
//                int blockSize = cipher.getBlockSize();
//                int outputSize = cipher.getOutputSize(data.length);//获得加密块加密后块大小
//                int leavedSize = data.length % blockSize;
//                int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
//                byte[] raw = new byte[outputSize * blocksSize];
//                int i = 0;
//                while (data.length - i * blockSize > 0) {
//                    if (data.length - i * blockSize > blockSize)
//                        cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
//                    else
//                        cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
//                    //这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到ByteArrayOutputStream中
//                    //，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了OutputSize所以只好用dofinal方法。
//                    i++;
//                }
//                return raw;
//            } catch (Exception e) {
//                throw new Exception(e.getMessage());
//            }
//        }
//
//        /**
//         * 解密
//         * @param key 解密的密钥
//         * @param raw 已经加密的数据
//         * @return 解密后的明文
//         * @throws Exception
//         */
//        public byte[] decrypt(Key key, byte[] raw) throws Exception {
//            try {
//                Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
//                cipher.init(cipher.DECRYPT_MODE, key);
//                int blockSize = cipher.getBlockSize();
//                ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
//                int j = 0;
//                while (raw.length - j * blockSize > 0) {
//                    bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
//                    j++;
//                }
//                return bout.toByteArray();
//            } catch (Exception e) {
//                throw new Exception(e.getMessage());
//            }
//        }
//
//        /**
//         * 返回公钥
//         * @return
//         * @throws Exception
//         */
//        public RSAPublicKey getRSAPublicKey() throws Exception{
//
//            //获取公钥
//            RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
//            //获取公钥系数(字节数组形式)
//            byte[] pubModBytes = pubKey.getModulus().toByteArray();
//            //返回公钥公用指数(字节数组形式)
//            byte[] pubPubExpBytes = pubKey.getPublicExponent().toByteArray();
//            //生成公钥
//            RSAPublicKey recoveryPubKey = this.generateRSAPublicKey(pubModBytes,pubPubExpBytes);
//            return recoveryPubKey;
//        }
//
//        /**
//         * 获取私钥
//         * @return
//         * @throws Exception
//         */
//        public RSAPrivateKey getRSAPrivateKey() throws Exception{
//
//            //获取私钥
//            RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();
//            //返回私钥系数(字节数组形式)
//            byte[] priModBytes = priKey.getModulus().toByteArray();
//            //返回私钥专用指数(字节数组形式)
//            byte[] priPriExpBytes = priKey.getPrivateExponent().toByteArray();
//            //生成私钥
//            RSAPrivateKey recoveryPriKey = this.generateRSAPrivateKey(priModBytes,priPriExpBytes);
//            return recoveryPriKey;
//        }
//
//    }

}
