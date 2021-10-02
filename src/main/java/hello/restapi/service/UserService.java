package hello.restapi.service;

import hello.restapi.controller.dto.UserDto;
import hello.restapi.entity.User;
import hello.restapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User findUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(RuntimeException::new);
    }

    public User saveUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }

    public User modifyUser(UserDto userDto) {
        Long id = userRepository.findByUserId(userDto.getUserId()).orElseThrow(NoSuchElementException::new).getId();
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
}
