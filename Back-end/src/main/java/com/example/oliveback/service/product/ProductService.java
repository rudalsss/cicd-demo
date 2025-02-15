package com.example.oliveback.service.product;

import com.example.oliveback.domain.product.Product;
import com.example.oliveback.domain.user.Role;
import com.example.oliveback.domain.user.Users;
import com.example.oliveback.dto.product.ProductRequest;
import com.example.oliveback.dto.product.ProductResponse;
import com.example.oliveback.exception.CustomException;
import com.example.oliveback.repository.product.ProductRepository;
import com.example.oliveback.repository.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(int page, int size, String category) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Product> products;

        if (category != null && !category.isEmpty()) {
            products = productRepository.findByCategory(category, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public ProductResponse getProductById(Long productId) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
//        return ProductResponse.fromEntity(product);
//    }
//
//    @Transactional
//    public ProductResponse createProduct(String username, ProductRequest request) {
//        //관리자 상품등록
////        Users user = usersRepository.findByUsername(username)
////                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
////
////        if (user.getRole() != Role.ADMIN) {
////            throw new IllegalArgumentException("관리자만 상품을 등록할 수 있습니다.");
////        }
//
//        Product product = Product.builder()
//                .name(request.getName())
//                .price(request.getPrice())
//                .category(request.getCategory())
//                .description(request.getDescription())
//                .imageUrl(request.getImageUrl())
//                .build();
//
//        productRepository.save(product);
//        return ProductResponse.fromEntity(product);
//    }
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(CustomException.ProductNotFoundException::new);
        return ProductResponse.fromEntity(product);
    }


    @Transactional
    public ProductResponse createProduct(String username, ProductRequest request) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(CustomException.UserNotFoundException::new);

        if (user.getRole() != Role.ADMIN) {
            throw new CustomException.AccessDeniedException();
        }

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .category(request.getCategory())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        productRepository.save(product);
        return ProductResponse.fromEntity(product);
    }

    //카테고리 별 상품 조회
    public List<ProductResponse> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);

        return products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getDescription(),
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
