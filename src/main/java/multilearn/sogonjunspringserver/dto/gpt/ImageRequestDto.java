package multilearn.sogonjunspringserver.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class ImageRequestDto {
    private Long questionId;
    private String answerText;
}
