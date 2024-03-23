package tech.gaosong886.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import io.lettuce.core.dynamic.annotation.Param;
import tech.gaosong886.system.model.entity.SysUserEntity;
import java.io.Serializable;

@Repository
public interface SysUserRepository extends JpaRepository<SysUserEntity, Serializable> {
        @Query("SELECT e FROM SysUserEntity e " +
                        "LEFT JOIN FETCH e.roles r " +
                        "WHERE e.username = :username")
        SysUserEntity findByUsername(@Param("username") String username);

        @Query("SELECT e FROM SysUserEntity e " +
                        "LEFT JOIN FETCH e.roles r " +
                        "WHERE e.username LIKE CONCAT('%', :queryCondition, '%') " +
                        "OR e.nickname LIKE CONCAT('%', :queryCondition, '%')")
        Page<SysUserEntity> findByUsernameLikeOrNicknameLike(@Param("queryCondition") String queryCondition,
                        Pageable pageable);

        @Query("SELECT e FROM SysUserEntity e " +
                        "LEFT JOIN FETCH e.roles r")
        Page<SysUserEntity> find(Pageable pageable);
}
