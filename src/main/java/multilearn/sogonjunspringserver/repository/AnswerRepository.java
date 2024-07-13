package multilearn.sogonjunspringserver.repository;


import multilearn.sogonjunspringserver.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
