/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.helper;

import android.util.Base64;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class GaanaDecrypt {

    private static final String key = "g@1n!(f1#r.0$)&%";
    private static final String initVector = "asd!@#!@#@!12312";

    public GaanaDecrypt() {
    }

    public String decrypt(String encrypted) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            //int blockSize = cipher.getBlockSize();
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(initVector.getBytes("UTF-8"), 16));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
