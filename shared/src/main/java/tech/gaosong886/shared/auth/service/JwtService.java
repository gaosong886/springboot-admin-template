package tech.gaosong886.shared.auth.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
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
import tech.gaosong886.shared.auth.config.JwtConfig;

@Service
public class JwtService {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ObjectMapper objectMapper;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 初始化，将配置文件中的 keyPair 转化为可用格式
     */
    @SneakyThrows
    @PostConstruct
    public void init() {
        this.publicKey = RsaKeyUtil.getPublicKey(
                new String(Base64.decodeBase64(this.jwtConfig.getPublicKeyBase64()), "utf-8"));
        this.privateKey = RsaKeyUtil.getPrivateKey(
                new String(Base64.decodeBase64(this.jwtConfig.getPrivateKeyBase64()), "utf-8"));
    }

    /**
     * 生成 Token 对象
     * 
     * @param payload jwt 数据载荷
     * @return JwtTokenVO
     */
    @SneakyThrows
    public JwtTokenVO generateTokenPair(JwtPayloadDTO payload) {
        JwtPayloadDTO refreshPayload = new JwtPayloadDTO(payload.id(), null, null);
        JwtTokenVO jwtToken = new JwtTokenVO(
                // tokenType
                JwtStatics.JWT_TOKEN_PREFIX,

                // accessToken
                this.sign(
                        JwtTokenType.ACCESS.value,
                        payload,
                        this.jwtConfig.getAccessTokenExpiresInSec()),

                // accessTokenExpiresInSec
                this.jwtConfig.getAccessTokenExpiresInSec(),

                // refreshToken
                this.sign(
                        JwtTokenType.REFRESH.value,
                        refreshPayload,
                        this.jwtConfig.getRefreshTokenExpiresInSec()),

                // refreshTokenExpiresInSec
                this.jwtConfig.getRefreshTokenExpiresInSec());
        return jwtToken;
    }

    /**
     * 构造 Jwt Token
     * 
     * @param type                Token 类型（access 或 refresh）
     * @param payload             数据载荷
     * @param expirationTimeInSec 过期时间
     * @return Token 字符串
     */
    @SneakyThrows
    public String sign(int type, JwtPayloadDTO payload, long expirationTimeInSec) {
        return Jwts.builder()
                .claim(JwtStatics.JWT_TOKEN_TYPE_KEY, type)
                .claim(JwtStatics.JWT_PAYLOAD_USER_KEY, objectMapper.writeValueAsString(payload))
                .expiration(new Date(new Date().getTime() + expirationTimeInSec * 1000))
                .signWith(this.privateKey)
                .compact();
    }

    /**
     * 验证并解包 Token
     * 
     * @param type  Token 类型（access 或 refresh）
     * @param token
     * @param clazz payload 的数据类型
     * @return Jwt payload
     */
    @SneakyThrows
    public <T> T verify(int type, String token, Class<T> clazz) {
        Claims claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // 检查过期时间
        Date expiration = claims.getExpiration();
        if (expiration.before(new Date()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The provided access token is expired.");

        // 检查 token 类型是否和传入的类型一致
        int tokenType = Integer.parseInt(claims.get(JwtStatics.JWT_TOKEN_TYPE_KEY).toString());
        if (tokenType != type)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong token.");

        return objectMapper.readValue(claims.get(JwtStatics.JWT_PAYLOAD_USER_KEY).toString(), clazz);
    }

    /**
     * 从请求头中获取 Token
     * 
     * @param request
     * @return Token 字符串
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtStatics.JWT_AUTH_HEADER);
        if (authHeader != null && authHeader.toLowerCase().startsWith(JwtStatics.JWT_TOKEN_PREFIX.toLowerCase()))
            return authHeader.substring(JwtStatics.JWT_TOKEN_PREFIX.length()).trim();

        return null;
    }
}
