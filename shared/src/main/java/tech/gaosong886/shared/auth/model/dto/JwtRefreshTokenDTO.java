package tech.gaosong886.shared.auth.model.dto;

import jakarta.validation.constraints.NotNull;

public record JwtRefreshTokenDTO(
        @NotNull String refreshToken) {
}
