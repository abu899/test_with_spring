package com.example.demo.mock;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {
    private Long id = 0L;
    private final List<User> users = new ArrayList<>();

    @Override
    public User getById(long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }

    @Override
    public Optional<User> findById(long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return users.stream()
                .filter(u -> u.getId() == id && u.getStatus().equals(userStatus))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return users.stream()
                .filter(u -> u.getEmail().equals(email) && u.getStatus().equals(userStatus))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if( user.getId() == null || user.getId() == 0) {
            User newUser = User.builder()
                    .id(id++)
                    .email("test@naver.com")
                    .nickname("test")
                    .address("Seoul")
                    .status(UserStatus.PENDING)
                    .certificationCode("aaaaaaaa-aaa-aaaaaa-aaaaaaaa")
                    .build();
            users.add(newUser);
            return newUser;
        } else {
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
            return user;
        }
    }
}
