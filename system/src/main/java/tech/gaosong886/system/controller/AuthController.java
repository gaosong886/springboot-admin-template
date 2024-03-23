package tech.gaosong886.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.gaosong886.shared.auth.annotation.AllowAnonymous;
import tech.gaosong886.shared.auth.constant.JwtTokenType;
import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.shared.auth.model.vo.JwtTokenVO;
import tech.gaosong886.shared.auth.service.JwtService;
import tech.gaosong886.system.model.dto.LoginDTO;
import tech.gaosong886.system.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @AllowAnonymous
    @PostMapping("login")
    public JwtTokenVO login(@Validated @RequestBody LoginDTO loginDTO) {
        return this.authService.validateSysUser(loginDTO);
    }

    @AllowAnonymous
    @PostMapping("refresh")
    public JwtTokenVO refresh(@RequestBody JwtTokenVO jwtToken) {
        JwtPayloadDTO jwtPayload = this.jwtService.verify(JwtTokenType.REFRESH.value, jwtToken.getRefreshToken(),
                JwtPayloadDTO.class);
        return this.authService.refreshAuthToken(jwtPayload);
    }
}
