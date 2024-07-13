package multilearn.sogonjunspringserver.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
public class ImageResponseDto {
    private Long questionId;
    private String image;
}
