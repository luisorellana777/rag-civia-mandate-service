package com.civia.mandate.repository.model;

import com.civia.mandate.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@Builder(toBuilder=true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FirebaseUserModel {

    @EqualsAndHashCode.Include
    private String email;
    private String password;
    private String displayName;
    private Role role;
}
