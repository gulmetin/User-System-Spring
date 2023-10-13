package tr.com.htr.intern.userservice.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import tr.com.htr.intern.userservice.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepositoryImplementation<Permission, Long> {
    Boolean existsByname(String permissionName);
    Permission findByName(String permissionName);
    //List<Permission> findPermissionsByUsersId(Long id);
}
