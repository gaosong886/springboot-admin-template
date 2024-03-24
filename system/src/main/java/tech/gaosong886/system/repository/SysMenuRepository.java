package tech.gaosong886.system.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import io.lettuce.core.dynamic.annotation.Param;
import tech.gaosong886.system.model.entity.SysMenuEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Repository
public interface SysMenuRepository extends JpaRepository<SysMenuEntity, Serializable> {

    List<SysMenuEntity> findByParentId(int parentId);

    @NonNull
    @Query("SELECT e FROM SysMenuEntity e LEFT JOIN FETCH e.permissions")
    List<SysMenuEntity> findAll();

    @NonNull
    @Query("SELECT e FROM SysMenuEntity e LEFT JOIN FETCH e.permissions")
    List<SysMenuEntity> findAll(@NonNull Sort sort);

    @Query("""
            SELECT
                e
            FROM
                SysMenuEntity e
                LEFT JOIN FETCH e.permissions
            WHERE
                e.id IN :ids
                """)
    List<SysMenuEntity> findByIdIn(@Param("ids") Collection<Integer> ids);

    @Query("""
            SELECT
                e
            FROM
                SysMenuEntity e
                LEFT JOIN FETCH e.permissions
                LEFT JOIN e.roles r
            WHERE
                e.type = :type
                AND r.id = :roleId
                """)
    List<SysMenuEntity> findByTypeAndRoleId(@Param("type") Integer type, @Param("roleId") Integer roleId);

    @Query("""
            SELECT
                e
            FROM
                SysMenuEntity e
                LEFT JOIN FETCH e.permissions
                LEFT JOIN e.roles r
            WHERE
                e.type <> 2
                AND e.hidden = 0
                AND r.id IN :roleIds
            ORDER BY
                e.sortWeight ASC
                """)
    List<SysMenuEntity> findDisplayableNodesByRoleIdIn(@Param("roleIds") Collection<Integer> roleIds);

    @Query("""
            SELECT
                e
            FROM
                SysMenuEntity e
                LEFT JOIN FETCH e.permissions
                LEFT JOIN e.roles r
            WHERE
                e.type <> 2
                AND e.hidden = 0
            ORDER BY
                e.sortWeight ASC
                """)
    List<SysMenuEntity> findDisplayableNodes();
}
