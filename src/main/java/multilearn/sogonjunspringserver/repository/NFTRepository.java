package multilearn.sogonjunspringserver.repository;

import jakarta.transaction.Transactional;
import multilearn.sogonjunspringserver.domain.NFT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NFTRepository extends JpaRepository<NFT, Long> {
    @Transactional
    void deleteByAnswerId(Long answerId);
}
