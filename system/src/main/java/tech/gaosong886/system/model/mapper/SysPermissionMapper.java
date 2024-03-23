package tech.gaosong886.system.model.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tech.gaosong886.system.model.entity.SysPermissionEntity;
import tech.gaosong886.system.model.vo.SysPermissionVO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysPermissionMapper {
    SysPermissionMapper INSTANCE = Mappers.getMapper(SysPermissionMapper.class);

    SysPermissionVO entityToVO(SysPermissionEntity sysPermissionEntity);

    List<SysPermissionVO> entityListToVOList(List<SysPermissionEntity> sysPermissionEntityList);
}
