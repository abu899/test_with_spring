package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileResponse {

    private Long id;
    private String email;
    private String nickname;
    private String address;
    private UserStatus status;
    private Long lastLoginAt;

    public static MyProfileResponse from(User user) {
        MyProfileResponse myProfileResponse = new MyProfileResponse();
        myProfileResponse.setId(user.getId());
        myProfileResponse.setEmail(user.getEmail());
        myProfileResponse.setNickname(user.getNickname());
        myProfileResponse.setAddress(user.getAddress());
        myProfileResponse.setStatus(user.getStatus());
        myProfileResponse.setLastLoginAt(user.getLastLoginAt());
        return myProfileResponse;
    }
}
