package tech.gaosong886.shared.auth.model.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayloadDTO {
    private int id;
    private String username;
    private Set<JwtPayloadUserRoleDTO> roles;
}
