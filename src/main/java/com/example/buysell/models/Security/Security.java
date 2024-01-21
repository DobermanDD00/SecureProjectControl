package com.example.buysell.models.Security;

import javax.crypto.Cipher;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Security {
    public static void main(String[] args) {

    }


    public static byte[] cipherAes(byte[] data, SecretKey key, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //mod = Cipher.DECRYPT_MODE
        //mod = Cipher.ENCRYPT_MODE
        if (data == null) return null;

        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        javax.crypto.Cipher encrypt = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
        encrypt.init(mode, key, iv);
        return encrypt.doFinal(data);

    }

    public static SecretKey generatedAesKey() throws NoSuchAlgorithmException {

        return KeyGenerator.getInstance("AES").generateKey();


    }

    public static byte[] encodedAnyKey(Key key) {
        return key.getEncoded();
    }

    public static SecretKey decodedKeyAes(byte[] bytesOfKey) {
        return new SecretKeySpec(bytesOfKey, 0, bytesOfKey.length, "AES");
    }


    public static byte[] cipherRSAEncrypt(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //mod = Cipher.DECRYPT_MODE
        //mod = Cipher.ENCRYPT_MODE

        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);


    }

    public static byte[] cipherRSADecrypt(byte[] data, PrivateKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //mod = Cipher.DECRYPT_MODE
        //mod = Cipher.ENCRYPT_MODE


        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);


    }

    public static KeyPair generatedRsaKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(4096);
        return keyPairGenerator.generateKeyPair();
    }

    public static RSAPublicKey decodedKeyPublicRsa(byte[] bytesOfKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec spec1 = new X509EncodedKeySpec(bytesOfKey);
        KeyFactory kf1 = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf1.generatePublic(spec1);
        return pubKey;


    }

    public static RSAPrivateKey decodedKeyPrivateRsa(byte[] bytesOfKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytesOfKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey privKey = (RSAPrivateKey) kf.generatePrivate(spec);
        return privKey;


    }


    public static boolean isCorrectPairKeys(byte[] bytesKeyPublic, byte[] bytesKeyPrivate) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if (bytesKeyPublic == null || bytesKeyPrivate == null)
            return false;
        RSAPublicKey keyPub = decodedKeyPublicRsa(bytesKeyPublic);
        RSAPrivateKey keyPri = decodedKeyPrivateRsa(bytesKeyPrivate);
        return isCorrectPairKeys(keyPub, keyPri);

    }

    public static boolean isCorrectPairKeys(PublicKey publicKey, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] plainText = new byte[0];

        plainText = generateRandomBytes(100);

        byte[] encryptedText = cipherRSAEncrypt(plainText, publicKey);
        byte[] decryptedText = cipherRSADecrypt(encryptedText, privateKey);

        return Arrays.equals(plainText, decryptedText);


    }


    public static byte[] generateRandomBytes(int numBytes) throws NoSuchAlgorithmException {
        byte[] salt;
        salt = new byte[numBytes];
        SecureRandom randByte = new SecureRandom(SecureRandom.getSeed(numBytes));
        SecureRandom.getInstanceStrong().nextBytes(salt);

        return salt;
    }

    public static byte[] generateHashSha256(byte[] first, byte[] second) throws NoSuchAlgorithmException {

        if (first == null || second == null) throw new NullPointerException("first == null of secornd == null");
        byte[] saltHash;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(first);
        messageDigest.update(second);
        saltHash = messageDigest.digest();


        return saltHash;
    }


}
