package multilearn.sogonjunspringserver.service;


import jakarta.persistence.EntityNotFoundException;
import multilearn.sogonjunspringserver.domain.Answer;
import multilearn.sogonjunspringserver.domain.Question;
import multilearn.sogonjunspringserver.domain.User;
import multilearn.sogonjunspringserver.dto.NFTRequestDto;
import multilearn.sogonjunspringserver.dto.NFTResponseDto;
import multilearn.sogonjunspringserver.dto.gpt.*;
import multilearn.sogonjunspringserver.repository.AnswerRepository;
import multilearn.sogonjunspringserver.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Service
public class GPTService {
    @Value("${openai.text.model}")
    private String textModel;

    @Value("${openai.image.model}")
    private String imageModel;

    @Value("${openai.api.text.url}")
    private String textApiURL;

    @Value("${openai.api.image.url}")
    private String imageApiURL;

    @Value("${node.js.nft.url}")
    private String nodeJsURL;

    private final RestTemplate template;
    private final Logger logger = LoggerFactory.getLogger(GPTService.class);
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public GPTService(RestTemplate template, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.template = template;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }


    public QuestionsResponseDto getAnswer(QuestionsRequestDto requestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // save question
        User user = (User) auth.getPrincipal();
        Question question = new Question();
        question.setContent(requestDto.getContent());
        question.setUser(user);
        Question savedQuestion = questionRepository.save(question);
        logger.info("[GPTService] saved question: {}", savedQuestion);
        // get text with gpt api
        OpenaiTextRequestDto textRequest = new OpenaiTextRequestDto(textModel, requestDto.getContent() + "(answer less than 1000 byte)");
        logger.info("[GPTService] Request body for answer: {}", textRequest);
        OpenaiTextResponseDto textResponse =  template.postForObject(textApiURL, textRequest, OpenaiTextResponseDto.class);
        if(textResponse == null) {
            return null; // have to be determined by the convention of exception handling.
        }
        String text = textResponse.getChoices().get(0).getMessage().getContent();
        logger.info("[GPTService] saved text: {}", text);

        return new QuestionsResponseDto(savedQuestion.getId(), text, null);
    }

    public ImageResponseDto getImage(ImageRequestDto requestDto)
            throws HttpClientErrorException.NotFound,
            HttpClientErrorException.BadRequest,
            NullPointerException,
            DataIntegrityViolationException {
        String prompt = requestDto.getAnswerText();
        logger.info("[GPTService] client's prompt: {}", prompt);

        // get image with gpt api
        OpenaiImageRequestDto imageRequest = new OpenaiImageRequestDto(imageModel, requestDto.getAnswerText());
        logger.info("[GPTService] image request: {}", imageRequest);
        ResponseEntity<OpenaiImageResponseDto> imageResponse;
        String url;
        Question question;
        Answer answer;
        var opt = questionRepository.findById(requestDto.getQuestionId());
        if(opt.isEmpty()) {
            logger.error("[GPTService] question id is invalid. can't find question.");
            throw new EntityNotFoundException("question id is invalid");
        }
        imageResponse = template.postForEntity(imageApiURL, imageRequest, OpenaiImageResponseDto.class);
        url = Objects.requireNonNull(imageResponse.getBody()).getData().get(0).getUrl();
        logger.info("[GPTService] saved image length: {}", url.length());

        question = opt.get();
        answer = new Answer();
        answer.setQuestion(question);
        answer.setText(requestDto.getAnswerText());
        answer.setImageUrl(url);
        Answer savedAnswer = answerRepository.saveAndFlush(answer);
        logger.info("[GPTService] saved answer: {}", savedAnswer);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompletableFuture.runAsync(() -> {
            logger.info("[GPTService] start of posting nft data");
            logger.info("[GPTService] username: {}", user.getNickname());
            NFTRequestDto nftRequestDto = new NFTRequestDto(question.getId(),
                    question.getContent(),
                    answer.getText(),
                    user.getNationality(),
                    user.getGrade(),
                    url);
            logger.info("[GPTService] nft request: {}", nftRequestDto);
            try {
                ResponseEntity<NFTResponseDto> nftResponseDto = template.postForEntity(nodeJsURL + question.getId().toString(), nftRequestDto, NFTResponseDto.class);
                String message = Objects.requireNonNull(nftResponseDto.getBody()).getMessage();
                logger.info("[GPTService] response: {}", message);
            } catch(Exception e) {
                logger.error("[GPTService] error message: {}", e.getMessage());
            }
        });

        return new ImageResponseDto(requestDto.getQuestionId(), url);
    }
}
