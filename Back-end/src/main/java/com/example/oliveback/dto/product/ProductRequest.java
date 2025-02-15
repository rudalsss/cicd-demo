package com.example.oliveback.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "상품명을 입력하세요.")
    private String name;

    @NotNull(message = "가격을 입력하세요.")
    @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "카테고리를 입력하세요.")
    private String category;

    @NotBlank(message = "상품 설명을 입력하세요.")
    private String description;

    @NotBlank(message = "이미지 URL을 입력하세요.")
    private String imageUrl;
}

