package com.user.br.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.user.br.user.validation.UserNameConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(@NotBlank(groups = UserView.RegistrationPost.class)
                  @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
                  @UserNameConstraint(groups = UserView.RegistrationPost.class)
                  @JsonView(UserView.RegistrationPost.class) String userName,
                  @NotBlank(groups = UserView.RegistrationPost.class)
                  @Email(groups = UserView.RegistrationPost.class)
                  @JsonView(UserView.RegistrationPost.class) String email,
                  @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
                  @Size(min = 6, max = 20, groups = {UserView.PasswordPut.class, UserView.RegistrationPost.class})
                  @JsonView({UserView.PasswordPut.class, UserView.RegistrationPost.class}) String password,
                  @NotBlank(groups = UserView.PasswordPut.class)
                  @Size(min = 6, max = 20, groups = UserView.PasswordPut.class)
                  @JsonView(UserView.PasswordPut.class) String oldPassword,
                  @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class}) String fullName,
                  @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class}) String phoneNumber,
                  @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class}) String cpf,
                  @NotBlank(groups = UserView.ImagePut.class)
                  @JsonView(UserView.ImagePut.class) String imageUrl,
                  UUID userId) {

    public interface UserView{
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }

}
