package com.civia.mandate.dto.inout;

import com.civia.mandate.dto.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserResponse {

    @EqualsAndHashCode.Include
    private String email;
    private String displayName;
    private Role role;
    private String department;
}
