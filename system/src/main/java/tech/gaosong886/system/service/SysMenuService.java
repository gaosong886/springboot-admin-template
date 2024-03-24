package tech.gaosong886.system.service;

import tech.gaosong886.system.constant.MenuType;
import tech.gaosong886.system.model.dto.SysMenuDTO;
import tech.gaosong886.system.model.entity.SysMenuEntity;
import tech.gaosong886.system.model.entity.SysPermissionEntity;
import tech.gaosong886.system.model.entity.SysRoleEntity;
import tech.gaosong886.system.model.mapper.SysMenuMapper;
import tech.gaosong886.system.model.vo.SysMenuVO;
import tech.gaosong886.system.repository.SysMenuRepository;
import tech.gaosong886.system.repository.SysPermissionRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SysMenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 创建菜单节点
     * 
     * @param sysMenuDTO
     * @return SysMenuVO 对象
     */
    public SysMenuVO create(SysMenuDTO sysMenuDTO) {
        SysMenuEntity entity = SysMenuMapper.INSTANCE.dtoToEntity(sysMenuDTO);

        // 类型为 '操作' 的节点，找出其对应的权限
        if (entity.getType() == MenuType.OPERATION.value) {
            entity.setPermissions(
                    this.sysPermissionRepository.findByIdIn(sysMenuDTO.getPermissionIds()));
        }

        this.sysMenuRepository.save(entity);
        return SysMenuMapper.INSTANCE.entityToVO(entity);
    }

    /**
     * 更新菜单节点
     * 
     * @param menuId
     * @param sysMenuDTO
     */
    public void update(int menuId, SysMenuDTO sysMenuDTO) {
        SysMenuEntity entity = this.sysMenuRepository.findById(menuId).get();
        entity.setName(sysMenuDTO.getName());
        entity.setType(sysMenuDTO.getType());
        entity.setIcon(sysMenuDTO.getIcon());
        entity.setPath(sysMenuDTO.getPath());
        entity.setParentId(sysMenuDTO.getParentId());
        entity.setSortWeight(sysMenuDTO.getSortWeight());
        entity.setHidden(sysMenuDTO.getHidden());

        // 类型为 '操作' 的节点，找出其对应的权限
        if (entity.getType() == MenuType.OPERATION.value) {
            entity.setPermissions(
                    this.sysPermissionRepository.findByIdIn(sysMenuDTO.getPermissionIds()));
        }

        this.sysMenuRepository.save(entity);

        // 类型为 '操作' 的节点，同步关联角色的权限集合
        if (entity.getType() == MenuType.OPERATION.value) {
            List<SysRoleEntity> roleList = entity.getRoles();
            if (roleList != null && roleList.size() > 0)
                this.syncRolePermissionsByRoleIds(roleList.stream().map(r -> r.getId()).toList());
        }
    }

    /**
     * 显示 / 隐藏菜单节点
     * 
     * @param menuId
     */
    public void hide(int menuId) {
        SysMenuEntity entity = this.sysMenuRepository.findById(menuId).get();
        entity.setHidden(entity.getHidden() == 0 ? 1 : 0);
        this.sysMenuRepository.save(entity);
    }

    /**
     * 删除节点
     * 
     * @param menuId
     */
    public void delete(int menuId) {
        List<SysMenuEntity> childNodeList = this.sysMenuRepository.findByParentId(menuId);

        // 不能删除有子节点的节点
        if (childNodeList != null && childNodeList.size() > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The node has child nodes. Please delete the child nodes first.");
        }

        SysMenuEntity node = this.sysMenuRepository.findById(menuId).get();

        // 为了删除节点相关权限，先把有访问权限的角色查询出来
        List<SysRoleEntity> roleList = node.getRoles();

        this.sysMenuRepository.delete(node);

        // 类型为 '操作' 的节点，同步关联角色的权限集合
        if (node.getType() == MenuType.OPERATION.value) {
            if (roleList != null && roleList.size() > 0)
                this.syncRolePermissionsByRoleIds(roleList.stream().map(r -> r.getId()).toList());
        }
    }

    /**
     * 列表查询
     * 
     * @return List<SysMenuVO> 全部菜单节点列表
     */
    public List<SysMenuVO> list() {
        List<SysMenuEntity> list = this.sysMenuRepository.findAll(Sort.by(Sort.Direction.ASC, "sortWeight"));
        return SysMenuMapper.INSTANCE.entityListToVOList(list);
    }

    /**
     * 查询角色可显示的菜单
     * 
     * @param roleIds 角色 id 列表
     * @return List<SysMenuVO> 菜单列表
     */
    public List<SysMenuVO> listByRoleIds(List<Integer> roleIds) {
        // 管理员角色，显示全部菜单
        if (roleIds.indexOf(1) >= 0) {
            List<SysMenuEntity> list = this.sysMenuRepository.findDisplayableNodes();
            return SysMenuMapper.INSTANCE.entityListToVOList(list);
        }

        // 普通角色
        List<SysMenuEntity> list = this.sysMenuRepository.findDisplayableNodesByRoleIdIn(roleIds);
        return SysMenuMapper.INSTANCE.entityListToVOList(list);
    }

    /**
     * 同步角色权限到 Redis
     * 
     * @param roleIds 角色 id 列表
     */
    private void syncRolePermissionsByRoleIds(List<Integer> roleIds) {
        roleIds.forEach(id -> {
            // 先获取角色能访问的菜单列表
            List<SysMenuEntity> menuList = this.sysMenuRepository.findByTypeAndRoleId(MenuType.OPERATION.value, id);

            Set<String> permissionSet = new HashSet<>();

            // 汇总菜单列表涉及到的权限
            menuList.forEach(m -> {
                List<SysPermissionEntity> permissionList = m.getPermissions();
                if (permissionList == null || permissionList.isEmpty())
                    return;
                permissionList.forEach(p -> permissionSet.add(p.getName()));
            });

            this.sysRoleService.saveRolePermissionToCache(id, permissionSet);
        });
    }
}
