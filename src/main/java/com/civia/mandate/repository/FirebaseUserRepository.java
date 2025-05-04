package com.civia.mandate.repository;

import com.civia.mandate.dto.UserDto;
import com.civia.mandate.mapper.UserMapper;
import com.civia.mandate.repository.model.FirebaseUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@AllArgsConstructor
public class FirebaseUserRepository {

    private UserMapper userMapper;
    private FirebaseAuth firebaseAuth;

    public void createUser(UserDto userDto) {

        try {
            FirebaseUserModel firebaseUserModel = userMapper.dtoToFirebaseModel(userDto);
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                    .setEmail(firebaseUserModel.getEmail())
                    .setPassword(firebaseUserModel.getPassword())
                    .setDisplayName(firebaseUserModel.getDisplayName());

            UserRecord userRecord = firebaseAuth.createUser(createRequest);

            Map<String, Object> claims = Map.of("role", firebaseUserModel.getRole().name());
            firebaseAuth.setCustomUserClaims(userRecord.getUid(), claims);
        }catch(FirebaseAuthException exception){
            throw new RuntimeException(exception.getMessage());
        }

    }

    public String getUser(String token) {

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getEmail();
        }catch(FirebaseAuthException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }
}