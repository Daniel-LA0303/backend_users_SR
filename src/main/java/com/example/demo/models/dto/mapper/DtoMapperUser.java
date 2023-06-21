package com.example.demo.models.dto.mapper;

import com.example.demo.models.dto.UserDto;
import com.example.demo.models.entities.User;

public class DtoMapperUser {

    //private static DtoMapperUser mapper;

    private User user;

    private DtoMapperUser() {

    }

    public static DtoMapperUser builder() {

        //mapper = new DtoMapperUser();
        return new DtoMapperUser();
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;

        return this;
    }

    public UserDto build(){

        if (user == null) {
            throw new RuntimeException("User is null");
        }

        UserDto userDto = new UserDto(this.user.getId(), this.user.getUsername(), this.user.getEmail());
        return userDto;
    }
}
