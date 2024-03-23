package tech.gaosong886.system.service;

import tech.gaosong886.shared.pagination.model.dto.PageDTO;
import tech.gaosong886.shared.pagination.model.vo.PagePayloadVO;
import tech.gaosong886.system.constant.AccountStatus;
import tech.gaosong886.system.constant.RedisKey;
import tech.gaosong886.system.model.dto.SysUserCreateDTO;
import tech.gaosong886.system.model.dto.SysUserUpdateDTO;
import tech.gaosong886.system.model.entity.SysUserEntity;
import tech.gaosong886.system.model.mapper.SysUserMapper;
import tech.gaosong886.system.model.vo.SysUserVO;
import tech.gaosong886.system.repository.SysRoleRepository;
import tech.gaosong886.system.repository.SysUserRepository;
import tech.gaosong886.system.service.SysUserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建账号
     * @param createSysUserDTO
     * @return SysUserVO 对象
     */
    public SysUserVO create(SysUserCreateDTO createSysUserDTO) {
        if (this.sysUserRepository.findByUsername(createSysUserDTO.getUsername()) != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Username already exists.");

        SysUserEntity entity = SysUserMapper.INSTANCE.createSysUserDTOToEntity(createSysUserDTO);
        entity.setPassword(BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt()));

        // 获得用户的角色
        if (createSysUserDTO.getRoleIds().size() > 0)
            entity.setRoles(this.sysRoleRepository.findByIdIn(createSysUserDTO.getRoleIds()));

        this.sysUserRepository.save(entity);

        SysUserVO vo = SysUserMapper.INSTANCE.entityToVO(entity);

        // 保存至 Redis
        this.saveSysUserToCache(vo);
        return vo;
    }

    /**
     * 更新账号信息
     * @param userId
     * @param updateSysUserDTO
     * @return SysUserVO 对象
     */
    public SysUserVO update(int userId, SysUserUpdateDTO updateSysUserDTO) {
        SysUserEntity entity = this.findById(userId);
        entity.setUsername(updateSysUserDTO.getUsername());
        entity.setAccountStatus(updateSysUserDTO.getAccountStatus());
        entity.setNickname(updateSysUserDTO.getNickname());
        entity.setPhoto(updateSysUserDTO.getPhoto());
        entity.setRemark(updateSysUserDTO.getRemark());

        if (updateSysUserDTO.getPassword() != null)
            entity.setPassword(BCrypt.hashpw(updateSysUserDTO.getPassword(), BCrypt.gensalt()));

        // 获得账号的角色
        entity.setRoles(this.sysRoleRepository.findByIdIn(updateSysUserDTO.getRoleIds()));

        this.sysUserRepository.save(entity);

        SysUserVO vo = SysUserMapper.INSTANCE.entityToVO(entity);

        // 保存至 Redis
        this.saveSysUserToCache(vo);
        return vo;
    }

    /**
     * 删除账号
     * @param userId
     */
    public void delete(int userId) {
        this.sysUserRepository.deleteById(userId);
    }

    /**
     * 分页查询
     * @param pageDTO
     * @return PagePayloadVO<SysUserVO> 分页对象
     */
    public PagePayloadVO<SysUserVO> page(PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPage() - 1, pageDTO.getPageSize());

        Page<SysUserEntity> page;
        if (pageDTO.getQuery() != null && !pageDTO.getQuery().equals("")) {
            // 根据输入对用户名和昵称做模糊查询
            page = this.sysUserRepository.findByUsernameLikeOrNicknameLike(pageDTO.getQuery(), pageable);
        } else {
            page = this.sysUserRepository.find(pageable);
        }

        PagePayloadVO<SysUserVO> pagePayloadVO = new PagePayloadVO<>();
        pagePayloadVO.setPage(pageDTO.getPage());
        pagePayloadVO.setPageSize(pageDTO.getPageSize());
        pagePayloadVO.setTotalItems(page.getTotalElements());
        pagePayloadVO.setTotalPages(page.getTotalPages());
        pagePayloadVO.setData(SysUserMapper.INSTANCE.entityListToVOList(page.getContent()));
        return pagePayloadVO;
    }

    /**
     * 验证账号密码
     * @param username
     * @param password
     * @return SysUserVO 对象
     */
    public SysUserVO validate(String username, String password) {
        SysUserEntity entity = this.sysUserRepository.findByUsername(username);

        if (entity == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Login failed, account does not exist.");

        if (entity.getAccountStatus().intValue() == AccountStatus.BANNED.value)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Login failed, account is banned.");

        if (!BCrypt.checkpw(password, entity.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Login failed, username or password is incorrect.");

        return SysUserMapper.INSTANCE.entityToVO(entity);
    }

    /**
     * 获取用户信息
     * @param userId
     * @return SysUserVO 对象
     */
    public SysUserVO getUserProfile(int userId) {
        return this.getSysUserFromCache(userId);
    }

    /**
     * 根据用户 id 查询
     * @param userId
     * @return SysUserEntity 实体
     */
    public SysUserEntity findById(int userId) {
        return this.sysUserRepository.findById(userId).get();
    }

    /**
     * 从缓存中获取用户信息
     * @param userId
     * @return SysUserVO 对象
     */
    @SuppressWarnings("null")
    public SysUserVO getSysUserFromCache(int userId) {
        SysUserVO vo = (SysUserVO) redisTemplate.opsForValue().get(RedisKey.SYS_USER(userId));
        if (vo != null)
            return vo;

        // 缓存中没有则更新缓存
        SysUserEntity entity = this.findById(userId);
        vo = SysUserMapper.INSTANCE.entityToVO(entity);
        this.saveSysUserToCache(vo);
        return vo;
    }

    /**
     * 将用户信息更新至缓存
     * @param sysUserVO
     */
    @SuppressWarnings("null")
    public void saveSysUserToCache(SysUserVO sysUserVO) {
        redisTemplate.opsForValue().set(RedisKey.SYS_USER(sysUserVO.getId()), sysUserVO);
    }
}
