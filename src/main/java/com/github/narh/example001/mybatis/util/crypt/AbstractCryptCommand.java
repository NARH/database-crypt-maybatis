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

/**
 * @author narita
 *
 */
public abstract class AbstractCryptCommand implements CryptCommand {

  private String passphrase;

  private String iv;

  /* (非 Javadoc)
   * @see com.github.narh.example001.mybatis.util.crypt.CryptCommand#encrypt(byte[], java.lang.String)
   */
  @Override
  abstract public byte[] encrypt(final byte[] src);

  /* (非 Javadoc)
   * @see com.github.narh.example001.mybatis.util.crypt.CryptCommand#decrypt(byte[], java.lang.String)
   */
  @Override
  abstract public byte[] decrypt(final byte[] src);

  protected void setPassphrase(String passphrase) {
    this.passphrase = passphrase;
  }

  protected void setInitializationVector(String iv) {
    this.iv = iv;
  }

  protected byte[] getPassphrase() {
    return passphrase.getBytes();
  }

  protected byte[] getInitializationVector() {
    return iv.getBytes();
  }
}
