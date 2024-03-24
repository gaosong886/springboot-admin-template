package tech.gaosong886.system.repository;

import tech.gaosong886.system.model.entity.SysRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRoleEntity, Serializable> {
    @SuppressWarnings("null")
    @Query("SELECT e FROM SysRoleEntity e LEFT JOIN FETCH e.menus")
    List<SysRoleEntity> findAll();

    List<SysRoleEntity> findByIdIn(Collection<Integer> ids);
}
