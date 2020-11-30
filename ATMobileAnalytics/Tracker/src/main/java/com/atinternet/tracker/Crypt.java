package com.atinternet.tracker;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

class Crypt {

    /***
     * Transformation type
     */
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    /***
     * Security provider
     */
    private static final String PROVIDER = "AndroidKeyStore";

    /***
     * Alias
     */
    private static final String ALIAS = "com.atinternet.encryption.key";

    /***
     * Constant Initialization vector length
     */
    private static final int IV_LENGTH = 12;

    /***
     * Constant GCM authentication tag length
     */
    private static final int GCM_TAG_LENGTH = 128;

    private KeyStore keyStore;

    private static ATInternet.EncryptionMode encryptionMode = ATInternet.EncryptionMode.none;

    static ATInternet.EncryptionMode getEncryptionMode() {
        return Crypt.encryptionMode;
    }

    static void setEncryptionMode(ATInternet.EncryptionMode encryptionMode) {
        Crypt.encryptionMode = encryptionMode;
    }

    /***
     * Encrypt and base64 encode data
     * @param data String
     * @return String
     */
    String encrypt(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }

        if (encryptionMode == ATInternet.EncryptionMode.none) {
            return data;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(Cipher.ENCRYPT_MODE, getKey());

                byte[] iv = cipher.getIV();
                byte[] cipherText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

                byte[] encrypted = new byte[iv.length + cipherText.length];
                System.arraycopy(iv, 0, encrypted, 0, iv.length);
                System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);

                return Base64.encodeToString(encrypted, Base64.DEFAULT);
            } catch (Exception e) {
                Log.e(ATInternet.TAG, e.toString());
            }
        }

        /// if force, we don't use original data
        return encryptionMode == ATInternet.EncryptionMode.ifCompatible ? data : null;
    }

    String decrypt(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                byte[] encryptedData = Base64.decode(data, Base64.DEFAULT);

                cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(GCM_TAG_LENGTH, encryptedData, 0, IV_LENGTH));
                return new String(cipher.doFinal(encryptedData, IV_LENGTH, encryptedData.length - IV_LENGTH), StandardCharsets.UTF_8);
            } catch (Exception e) {
                Log.e(ATInternet.TAG, e.toString());
            }
        }
        return data;
    }

    /***
     * Get secret key from store or create, store and return it
     * @return SecretKey
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey getKey() {
        try {
            if (keyStore == null) {
                keyStore = KeyStore.getInstance(PROVIDER);
                keyStore.load(null);
            }

            if (keyStore.containsAlias(ALIAS)) {
                KeyStore.Entry keyEntry = keyStore.getEntry(ALIAS, null);
                if (keyEntry instanceof KeyStore.SecretKeyEntry) {
                    return ((KeyStore.SecretKeyEntry) keyEntry).getSecretKey();
                }
            }

            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER);
            keyGenerator.init(new KeyGenParameterSpec.Builder(ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());

            return keyGenerator.generateKey();
        } catch (Exception e) {
            Log.e(ATInternet.TAG, e.toString());
        }
        return null;
    }
}
