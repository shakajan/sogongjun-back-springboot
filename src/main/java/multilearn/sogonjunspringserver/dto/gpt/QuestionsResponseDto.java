package multilearn.sogonjunspringserver.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class QuestionsResponseDto {
    private Long questionId;
    private Answer answer;

    @Data
    @AllArgsConstructor
    static class Answer {
        private String text;
        private String image;
    }

    public QuestionsResponseDto(Long questionId, String text, String image) {
        this.questionId = questionId;
        this.answer = new Answer(text, image);
    }
}
