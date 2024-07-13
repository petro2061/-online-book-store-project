package project.onlinebookstore.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.onlinebookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
}
