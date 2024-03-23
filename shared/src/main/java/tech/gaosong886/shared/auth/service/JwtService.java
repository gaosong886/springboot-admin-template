package tech.gaosong886.shared.auth.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import tech.gaosong886.shared.auth.constant.JwtStatics;
import tech.gaosong886.shared.auth.constant.JwtTokenType;
import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.shared.auth.model.vo.JwtTokenVO;
import tech.gaosong886.shared.auth.util.RsaKeyUtil;

@Service
public class JwtService {

    @Value("${jwt.access-token-expires-in-sec}")
    private Long accessTokenExpiresInSec;

    @Value("${jwt.refresh-token-expires-in-sec}")
    private Long refreshTokenExpiresInSec;

    @Value("${jwt.public-key-base64}")
    private String publicKeyBase64;

    @Value("${jwt.private-key-base64}")
    private String privateKeyBase64;

    @Autowired
    private ObjectMapper objectMapper;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @SneakyThrows
    @PostConstruct
    public void init() {
        this.publicKey = RsaKeyUtil.getPublicKey(
                new String(Base64.decodeBase64(publicKeyBase64), "utf-8"));
        this.privateKey = RsaKeyUtil.getPrivateKey(
                new String(Base64.decodeBase64(privateKeyBase64), "utf-8"));
    }

    @SneakyThrows
    public JwtTokenVO generateTokenPair(JwtPayloadDTO payload) {
        JwtTokenVO jwtToken = new JwtTokenVO();
        jwtToken.setTokenType(JwtStatics.JWT_TOKEN_PREFIX);
        jwtToken.setAccessToken(this.sign(JwtTokenType.ACCESS.value, payload, this.accessTokenExpiresInSec));
        jwtToken.setAccessTokenExpiresInSec(this.accessTokenExpiresInSec);

        JwtPayloadDTO refreshPayload = new JwtPayloadDTO();
        refreshPayload.setId(payload.getId());
        jwtToken.setRefreshToken(
                this.sign(JwtTokenType.REFRESH.value, refreshPayload, this.refreshTokenExpiresInSec));
        jwtToken.setRefreshTokenExpiresInSec(this.refreshTokenExpiresInSec);

        return jwtToken;
    }

    @SneakyThrows
    public String sign(int type, JwtPayloadDTO payload, long expirationTimeInSec) {
        return Jwts.builder()
                .claim(JwtStatics.JWT_TOKEN_TYPE_KEY, type)
                .claim(JwtStatics.JWT_PAYLOAD_USER_KEY, objectMapper.writeValueAsString(payload))
                .expiration(new Date(new Date().getTime() + expirationTimeInSec * 1000))
                .signWith(this.privateKey)
                .compact();
    }

    @SneakyThrows
    public <T> T verify(int type, String token, Class<T> clazz) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Date expiration = claims.getExpiration();
        if (expiration.before(new Date()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The provided access token is expired.");

        int tokenType = Integer.parseInt(claims.get(JwtStatics.JWT_TOKEN_TYPE_KEY).toString());
        if (tokenType != type)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong token.");

        return objectMapper.readValue(claims.get(JwtStatics.JWT_PAYLOAD_USER_KEY).toString(), clazz);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtStatics.JWT_AUTH_HEADER);
        if (authHeader != null && authHeader.toLowerCase().startsWith(JwtStatics.JWT_TOKEN_PREFIX.toLowerCase()))
            return authHeader.substring(JwtStatics.JWT_TOKEN_PREFIX.length()).trim();

        return null;
    }
}
