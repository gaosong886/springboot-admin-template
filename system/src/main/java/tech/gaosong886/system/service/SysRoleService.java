package tech.gaosong886.system.service;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.gaosong886.system.constant.RedisKey;
import tech.gaosong886.system.model.dto.SysRoleDTO;
import tech.gaosong886.system.model.entity.SysRoleEntity;
import tech.gaosong886.system.model.mapper.SysRoleMapper;
import tech.gaosong886.system.model.vo.SysRoleVO;
import tech.gaosong886.system.repository.SysMenuRepository;
import tech.gaosong886.system.repository.SysRoleRepository;

@Service
public class SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建角色
     * @param sysRoleDTO
     * @return SysRoleVO 对象
     */
    public SysRoleVO create(SysRoleDTO sysRoleDTO) {
        SysRoleEntity entity = SysRoleMapper.INSTANCE.dtoToEntity(sysRoleDTO);
        if (sysRoleDTO.getMenuIds().size() > 0)
            entity.setMenus(this.sysMenuRepository.findByIdIn(sysRoleDTO.getMenuIds()));

        Set<String> permissionSet = new HashSet<>();
        if (entity.getMenus() != null) {
            // 取出该角色的所有权限
            entity.getMenus()
                    .forEach(menu -> menu.getPermissions()
                            .forEach(permission -> permissionSet.add(permission.getName())));
        }

        this.sysRoleRepository.save(entity);

        // 把权限字符串存入 Redis Set
        this.saveRolePermissionToCache(entity.getId(), permissionSet);
        return SysRoleMapper.INSTANCE.entityToVO(entity);
    }


    /**
     * 更新角色信息
     * @param roleId
     * @param sysRoleDTO
     * @return SysRoleVO 对象
     */
    public SysRoleVO update(int roleId, SysRoleDTO sysRoleDTO) {
        SysRoleEntity entity = this.sysRoleRepository.findById(roleId).get();
        entity.setName(sysRoleDTO.getName());
        entity.setDescription(sysRoleDTO.getDescription());

        // roleId == 1 是管理员，应该拥有全部权限
        if (roleId == 1) {
            entity.setMenus(this.sysMenuRepository.findAll());
        } else {
            entity.setMenus(this.sysMenuRepository.findByIdIn(sysRoleDTO.getMenuIds()));
        }

        // 取出该角色的所有权限名称字符串
        Set<String> permissionSet = new HashSet<>();
        entity.getMenus()
                .forEach(menu -> menu.getPermissions().forEach(permission -> permissionSet.add(permission.getName())));
        this.sysRoleRepository.save(entity);

        // 把权限字符串存入 Redis Set
        this.saveRolePermissionToCache(entity.getId(), permissionSet);
        return SysRoleMapper.INSTANCE.entityToVO(entity);
    }

    /**
     * 删除角色
     * @param roleId
     */
    public void delete(int roleId) {
        // 不能删管理员角色
        if (roleId == 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You cant delete the role of administrator");
        this.sysRoleRepository.deleteById(roleId);
        this.deleteRolePermissionInCache(roleId);
    }

    /**
     * 列表查询
     * @return List<SysRoleVO> 角色列表
     */
    public List<SysRoleVO> list() {
        return SysRoleMapper.INSTANCE.entityListToVOList(this.sysRoleRepository.findAll());
    }

    /**
     * 保存角色权限至 Redis
     * @param roleId
     * @param permissions 权限集合
     */
    @SuppressWarnings("null")
    public void saveRolePermissionToCache(int roleId, Set<String> permissions) {
        this.redisTemplate.opsForSet().add(RedisKey.SYS_ROLE_PERMISSION(roleId), permissions);
    }

    /**
     * 判断角色是否有指定权限
     * @param roleId
     * @param permissionString 权限字符串
     * @return true / false
     */
    @SuppressWarnings("null")
    public boolean hasPermission(int roleId, String permissionString) {
        return this.redisTemplate.opsForSet().isMember(RedisKey.SYS_ROLE_PERMISSION(roleId), permissionString);
    }

    /**
     * 从 Redis 中删除角色的权限
     */
    @SuppressWarnings("null")
    public void deleteRolePermissionInCache(int roleId) {
        this.redisTemplate.delete(RedisKey.SYS_ROLE_PERMISSION(roleId));
    }
}
