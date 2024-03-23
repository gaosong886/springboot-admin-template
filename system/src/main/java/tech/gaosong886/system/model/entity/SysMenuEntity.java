package tech.gaosong886.system.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sys_menu")
public class SysMenuEntity implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "name")
        private String name;

        @Column(name = "type")
        private Integer type;

        @Column(name = "icon")
        private String icon;

        @Column(name = "parent_id")
        private Integer parentId;

        @Column(name = "path")
        private String path;

        @Column(name = "sort_weight")
        private Integer sortWeight;

        @Column(name = "hidden")
        private Integer hidden;

        @CreatedDate
        @Column(name = "create_at")
        private LocalDateTime createAt;

        @LastModifiedDate
        @Column(name = "update_at")
        private LocalDateTime updateAt;

        @ManyToMany(targetEntity = SysPermissionEntity.class, fetch = FetchType.LAZY)
        @JoinTable(name = "sys_menu_permission", joinColumns = {
                        @JoinColumn(name = "menu_id", referencedColumnName = "id") }, inverseJoinColumns = {
                                        @JoinColumn(name = "permission_id", referencedColumnName = "id") })
        private List<SysPermissionEntity> permissions;

        @ManyToMany(targetEntity = SysRoleEntity.class, fetch = FetchType.LAZY)
        @JoinTable(name = "sys_role_menu", joinColumns = {
                        @JoinColumn(name = "menu_id", referencedColumnName = "id") }, inverseJoinColumns = {
                                        @JoinColumn(name = "role_id", referencedColumnName = "id") })
        private List<SysRoleEntity> roles;
}
