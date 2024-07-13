package multilearn.sogonjunspringserver.repository;


import multilearn.sogonjunspringserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByNickname(String nickname);
}