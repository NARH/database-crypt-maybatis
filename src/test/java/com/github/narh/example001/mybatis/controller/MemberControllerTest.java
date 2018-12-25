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

package com.github.narh.example001.mybatis.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.github.narh.example001.mybatis.domain.entity.MemberEntity;
import com.github.narh.example001.mybatis.domain.mapper.MemberMapper;

/**
 * @author narita
 *
 */
@RunWith(SpringRunner.class) @SpringBootTest
public class MemberControllerTest {

  @MockBean
  private MemberMapper memberMapper;

  @Autowired
  private MemberController controller;

  @SuppressWarnings("unchecked")
  @Test
  public void testGetMethod() throws Exception {
    /** Mock data の作成 */
    List<MemberEntity> members = new ArrayList<>();
    int i = 0;
    while(i < 5) {
      members.add(new MemberEntity());
      members.get(i).setId(Integer.toString(i));
      members.get(i).setName("Foo");
      members.get(i).setKana("foo");
      members.get(i).setPostalCode("000-0000");
      members.get(i).setAddress("Foo Bar 123");
      i++;
    }
    /** Mock の登録 */
    given(this.memberMapper.findAll()).willReturn(members);

    Model model = new ConcurrentModel();
    String templateName = this.controller.showIndexPage(model);
    assertThat("テンプレート名がindexであること", templateName, is("index"));
    assertThat("リストの件数が5件であること", ((List<MemberEntity>)model.asMap().get("list")).size(),is(5));
  }
}
