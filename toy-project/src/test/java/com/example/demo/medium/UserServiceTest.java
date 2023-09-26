package com.example.demo.medium;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    UserService userService;
    @MockBean
    JavaMailSender javaMailSender;

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

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userService.create(userCreate);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        // CertificationCode 는 랜덤 생성이라 테스트할 방법이 없음
//        assertThat(result.getCertificationCode()).isEqualTo("aaaaa-aaa-aaaaaaaa");
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
        // CertificationCode 는 랜덤 생성이라 테스트할 방법이 없음
//        assertThat(result.getCertificationCode()).isEqualTo("aaaaa-aaa-aaaaaaaa");
    }

    @Test
    void 로그인을_하게되면_마지막_로그인시간_변경() {
        //given
        //when
        userService.login(1L);

        //then
        User result = userService.getById(1L);
        // 마지막 로그인 시간을 정확하게 비교할 수 없으므로 null 이 아닌지만 확인
        assertThat(result.getLastLoginAt()).isNotNull(); // FIXME
    }

    @Test
    void PENDING_상태_사용자는_인증코드로_ACTIVE() {
        //given
        //when
        userService.verifyEmail(2L, "aaaaaaaa-aaa-aaaaaa-aaaaaaab");

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