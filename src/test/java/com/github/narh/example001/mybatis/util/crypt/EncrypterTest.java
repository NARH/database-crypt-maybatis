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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author narita
 *
 */
@Slf4j
public class EncrypterTest {

  @Test
  public void testAES256ToXOR() throws Exception {
    String src = "テスト文字列";
    String passphrase = "hogehogeFugafugaFooBar123";

    Encrypter encrypter = new Encrypter();
    AES256CryptCommand aes256CryptCommand = new AES256CryptCommand();
    XORCryptCommand xorCryptCommand = new XORCryptCommand();

    log.info("<=== {}", src);
    encrypter.add(aes256CryptCommand);
    encrypter.add(xorCryptCommand);
    byte[] encryptGraph = encrypter.execute(src.getBytes(StandardCharsets.UTF_8), passphrase);

    Decrypter decrypter = new Decrypter();
    decrypter.add(xorCryptCommand);
    decrypter.add(aes256CryptCommand);
    byte[] decryptGraph = decrypter.execute(encryptGraph, passphrase);
    log.info("===> {}",new String(decryptGraph, StandardCharsets.UTF_8));
    assertThat("元の文字列であること", new String(decryptGraph, StandardCharsets.UTF_8), is(src));
  }
}
