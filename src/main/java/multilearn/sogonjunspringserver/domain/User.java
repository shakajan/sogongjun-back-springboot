package multilearn.sogonjunspringserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(nullable = false, length = 100)
    private String nationality;

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false, length = 255)
    private String password;

    // Getters and Setters
}
