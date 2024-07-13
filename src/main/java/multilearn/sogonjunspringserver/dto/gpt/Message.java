package multilearn.sogonjunspringserver.dto.gpt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// using class name "Message" since request, response body of chatGPT api require "Message": {...} format.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}