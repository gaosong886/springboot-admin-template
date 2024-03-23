package tech.gaosong886.shared.auth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenVO {
    private String tokenType;
    private String accessToken;
    private long accessTokenExpiresInSec;
    private String refreshToken;
    private long refreshTokenExpiresInSec;
}
