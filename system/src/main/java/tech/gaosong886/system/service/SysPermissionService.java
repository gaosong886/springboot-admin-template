package tech.gaosong886.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import tech.gaosong886.system.model.entity.SysPermissionEntity;
import tech.gaosong886.system.model.mapper.SysPermissionMapper;
import tech.gaosong886.system.model.vo.SysPermissionVO;
import tech.gaosong886.system.repository.SysPermissionRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysPermissionService {

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    /**
     * 列表查询
     * @return List<SysPermissionVO> 权限列表
     */
    public List<SysPermissionVO> list() {
        return SysPermissionMapper.INSTANCE.entityListToVOList(this.sysPermissionRepository.findAll());
    }

    /**
     * 保存权限
     * @param names 待保存的权限名称集合
     */
    @SuppressWarnings("null")
    public void saveByNameIn(Set<String> names) {
        List<SysPermissionEntity> list = names.stream().map(
                name -> new SysPermissionEntity().setName(name)).toList();
        this.sysPermissionRepository.saveAll(list);
    }

    /**
     * 删除权限
     * @param names 待删除的权限名称集合
     */
    public void deleteByNameIn(Set<String> names) {
        this.sysPermissionRepository.deleteByNameIn(names);
    }

    /**
     * 根据控制器的方法句柄拼装权限字符串
     * @param handlerMethod 方法句柄
     * @return 形式如 demo:update 的权限字符串
     */
    public String getPermissionStringByHandlerMethod(HandlerMethod handlerMethod) {
        // 排除 springboot 框架内部控制器
        Class<?> controllerClass = handlerMethod.getBeanType();
        if (controllerClass.getName().contains("org.springframework"))
            return null;

        // 获得控制器映射路径
        RequestMapping controllerMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (controllerMapping == null)
            return null;

        String methodName = handlerMethod.getMethod().getName();

        // 权限字符串 = 控制器映射路径 + ":" + 方法名
        return controllerMapping.value()[0] + ":" + methodName;
    }

    /**
     * 同步权限数据到数据库
     * @param newSet 权限字符串集合
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void savePermissions(Set<String> newSet) {
        List<SysPermissionVO> permissionInDB = this.list();

        // 把查询结果转为集合
        Set<String> oldSet = permissionInDB.stream().map(p -> p.getName()).collect(Collectors.toSet());

        // 比较本地和数据库两个集合
        // 取出待保存的新权限
        Set<String> setToSave = newSet.stream()
                .filter(element -> !oldSet.contains(element))
                .collect(Collectors.toSet());

        // 取出待删除的老权限
        Set<String> setToDelete = oldSet.stream()
                .filter(element -> !newSet.contains(element))
                .collect(Collectors.toSet());

        // 结果入库
        if (setToSave.size() > 0)
            this.saveByNameIn(setToSave);
        if (setToDelete.size() > 0)
            this.deleteByNameIn(setToDelete);
    }
}
