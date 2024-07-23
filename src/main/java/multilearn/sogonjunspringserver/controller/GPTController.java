package multilearn.sogonjunspringserver.controller;

import multilearn.sogonjunspringserver.dto.gpt.QuestionsRequestDto;
import multilearn.sogonjunspringserver.dto.gpt.QuestionsResponseDto;
import multilearn.sogonjunspringserver.dto.gpt.ImageResponseDto;
import multilearn.sogonjunspringserver.dto.gpt.ImageRequestDto;
import multilearn.sogonjunspringserver.service.GPTService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GPTController {
    private final GPTService gptService;

    public GPTController(GPTService gptService) {
        this.gptService = gptService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/questions")
    public QuestionsResponseDto answer(@RequestBody QuestionsRequestDto requestDto){
        return gptService.getAnswer(requestDto);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api/image")
    public ImageResponseDto image(@RequestBody ImageRequestDto requestDto){
        return gptService.getImage(requestDto);
    }
}