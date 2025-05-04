package com.civia.mandate.service.user;

import com.civia.mandate.dto.UserDto;
import com.civia.mandate.dto.inout.UserRequest;
import com.civia.mandate.dto.inout.UserResponse;
import com.civia.mandate.mapper.UserMapper;
import com.civia.mandate.repository.FirebaseUserRepository;
import com.civia.mandate.repository.UserRepository;
import com.civia.mandate.repository.model.UserModel;
import com.google.firebase.auth.UserRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserMapper userMapper;
    private FirebaseUserRepository firebaseUserRepository;
    private UserRepository userRepository;


    public boolean createUser(UserRequest request) {

        UserDto userDto = userMapper.requestToDto(request);

        boolean existsByEmail = userRepository.existsByEmail(userDto.getEmail());
        if(existsByEmail) return false;

        firebaseUserRepository.createUser(userDto);
        UserModel userModel = userMapper.dtoToModel(request);
        userRepository.save(userModel);

        return true;
    }

    public UserResponse getUser(String token) {

        String email = firebaseUserRepository.getUser(token);
        UserModel userModel = userRepository.findByEmail(email);

        return userMapper.modelToResponse(userModel);
    }
}
