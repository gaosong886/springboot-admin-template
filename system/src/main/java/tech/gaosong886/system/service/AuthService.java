package tech.gaosong886.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.shared.auth.model.vo.JwtTokenVO;
import tech.gaosong886.shared.auth.service.JwtService;
import tech.gaosong886.system.constant.AccountStatus;
import tech.gaosong886.system.model.dto.LoginDTO;
import tech.gaosong886.system.model.mapper.SysUserMapper;
import tech.gaosong886.system.model.vo.SysUserVO;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录验证
     * @param loginDTO
     * @return JwtTokenVO 对象
     */
    public JwtTokenVO validateSysUser(LoginDTO loginDTO) {
        SysUserVO vo = this.sysUserService.validate(loginDTO.getUsername(), loginDTO.getPassword());
        return generateAuthToken(vo);
    }

    /**
     * 刷新 Token
     * @param jwtPayload 解码后的 JWT 载荷对象
     * @return JwtTokenVO 对象
     */
    public JwtTokenVO refreshAuthToken(JwtPayloadDTO jwtPayload) {
        SysUserVO sysUserVO = this.sysUserService.getSysUserFromCache(jwtPayload.getId());
        if (sysUserVO == null || sysUserVO.getAccountStatus() == AccountStatus.BANNED.value)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Invalid user.");
        return this.jwtService.generateTokenPair(SysUserMapper.INSTANCE.voToJwtPayload(sysUserVO));
    }

    /**
     * 生成 Jwt Token
     * @param sysUserVO
     * @return JwtTokenVO 对象
     */
    private JwtTokenVO generateAuthToken(SysUserVO sysUserVO) {
        JwtPayloadDTO payload = SysUserMapper.INSTANCE.voToJwtPayload(sysUserVO);
        return this.jwtService.generateTokenPair(payload);
    }
}
