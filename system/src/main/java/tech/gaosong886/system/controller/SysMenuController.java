package tech.gaosong886.system.controller;

import tech.gaosong886.shared.auth.constant.JwtStatics;
import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.shared.auth.model.dto.JwtPayloadUserRoleDTO;
import tech.gaosong886.system.annotation.AvoidPermission;
import tech.gaosong886.system.model.dto.SysMenuDTO;
import tech.gaosong886.system.model.vo.SysMenuVO;
import tech.gaosong886.system.service.SysMenuService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("sys-menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("create")
    public SysMenuVO create(
            @Validated @RequestBody SysMenuDTO sysMenuDTO) {
        return this.sysMenuService.create(sysMenuDTO);
    }

    @PostMapping("update/{id}")
    public void update(
            @PathVariable("id") @NotNull Integer id,
            @Validated @RequestBody SysMenuDTO sysMenuDTO) {
        this.sysMenuService.update(id, sysMenuDTO);
    }

    @PostMapping("delete/{id}")
    public void delete(
            @PathVariable("id") @NotNull Integer id) {
        this.sysMenuService.delete(id);
    }

    @GetMapping("list")
    public List<SysMenuVO> list() {
        return this.sysMenuService.list();
    }

    @PostMapping("hide/{id}")
    public void hide(
            @PathVariable("id") @NotNull Integer id) {
        this.sysMenuService.hide(id);
    }

    @AvoidPermission()
    @GetMapping("menu")
    public List<SysMenuVO> menu(HttpServletRequest httpServletRequest) {
        JwtPayloadDTO payload = (JwtPayloadDTO) httpServletRequest.getAttribute(JwtStatics.JWT_PAYLOAD_USER_KEY);
        return this.sysMenuService
                .listByRoleIds(payload.getRoles().stream().map(JwtPayloadUserRoleDTO::getId).toList());
    }
}
