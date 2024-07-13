package multilearn.sogonjunspringserver.dto.gpt;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class QuestionsRequestDto {
    private int userId;
    private String content;
}
