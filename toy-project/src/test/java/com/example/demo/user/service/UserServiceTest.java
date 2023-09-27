package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        userService = UserServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(100L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaa-aaaaaa-aaaaaaaa"))
                .certificationService(new CertificationService(fakeMailSender))
                .build();

        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("test2@naver.com")
                .nickname("test2")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail이_ACTIVE_유저를_찾아온다() {
        //given
        String email = "test@naver.com";

        //when
        User result = userService.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    void getByEmail이_PENDING_유저는_찾아올_수_없다() {
        //given
        String email = "test2@naver.com";

        //when
        //then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById가_ACTIVE_유저를_찾아온다() {
        //given
        //when
        User result = userService.getById(1L);

        //then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    void getById는_PENDING_유저는_찾아올_수_없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            User result = userService.getById(2L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용해_유저를_생성() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("test@naver.com")
                .address("Busan")
                .nickname("test")
                .build();

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaa-aaaaaa-aaaaaaaa");
    }

    @Test
    void userUpdateDto를_이용해_유저를_수정() {
        //given
        UserUpdate userCreateDto = UserUpdate.builder()
                .address("Ilsan")
                .nickname("test-update")
                .build();

        //when
        userService.update(1L, userCreateDto);

        //then
        User result = userService.getById(1L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("Ilsan");
        assertThat(result.getNickname()).isEqualTo("test-update");
        assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaa-aaaaaa-aaaaaaaa");
    }

    @Test
    void 로그인을_하게되면_마지막_로그인시간_변경() {
        //given
        //when
        userService.login(1L);

        //then
        User result = userService.getById(1L);
        assertThat(result.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void PENDING_상태_사용자는_인증코드로_ACTIVE() {
        //given
        //when
        userService.verifyEmail(2L, "aaaaaaaa-aaa-aaaaaa-aaaaaab");

        //then
        User result = userService.getById(2L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태_사용자가_잘못된_인증코드_사용시_에러() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2L, "aaaaaaaa-aaa-aaaaaa-aaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}