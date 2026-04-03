package com.example.CESIZen.mapper;

import com.example.CESIZen.dto.user.UserDtoRequest;
import com.example.CESIZen.dto.user.UserDtoResponse;
import com.example.CESIZen.model.user.User;

public class UserMapper {
    public static UserDtoResponse toDto(User user){
        if(user != null){
            UserDtoResponse userDtoResponse = new UserDtoResponse();
            userDtoResponse.setId(user.getId());
            userDtoResponse.setUsername(user.getUsername());
            userDtoResponse.setEmail(user.getEmail());
            userDtoResponse.setPhone(user.getPhone());
            userDtoResponse.setRole(user.getRole());
            return userDtoResponse;
        }
        return null;
    }

    public static User mapToUser(UserDtoRequest userDtoRequest){
        if (userDtoRequest != null){
            User user = new User();
            user.setUsername(userDtoRequest.getUsername());
            user.setEmail(userDtoRequest.getEmail());
            user.setPassword(userDtoRequest.getPhone());
            user.setPassword(userDtoRequest.getPassword());
            user.setRole(userDtoRequest.getRole());
            return user;
        }
        return null;
    }
}
