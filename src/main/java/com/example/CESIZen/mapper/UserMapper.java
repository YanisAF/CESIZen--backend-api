package com.example.CESIZen.mapper;

import com.example.CESIZen.dto.user.UserDtoRequest;
import com.example.CESIZen.dto.user.UserDtoResponse;
import com.example.CESIZen.model.user.User;

public class UserMapper {
    public static UserDtoResponse toDto(User user){
        if(user != null){
            UserDtoResponse userDtoResponse = new UserDtoResponse();
            userDtoResponse.setId(user.getId());
            userDtoResponse.setUserName(user.getUserName());
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
            user.setUserName(userDtoRequest.getUserName());
            user.setEmail(userDtoRequest.getEmail());
            user.setPassword(userDtoRequest.getPhone());
            user.setPassword(userDtoRequest.getPassword());
            user.setRole(userDtoRequest.getRole());
            return user;
        }
        return null;
    }
}
