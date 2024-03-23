package tech.gaosong886.system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotNull;
import tech.gaosong886.system.model.dto.SysRoleDTO;
import tech.gaosong886.system.model.vo.SysRoleVO;
import tech.gaosong886.system.service.SysRoleService;

@RestController
@RequestMapping("sys-role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("create")
    public SysRoleVO create(
            @Validated @RequestBody SysRoleDTO sysRoleDTO) {
        return this.sysRoleService.create(sysRoleDTO);
    }

    @PostMapping("update/{id}")
    public void update(
            @PathVariable("id") @NotNull Integer id,
            @Validated @RequestBody SysRoleDTO sysRoleDTO) {
        this.sysRoleService.update(id, sysRoleDTO);
    }

    @PostMapping("delete/{id}")
    public void delete(
            @PathVariable("id") @NotNull Integer id) {
        this.sysRoleService.delete(id);
    }

    @GetMapping("list")
    public List<SysRoleVO> list() {
        return this.sysRoleService.list();
    }
}
