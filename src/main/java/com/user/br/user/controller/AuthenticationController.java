package com.user.br.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.user.br.user.dto.UserDto;
import com.user.br.user.entity.User;
import com.user.br.user.enums.UserStatus;
import com.user.br.user.enums.UserType;
import com.user.br.user.usecase.UserUseCase;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserUseCase userUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUse(@RequestBody
                                                  @Validated(UserDto.UserView.RegistrationPost.class)
                                                  @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
        if(userUseCase.existsByUserName(userDto.userName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error ! User name already exists");
        }

        if(userUseCase.existsByEmail(userDto.email())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error ! Email already exists");
        }
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserType(UserType.STUDENT);
        user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userUseCase.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
