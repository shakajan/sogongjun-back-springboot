package multilearn.sogonjunspringserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class NFTResponseDto {
    private String message;
    private NFTDto nft;

    public NFTResponseDto(String message, String tokenId, String metadataUrl) {
        this.message = message;
        this.nft = new NFTDto(tokenId, metadataUrl);
    }

    @Data
    @AllArgsConstructor
    public static class NFTDto {
        private String tokenId;
        private String metadataUrl;
    }
}
