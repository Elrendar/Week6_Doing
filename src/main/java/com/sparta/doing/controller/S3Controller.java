package com.sparta.doing.controller;

import com.sparta.doing.controller.dto.S3Dto;
import com.sparta.doing.service.S3Service;
import com.sparta.doing.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class S3Controller {
    private final S3Service s3Service;

    // 마이페이지에서 유저 정보 수정 시 유저의 사진 작성 또는 수정 가능.
    @PostMapping("/users/image/upload")
    public String S3UserImageUpload(@RequestPart MultipartFile file) throws IOException {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        this.s3Service.S3ImageUpload(file, userId);
        return "redirect:/users/mypage";
    }

    // 게시판 수정 시 썸네일 이미지 작성 또는 수정 가능.
    @PostMapping("/boards/image/upload")
    public String S3BoardImageUpload(@RequestPart MultipartFile file) throws IOException {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        this.s3Service.S3ImageUpload(file, userId);
        return "redirect:/boards/{boardId}";
    }
}
