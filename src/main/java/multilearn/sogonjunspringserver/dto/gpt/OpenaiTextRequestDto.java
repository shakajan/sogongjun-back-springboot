package multilearn.sogonjunspringserver.dto.gpt;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OpenaiTextRequestDto {
    private String model;
    private List<Message> Messages;
    private Long max_tokens;

    public OpenaiTextRequestDto(String model, String prompt) {
        this.model = model;
        this.Messages =  new ArrayList<>();
        this.Messages.add(new Message("user", prompt));
        this.setMax_tokens(1000L);
    }
}