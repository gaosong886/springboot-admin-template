package tech.gaosong886.shared.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayloadUserRoleDTO {
    private int id;
    private String name;
}
