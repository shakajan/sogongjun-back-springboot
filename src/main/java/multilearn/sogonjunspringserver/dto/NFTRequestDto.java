package multilearn.sogonjunspringserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NFTRequestDto {
    private Long questionId;
    private String questionContent;
    private String answerContent;
    private String nationality;
    private Integer grade;
    private String imageUrl;
}
