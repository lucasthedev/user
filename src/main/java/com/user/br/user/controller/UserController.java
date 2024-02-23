package com.user.br.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.user.br.user.dto.UserDto;
import com.user.br.user.entity.User;
import com.user.br.user.usecase.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserUseCase userUseCase;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "userId",
                                                    direction = Sort.Direction.ASC)Pageable pageable){
        Page<User> userPage = userUseCase.findAll(pageable);
        if(!userPage.isEmpty()){
            for(User user: userPage.toList()){
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId")UUID userId){
        Optional<User> user = userUseCase.findById(userId);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId")UUID userId){
        Optional<User> user = userUseCase.findById(userId);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userUseCase.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).body("User "+ userId + " was deleted");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId")UUID userId,
                                             @RequestBody
                                             @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
        Optional<User> user = userUseCase.findById(userId);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userEntity = user.get();
        userEntity.setFullName(userDto.fullName());
        userEntity.setPhoneNumber(userDto.phoneNumber());
        userEntity.setCpf(userDto.cpf());
        userEntity.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userUseCase.save(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(userEntity);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId")UUID userId,
                                                 @RequestBody
                                                 @Validated(UserDto.UserView.PasswordPut.class)
                                                 @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        Optional<User> user = userUseCase.findById(userId);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if(!userDto.oldPassword().equals(user.get().getPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The given password is wrong");
        }

        var userEntity = user.get();
        userEntity.setPassword(userDto.password());
        userEntity.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userUseCase.save(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId")UUID userId,
                                              @RequestBody
                                              @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){
        Optional<User> user = userUseCase.findById(userId);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userEntity = user.get();
        userEntity.setImageUrl(userDto.imageUrl());
        userEntity.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        userUseCase.save(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(userEntity);
    }

}
