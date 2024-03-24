package tech.gaosong886.shared.auth.model.dto;

import java.util.Set;

public record JwtPayloadDTO(
        int id,
        String username,
        Set<JwtPayloadUserRoleDTO> roles) {
}
