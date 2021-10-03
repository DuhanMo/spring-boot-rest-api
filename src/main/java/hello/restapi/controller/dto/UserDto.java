package hello.restapi.controller.dto;

import hello.restapi.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserDto {

    private String userId;
    private String userName;
    private String userPassword;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .userName(userName)
                .userPassword(userPassword)
                .build();
    }
}
