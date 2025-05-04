package com.civia.mandate.mapper;

import com.civia.mandate.dto.UserDto;
import com.civia.mandate.dto.inout.*;
import com.civia.mandate.repository.model.FirebaseUserModel;
import com.civia.mandate.repository.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {


    UserDto requestToDto(UserRequest request);

    FirebaseUserModel dtoToFirebaseModel(UserDto dto);


    @Mappings({
            @Mapping(target = "role", ignore = true),
            @Mapping(target = "department", ignore = true)
    })
    UserDto firebaseModelToDto(FirebaseUserModel model);


    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    UserDto modelToDto(UserModel model);

    UserModel dtoToModel(UserRequest request);

    UserResponse dtoToResponse(UserDto userDto);

    UserResponse modelToResponse(UserModel userModel);
}
