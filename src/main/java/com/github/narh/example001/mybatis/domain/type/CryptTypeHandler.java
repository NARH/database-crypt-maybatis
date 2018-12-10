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

package com.github.narh.example001.mybatis.domain.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.stereotype.Component;

import com.github.narh.example001.mybatis.ApplicationConfigRegistory;
import com.github.narh.example001.mybatis.util.CryptUtils;

import lombok.Setter;

/**
 * @author narita
 *
 */
@Component @MappedJdbcTypes(JdbcType.BINARY) @Setter
public class CryptTypeHandler extends BaseTypeHandler<String>{

  /* (非 Javadoc)
   * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, java.lang.String)
   */
  @Override
  public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
    try {
      return (null == rs.getBytes(columnName)) ? null
          : new String(CryptUtils.decrypt(rs.getBytes(columnName), getPassphrase()));
    }
    catch(IllegalArgumentException e) {
      throw new SQLException(e);
    }
  }

  /* (非 Javadoc)
   * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, int)
   */
  @Override
  public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    try {
      return (null == rs.getBytes(columnIndex)) ? null
          : new String(CryptUtils.decrypt(rs.getBytes(columnIndex), getPassphrase()));
    }
    catch(IllegalArgumentException e) {
      throw new SQLException(e);
    }
  }

  /* (非 Javadoc)
   * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement, int)
   */
  @Override
  public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    try {
      return (null == cs.getBytes(columnIndex)) ? null
          : new String(CryptUtils.decrypt(cs.getBytes(columnIndex), getPassphrase()));
    }
    catch(IllegalArgumentException e) {
      throw new SQLException(e);
    }
  }

  /* (非 Javadoc)
   * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
   */
  @Override
  public void setNonNullParameter(PreparedStatement ps, int index, String parameter, JdbcType jdbcType) throws SQLException {
    try {
      ps.setBytes(index, CryptUtils.encrypt(parameter, getPassphrase()));
    }
    catch(IllegalArgumentException e) {
      throw new SQLException(e);
    }
  }

  private String getPassphrase() {
    return ApplicationConfigRegistory.getInstance().getConfig().getCrypt().getPassphrase();
  }
}
