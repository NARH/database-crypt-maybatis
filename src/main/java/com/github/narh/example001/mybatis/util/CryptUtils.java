/*
 * Copyright (c) 2018, NARH https://github.com/NARH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.narh.example001.mybatis.util;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

/**
 * @author narita
 *
 */
public class CryptUtils {

  static MessageDigest messageDigest;

  public static byte[] encrypt(final String message, final String passphrase) {
    return (StringUtils.isNotEmpty(passphrase))
      ? getGraph(message.getBytes(), passphrase, Cipher.ENCRYPT_MODE)
      : message.getBytes();
  }

  public static String decrypt(final byte[] message, final Charset charset, final String passphrase) {
    return (StringUtils.isNotEmpty(passphrase))
      ? new String(getGraph(message, passphrase, Cipher.DECRYPT_MODE), charset)
      : new String(message, charset);
  }

  private static MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    if(null == messageDigest) messageDigest = MessageDigest.getInstance("SHA-256");
    return messageDigest;
  }

  private static Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
    return Cipher.getInstance("AES/CBC/PKCS5PADDING");
  }

  private static byte[] getGraph(final byte[] message, final String passphrase, final int mode) {
    try {
      byte[] key = getKey(passphrase);
      SecretKeySpec secretKeySpec = getSecretKeySpec(key);
      IvParameterSpec ivParameterSpec = getIvParameterSpec(key);
      Cipher cipher = getCipher();
      cipher.init(mode, secretKeySpec, ivParameterSpec);
      return cipher.doFinal(message);
    }
    catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private static byte[] getKey(final String passphrase) throws NoSuchAlgorithmException {
    if(null == passphrase) throw new IllegalArgumentException("Passphrase is null");
    byte[] key = getMessageDigest().digest(passphrase.getBytes());
    return Arrays.copyOf(key, 32);
  }

  private static SecretKeySpec getSecretKeySpec(final byte[] key) {
    return new SecretKeySpec(key, "AES");
  }

  private static IvParameterSpec getIvParameterSpec(final byte[] key) {
    return new IvParameterSpec(Arrays.copyOf(key, 16));
  }
}
