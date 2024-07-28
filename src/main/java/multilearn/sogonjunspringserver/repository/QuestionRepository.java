package multilearn.sogonjunspringserver.repository;


import multilearn.sogonjunspringserver.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByUserId(Long userId);
}
