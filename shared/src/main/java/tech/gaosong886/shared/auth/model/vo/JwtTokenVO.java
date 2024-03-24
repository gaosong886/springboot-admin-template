package tech.gaosong886.shared.auth.model.vo;

public record JwtTokenVO(
        String tokenType,
        String accessToken,
        Long accessTokenExpiresInSec,
        String refreshToken,
        Long refreshTokenExpiresInSec) {
}
