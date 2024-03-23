package tech.gaosong886.system.repository;

import tech.gaosong886.system.model.entity.SysPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermissionEntity, Serializable> {
    @Transactional
    @Modifying
    void deleteByNameIn(Collection<String> names);

    List<SysPermissionEntity> findByIdIn(Collection<Integer> ids);
}
