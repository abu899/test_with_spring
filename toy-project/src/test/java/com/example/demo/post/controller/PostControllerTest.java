package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostControllerTest {

    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaa-aaaaaa-aaaaaaaa"))
                .build();

        User user = testContainer.userRepository.save(User.builder()
                .id(0L)
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .build());

        testContainer.postRepository.save(Post.builder()
                .id(0L)
                .writer(user)
                .content("helloworld")
                .createdAt(100L)
                .build());

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(0L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(0L);
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getWriter().getId()).isEqualTo(0L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
    }

    @Test
    void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaa-aaaaaa-aaaaaaaa"))
                .build();

        User user = testContainer.userRepository.save(User.builder()
                .id(0L)
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .build());

        testContainer.postRepository.save(Post.builder()
                .id(0L)
                .writer(user)
                .content("helloworld")
                .build());

        // when
        // then
        assertThatThrownBy(() -> testContainer.postController.getPostById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .uuidHolder(new TestUuidHolder("aaaaaaaa-aaa-aaaaaa-aaaaaaaa"))
                .build();

        User user = testContainer.userRepository.save(User.builder()
                .id(0L)
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                .build());

        testContainer.postRepository.save(Post.builder()
                .id(0L)
                .writer(user)
                .content("helloworld")
                .createdAt(100L)
                .build());

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(0L,
                PostUpdate.builder()
                        .content("new helloworld")
                        .build());

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getContent()).isEqualTo("new helloworld");
        assertThat(result.getBody().getWriter().getId()).isEqualTo(0L);
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
    }
}