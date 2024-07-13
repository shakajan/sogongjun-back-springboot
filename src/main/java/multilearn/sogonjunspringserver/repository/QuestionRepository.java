package multilearn.sogonjunspringserver.repository;


import multilearn.sogonjunspringserver.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
