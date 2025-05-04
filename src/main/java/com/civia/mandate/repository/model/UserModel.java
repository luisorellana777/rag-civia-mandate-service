package com.civia.mandate.repository.model;

import com.civia.mandate.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Builder(toBuilder=true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "users")
public class UserModel {

    @Id
    @EqualsAndHashCode.Include
    private String email;
    private String displayName;
    private Role role;
    private String department;
}
