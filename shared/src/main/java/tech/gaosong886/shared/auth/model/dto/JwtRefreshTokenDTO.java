package tech.gaosong886.shared.auth.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRefreshTokenDTO {
    @NotNull
    private String refreshToken;
}
