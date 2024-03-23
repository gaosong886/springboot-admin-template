package tech.gaosong886.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import tech.gaosong886.shared.auth.constant.JwtStatics;
import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.shared.pagination.model.dto.PageDTO;
import tech.gaosong886.shared.pagination.model.vo.PagePayloadVO;
import tech.gaosong886.system.annotation.AvoidPermission;
import tech.gaosong886.system.model.dto.SysUserCreateDTO;
import tech.gaosong886.system.model.dto.SysUserUpdateDTO;
import tech.gaosong886.system.model.vo.FileUploadVO;
import tech.gaosong886.system.model.vo.SysUserVO;
import tech.gaosong886.system.service.SysUserService;
import tech.gaosong886.system.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("sys-user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UploadService uploadService;

    @AvoidPermission()
    @GetMapping("profile")
    public SysUserVO profile(HttpServletRequest httpServletRequest) {
        JwtPayloadDTO payload = (JwtPayloadDTO) httpServletRequest.getAttribute(JwtStatics.JWT_PAYLOAD_USER_KEY);
        return this.sysUserService.getUserProfile(payload.getId());
    }

    @PostMapping("page")
    public PagePayloadVO<SysUserVO> page(@Validated @RequestBody PageDTO paginationDTO) {
        return this.sysUserService.page(paginationDTO);
    }

    @PostMapping("update/{id}")
    public void update(
            @PathVariable("id") @NotNull Integer id,
            @Validated @RequestBody SysUserUpdateDTO updateSysUserDTO) {
        this.sysUserService.update(id, updateSysUserDTO);
    }

    @PostMapping("delete/{id}")
    public void delete(
            @PathVariable("id") @NotNull Integer id) {
        this.sysUserService.delete(id);
    }

    @PostMapping("create")
    public SysUserVO create(
            @Validated @RequestBody SysUserCreateDTO createSysUserDTO) {
        return this.sysUserService.create(createSysUserDTO);
    }

    @PostMapping("photo")
    public FileUploadVO photo(@RequestParam("file") MultipartFile file) {
        return this.uploadService.upload(file);
    }
}
