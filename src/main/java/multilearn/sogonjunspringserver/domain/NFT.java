package multilearn.sogonjunspringserver.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nfts")
@Getter
@Setter
public class NFT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String tokenId;

    @Column(length = 255)
    private String metadataUrl;

    @OneToOne
    @JoinColumn(name = "answerId", referencedColumnName = "id")
    private Answer answer;

    // Getters and Setters
}
