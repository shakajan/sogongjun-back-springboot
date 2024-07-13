package multilearn.sogonjunspringserver.dto.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Getter
@AllArgsConstructor
public class OpenaiImageResponseDto {
    private Long created;
    private List<ImageUrl> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUrl {
        private String url;
    }

}
