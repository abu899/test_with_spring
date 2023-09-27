package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.UserAuthenticationService;
import com.example.demo.user.controller.port.UserCreateService;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserUpdateService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final UserReadService userReadService;
    public final UserCreateService userCreateService;
    public final UserUpdateService userUpdateService;
    public final UserAuthenticationService userAuthenticationService;
    public final PostService postService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostController postController;
    public final PostCreateController postCreateController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .build();
        this.certificationService = new CertificationService(mailSender);
        UserServiceImpl userService = UserServiceImpl.builder()
                .userRepository(userRepository)
                .clockHolder(clockHolder)
                .uuidHolder(uuidHolder)
                .certificationService(certificationService)
                .build();
        this.userUpdateService = userService;
        this.userReadService = userService;
        this.userCreateService = userService;
        this.userAuthenticationService = userService;
        this.userController = UserController.builder()
                .userReadService(userReadService)
                .userCreateService(userCreateService)
                .userUpdateService(userUpdateService)
                .userAuthenticationService(userAuthenticationService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userCreateService(userCreateService)
                .build();
        this.postController = PostController.builder()
                .postService(postService)
                .build();
        this.postCreateController = PostCreateController.builder()
                .postController(postController)
                .postService(postService)
                .build();
    }
}