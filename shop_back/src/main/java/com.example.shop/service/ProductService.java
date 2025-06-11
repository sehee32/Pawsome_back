package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final Path uploadDir = Paths.get("uploads"); // 이미지 저장 경로

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;

        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir); // 업로드 디렉토리 생성
            }
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
    }

    public Product addProduct(String name, String description, int price, MultipartFile file) throws IOException {
        String imageUrl = saveFile(file);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImageUrl("/uploads/" + imageUrl);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, String name, String description, int price, MultipartFile file) throws IOException {
        Product existingProduct = getProductById(id);

        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);

        if (file != null && !file.isEmpty()) {
            String imageUrl = saveFile(file); // 새 이미지 저장
            existingProduct.setImageUrl(imageUrl);
        }

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("삭제할 상품을 찾을 수 없습니다. ID: " + id);
        }
        productRepository.deleteById(id);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);
        System.out.println("저장된 파일 경로: " + filePath);

        if (!Files.exists(filePath)) {
            throw new IOException("파일 저장에 실패했습니다: " + filePath);
        }

        return fileName;
    }

}
