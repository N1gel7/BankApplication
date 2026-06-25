package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.bankapplication.entitiy.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
   Optional<User> findByEmail(String email);

   boolean existsByEmail(String email);
}
