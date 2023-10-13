package tr.com.htr.intern.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import tr.com.htr.intern.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, Long>, JpaSpecificationExecutor<User> {
    Boolean existsByUsername(String username);

    User findByUsername(String username);
    //List<User> findUsersByPermissionsId(Long id);


}
