package test.bankapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.bankapplication.entitiy.User;

public interface UserRepository extends JpaRepository<User,Integer> {
}
