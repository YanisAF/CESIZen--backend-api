package com.example.CESIZen.mapper;

import com.example.CESIZen.dto.login.LoginDtoRequest;
import com.example.CESIZen.dto.user.UserDtoRequest;

public class LoginMapper {
    public static LoginDtoRequest toDtoLogin(UserDtoRequest userDtoRequest){
        if (userDtoRequest != null){
            LoginDtoRequest loginDtoRequest = new LoginDtoRequest();
            loginDtoRequest.setIdentifier(userDtoRequest.getEmail());
            loginDtoRequest.setPassword(userDtoRequest.getPassword());
            return loginDtoRequest;
        }
        return null;
    }
}
