package com.civia.mandate.dto.inout;

import com.civia.mandate.dto.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRequest {

    @Email(message="Please provide a valid email address")
    @Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
    @EqualsAndHashCode.Include
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, message = "Password is required")
    private String password;

    @NotNull(message = "Display name cannot be null")
    @Size(min = 2, message = "Display name is required")
    private String displayName;

    @NotNull(message = "Role name cannot be null")
    private Role role;

    @NotNull(message = "Department cannot be null")
    @Size(min = 2, message = "Department is required")
    private String department;
}
