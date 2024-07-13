package multilearn.sogonjunspringserver.dto.gpt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OpenaiImageRequestDto {
    private String model;
    private String prompt;

    public OpenaiImageRequestDto(String model, String prompt) {
        this.model = model;
        this.prompt = prompt;
    }
}
