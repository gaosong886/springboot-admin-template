package tech.gaosong886.system.controller;

import tech.gaosong886.system.model.vo.SysPermissionVO;
import tech.gaosong886.system.service.SysPermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("sys-permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    @GetMapping("list")
    public List<SysPermissionVO> list() {
        return this.sysPermissionService.list();
    }
}
