package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_전달된다() {
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        //when
        String email = "test@naver.com";
        certificationService.send(email, 1L, "aaaaaaaa-aaa-aaaaaa-aaaaaaaa");
        //then
        assertThat(fakeMailSender.email).isEqualTo(email);
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaaa-aaa-aaaaaa-aaaaaaaa");

    }
}