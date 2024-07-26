package multilearn.sogonjunspringserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "answers")
@Getter
@Setter
@ToString
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(length = 1000)
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;
}