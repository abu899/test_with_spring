package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void UserCreate_객체로_User_를_생성_할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("test@naver.com")
                .address("Busan")
                .nickname("test")
                .build();

        //when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaa-aaaaaa-aaaaaaaa"));

        //then
        assertThat(user.getNickname()).isEqualTo("test");
        assertThat(user.getAddress()).isEqualTo("Busan");
        assertThat(user.getEmail()).isEqualTo("test@naver.com");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaa-aaaaaa-aaaaaaaa");
    }

    @Test
    public void UserUpdate_객체로_User_를_업데이트_할_수_있다() {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("test2")
                .address("Pangyo")
                .build();

        User user = User.builder()
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .build();

        //when
        user = user.update(userUpdate);

        //then
        assertThat(user.getNickname()).isEqualTo("test2");
        assertThat(user.getAddress()).isEqualTo("Pangyo");
        assertThat(user.getEmail()).isEqualTo("test@naver.com");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaa-aaaaaa-aaaaaaaa");
    }

    @Test
    public void 로그인_할_수_있고_마지막_로그인_시간이_변경() {
        //given
        User user = User.builder()
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .build();

        //when
        user = user.login(new TestClockHolder(100L));

        //then
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void 인증_코드로_계정을_활성화() {
        //given
        User user = User.builder()
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .build();

        //when
        user = user.certificate("aaaaaaaa-aaa-aaaaaa-aaaaaaaa");

        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 잘못된_인증_코드로는_에러를_호출() {
        //given
        User user = User.builder()
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .build();

        //when
        //then
        assertThatThrownBy(() -> user.certificate("bbbbbbbb-bbb-bbbbbb-bbbbbbbb"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}