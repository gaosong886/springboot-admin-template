package tech.gaosong886.system.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import tech.gaosong886.system.model.dto.SysMenuDTO;
import tech.gaosong886.system.model.entity.SysMenuEntity;
import tech.gaosong886.system.model.vo.SysMenuVO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysMenuMapper {
    SysMenuMapper INSTANCE = Mappers.getMapper(SysMenuMapper.class);

    SysMenuEntity dtoToEntity(SysMenuDTO sysMenuDTO);

    SysMenuVO entityToVO(SysMenuEntity sysMenuEntity);

    List<SysMenuVO> entityListToVOList(List<SysMenuEntity> sysMenuEntityList);
}
