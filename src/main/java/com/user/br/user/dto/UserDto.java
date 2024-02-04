package com.user.br.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(@JsonView(UserView.RegistrationPost.class) String userName,
                      @JsonView(UserView.RegistrationPost.class) String email,
                      @JsonView({UserView.PasswordPut.class, UserView.RegistrationPost.class}) String password,
                      @JsonView(UserView.PasswordPut.class) String oldPassword,
                      @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class}) String fullName,
                      @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class}) String phoneNumber,
                      @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class}) String cpf,
                      @JsonView(UserView.ImagePut.class) String imageUrl,
                      UUID userId) {

    public interface UserView{
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }

}
