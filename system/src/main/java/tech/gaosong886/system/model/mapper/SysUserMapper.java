package tech.gaosong886.system.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import tech.gaosong886.shared.auth.model.dto.JwtPayloadDTO;
import tech.gaosong886.system.model.dto.SysUserCreateDTO;
import tech.gaosong886.system.model.entity.SysUserEntity;
import tech.gaosong886.system.model.vo.SysUserVO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserMapper {
    SysUserMapper INSTANCE = Mappers.getMapper(SysUserMapper.class);

    JwtPayloadDTO entityToJwtPayload(SysUserEntity sysUserEntity);

    JwtPayloadDTO voToJwtPayload(SysUserVO sysUserVO);

    SysUserVO entityToVO(SysUserEntity sysUserEntity);

    List<SysUserVO> entityListToVOList(List<SysUserEntity> sysUserEntityList);

    SysUserEntity createSysUserDTOToEntity(SysUserCreateDTO createSysUserDTO);
}
