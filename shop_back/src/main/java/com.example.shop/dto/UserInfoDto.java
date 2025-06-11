package com.example.shop.dto;

import com.example.shop.entity.User;
import lombok.Getter;

@Getter
public class UserInfoDto {
    private Long id;
    private String email;
    private String username;

    public UserInfoDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
    }
}