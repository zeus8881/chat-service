package com.example.userserviceclean.service.impl;

import com.example.userserviceclean.dto.UserDTO;
import com.example.userserviceclean.mapper.UserMapper;
import com.example.userserviceclean.model.User;
import com.example.userserviceclean.repository.UserRepository;
import com.example.userserviceclean.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RedisTemplate<String, UserDTO> redisTemplate;


    @Override
    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        User user = userMapper.toModel(userDTO);

        if (user.getPasswordHash().length() < 6) {
            log.warn("Password is too short");
            return ResponseEntity.badRequest()
                    .build();
        }
        User save = userRepository.save(user);
        UserDTO dto = userMapper.toDTO(save);
        redisTemplate.opsForValue().set(getCachedKey(save.getId()), dto);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        String key = getCachedKey(id);
        UserDTO cachedUserDTO = redisTemplate.opsForValue().get(key);

        if (cachedUserDTO != null) {
            return ResponseEntity.ok(cachedUserDTO);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found"));

        UserDTO dto = userMapper.toDTO(user);
        redisTemplate.opsForValue().set(key, dto);

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "User not found"));

        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setRole(userDTO.role());
        user.setPasswordHash(userDTO.passwordHash());

        User save = userRepository.save(user);
        UserDTO dto = userMapper.toDTO(user);

        redisTemplate.opsForValue().set(getCachedKey(save.getId()), dto);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> userDTOS = userRepository.findAll();
        List<UserDTO> userDTOList = userDTOS.stream()
                .map(userMapper::toDTO)
                .toList();
        return ResponseEntity.ok(userDTOList);
    }

    public String getCachedKey(Long id) {
        return "user:" + id;
    }
}
