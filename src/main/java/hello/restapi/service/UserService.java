package hello.restapi.service;

import hello.restapi.advice.exception.CSignInFailedException;
import hello.restapi.advice.exception.CUserNotFoundException;
import hello.restapi.controller.dto.UserDto;
import hello.restapi.entity.User;
import hello.restapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User findUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(CUserNotFoundException::new);
    }

    public User saveUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }

    public User modifyUser(UserDto userDto) {
        Long id = userRepository.findByUserId(userDto.getUserId()).orElseThrow(CUserNotFoundException::new).getId();
        User user = User.builder()
                .id(id)
                .userId(userDto.getUserId())
                .userName(userDto.getUserName())
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteByUserId(userId);
    }

    public User signIn(UserDto userDto) {
        User user = userRepository.findByUserId(userDto.getUserId()).orElseThrow(CSignInFailedException::new);
        if (!passwordEncoder.matches(userDto.getUserPassword(), user.getUserPassword())) {
            throw new CSignInFailedException();
        }
        return user;
    }

    public void signUp(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .userPassword(passwordEncoder.encode(userDto.getUserPassword()))
                .userName(userDto.getUserName())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(user);
    }
}
