package com.example.shop.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/uploads")
public class FileController {

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 한글 등 유니코드 파일명 인코딩 (RFC 5987)
                String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8)
                        .replaceAll("\\+", "%20"); // 공백 처리

                String contentDisposition = "inline; filename*=UTF-8''" + encodedFilename;

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                        .body(resource);
            } else {
                return ResponseEntity.status(404).body(null); // 파일 없음 처리
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 서버 오류 처리
        }
    }
}
