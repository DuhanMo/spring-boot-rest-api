package hello.restapi.service;

import hello.restapi.advice.exception.CSignInFailedException;
import hello.restapi.advice.exception.CUserExistException;
import hello.restapi.advice.exception.CUserNotFoundException;
import hello.restapi.controller.dto.UserDto;
import hello.restapi.entity.User;
import hello.restapi.model.social.KakaoProfile;
import hello.restapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @PostConstruct
    public void registerTestUser() {
        for (int i = 0; i < 10; i++) {
            User user = User.builder()
                    .userId("user" + i)
                    .userPassword(passwordEncoder.encode("user" + i))
                    .userName("user_name" + i)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userRepository.save(user);
        }
    }

    public User findUserByProvider(KakaoProfile profile, String provider) {
        return userRepository.findByUserIdAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(CUserNotFoundException::new);
    }

    @Transactional
    public void signUpProvider(KakaoProfile profile, String provider, String name) {
        Optional<User> user = userRepository.findByUserIdAndProvider(String.valueOf(profile.getId()), provider);
        if (user.isPresent()) {
            throw new CUserExistException();
        }
        User inUser = User.builder()
                .userId(String.valueOf(profile.getId()))
                .provider(provider)
                .userName(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userRepository.save(inUser);
    }
}