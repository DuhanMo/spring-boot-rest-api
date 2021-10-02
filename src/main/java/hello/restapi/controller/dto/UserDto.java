package hello.restapi.controller.dto;

import hello.restapi.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {

    private String userId;
    private String userName;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .userName(userName)
                .build();
    }
}
