package com.example.oliveback.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotBlank(message = "상품명을 입력하세요.")
    private String productName;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;

    @NotNull(message = "총 가격을 입력하세요.")
    private int totalPrice;
}
