package multilearn.sogonjunspringserver.repository;

import multilearn.sogonjunbackspringboot.domain.NFT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NFTRepository extends JpaRepository<NFT, Long> {
}
