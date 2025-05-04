package com.civia.mandate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder=true)
public class UserDto {

    private String email;
    private String password;
    private String displayName;
    private Role role;
    private String department;
}
