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

package com.github.narh.example001.mybatis.util.crypt;

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

import org.apache.commons.codec.binary.Hex;

import lombok.extern.slf4j.Slf4j;

/**
 * @author narita
 *
 */
@Slf4j
public class AES256CryptCommand extends AbstractCryptCommand implements CryptCommand {

  static final String HASH_ALGORITHM = "SHA-256";
  static final String KEY_ENCODING   = "AES";
  static final String ENCODING_MODE  = "AES/CBC/PKCS5PADDING";
  static final int PASSPHRASE_LENGTH = 32;
  static final int IV_LENGTH         = 16;

  MessageDigest messageDigest;

  final boolean USE_MESSAGE_DIGEST;

  public AES256CryptCommand(final String passphrase, final String iv) {
    this(passphrase, iv, false);
  }

  public AES256CryptCommand(final String passphrase, final String iv
      , final boolean useMessageDigest) {
    USE_MESSAGE_DIGEST = useMessageDigest;
    setPassphrase(passphrase);
    setInitializationVector(iv);
  }

  /* (非 Javadoc)
   * @see com.github.narh.example001.mybatis.util.crypt.CryptAdapter#encrypt(byte[], java.lang.String)
   */
  @Override
  public byte[] encrypt(final byte[] src) {
    if(log.isDebugEnabled()) log.debug("AES256 do encrypt... {}", Hex.encodeHexString(src));
    return (null == src || 0 == src.length
        || null == getPassphrase() || 0 == getPassphrase().length)
        ? src : getGraph(src, Cipher.ENCRYPT_MODE);
  }

  /* (非 Javadoc)
   * @see com.github.narh.example001.mybatis.util.crypt.CryptAdapter#decrypt(byte[], java.lang.String)
   */
  @Override
  public byte[] decrypt(final byte[] src) {
    if(log.isDebugEnabled()) log.debug("AES256 do decrypt... {}", Hex.encodeHex(src));
    return (null == src || 0 == src.length
        || null == getPassphrase() || 0 == getPassphrase().length
        || null == getInitializationVector() || 0 == getInitializationVector().length)
        ? src : getGraph(src, Cipher.DECRYPT_MODE);
  }

  private MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
    if(null == messageDigest) messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
    return messageDigest;
  }

  private Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
    return Cipher.getInstance(ENCODING_MODE);
  }

  private byte[] getGraph(final byte[] src, final int mode) {
    try {
      Cipher cipher                   = getCipher();
      cipher.init(mode, getSecretKeySpec(), getIvParameterSpec());
      if(log.isDebugEnabled())
        log.debug("===> AES256 passphrase:{}, iv:{}", new String(getPassphrase()), new String(getInitializationVector()));
      return cipher.doFinal(src);
    }
    catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
        | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private byte[] getKey() throws NoSuchAlgorithmException {
    if(null == getPassphrase()) throw new IllegalArgumentException("Passphrase is null");
    byte[] key = getMessageDigest().digest(getPassphrase());
    return Arrays.copyOf(key, PASSPHRASE_LENGTH);
  }

  private SecretKeySpec getSecretKeySpec() throws NoSuchAlgorithmException {
    byte[] key = (USE_MESSAGE_DIGEST)
        ? getKey() : Arrays.copyOf(getPassphrase(), PASSPHRASE_LENGTH);
    return new SecretKeySpec(key, KEY_ENCODING);
  }

  private IvParameterSpec getIvParameterSpec() {
    return new IvParameterSpec(Arrays.copyOf(getInitializationVector(), IV_LENGTH));
  }
}
