package tech.gaosong886.system.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import tech.gaosong886.system.model.dto.SysRoleDTO;
import tech.gaosong886.system.model.entity.SysRoleEntity;
import tech.gaosong886.system.model.vo.SysRoleVO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysRoleMapper {
    SysRoleMapper INSTANCE = Mappers.getMapper(SysRoleMapper.class);

    SysRoleEntity dtoToEntity(SysRoleDTO sysRoleDTO);

    SysRoleVO entityToVO(SysRoleEntity sysRoleEntity);

    List<SysRoleVO> entityListToVOList(List<SysRoleEntity> sysRoleEntities);
}
