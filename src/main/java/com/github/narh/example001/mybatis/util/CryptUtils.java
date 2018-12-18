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

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

import com.github.narh.example001.mybatis.ApplicationContextRegistory;
import com.github.narh.example001.mybatis.util.crypt.CryptType;
import com.github.narh.example001.mybatis.util.crypt.Decrypter;
import com.github.narh.example001.mybatis.util.crypt.Encrypter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author narita
 *
 */
@Slf4j
public class CryptUtils {

  static MessageDigest messageDigest;

  public static byte[] encrypt(final String message) {
    Charset charset = ApplicationContextRegistory.getInstance().getConfig().getCrypt().getCharset();
    return (StringUtils.isNotEmpty(message))
      ? getEncrypter(message.getBytes(charset)).execute()
      : message.getBytes(charset);
  }

  public static String decrypt(final byte[] message) {
    Charset charset = ApplicationContextRegistory.getInstance().getConfig().getCrypt().getCharset();
    return (null != message && 0 < message.length)
      ? new String(getDecrypt(message).execute(), charset)
      : new String(message, charset);
  }

  public static Encrypter getEncrypter(final byte[] src) {
    Encrypter encrypter = new Encrypter(src);
    if(log.isTraceEnabled()) log.trace("encrypt order:{}", ApplicationContextRegistory.getInstance().getConfig().getCrypt());
    ApplicationContextRegistory.getInstance().getConfig().getCrypt().getOrder()
      .entrySet().stream().sorted(java.util.Map.Entry.comparingByKey())
      .forEach(c->{
        try {
          encrypter.add(CryptType.valueOf(c.getValue()).getCommand(
              ApplicationContextRegistory.getInstance().getConfig().getCrypt().getPassphrase()
            , ApplicationContextRegistory.getInstance().getConfig().getCrypt().getIv()));
        }
        catch (
            InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
            | NoSuchMethodException | SecurityException e) {
          throw new RuntimeException(e);
    }});
    return encrypter;
  }

  public static Decrypter getDecrypt(final byte[] src) {
    Decrypter decrypter = new Decrypter(src);
    if(log.isTraceEnabled()) log.trace("decrypt order:{}", ApplicationContextRegistory.getInstance().getConfig().getCrypt());
    ApplicationContextRegistory.getInstance().getConfig().getCrypt().getOrder()
      .entrySet().stream().sorted(Collections.reverseOrder(java.util.Map.Entry.comparingByKey()))
      .forEach(c->{
        try {
          decrypter.add(CryptType.valueOf(c.getValue()).getCommand(
              ApplicationContextRegistory.getInstance().getConfig().getCrypt().getPassphrase()
            , ApplicationContextRegistory.getInstance().getConfig().getCrypt().getIv()));
        }
        catch (
          InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
          | NoSuchMethodException | SecurityException e) {
        throw new RuntimeException(e);
      }
    });
    return decrypter;
  }
}
