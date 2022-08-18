// package com.sparta.doing.service;
//
// import com.amazonaws.services.s3.AmazonS3Client;
// import com.amazonaws.services.s3.model.CannedAccessControlList;
// import com.amazonaws.services.s3.model.PutObjectRequest;
// import com.sparta.doing.controller.dto.S3Dto;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.io.FilenameUtils;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
//
// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.time.LocalDateTime;
// import java.util.Objects;
// import java.util.Optional;
//
// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class S3Service {
//     private final AmazonS3Client amazonS3Client;
//
//     @Value("${cloud.aws.s3.bucket}")
//     private String bucket;
//
//     public S3Dto S3ImageUpload(MultipartFile file, Long userId) throws IOException {
//
//         File uploadFile = convert(file).orElseThrow(() -> new IllegalArgumentException("파일 업로드에 실패하였습니다."));
//
//         String dir = "static/images/".concat(userId.toString());
//
//         return upload(uploadFile, dir);
//     }
//
//     private S3Dto upload(File uploadFile, String dir) {
//         String sourceName = uploadFile.getName();
//         String sourceExt = FilenameUtils.getExtension(sourceName).toLowerCase();
//
//         String fileName = dir + "/" + LocalDateTime.now().toString().concat(".").concat(sourceExt);
//         String uploadImageUrl = putS3(uploadFile, fileName);
//         removeNewFile(uploadFile);
//
//         return S3Dto.builder()
//                 .url(uploadImageUrl)
//                 .build();
//     }
//
//     private void removeNewFile(File targetFile) {
//         if (targetFile.delete()) {
//             log.info("파일이 삭제되었습니다.");
//         } else {
//             log.info("파일이 삭제되지 못했습니다.");
//         }
//     }
//
//     private String putS3(File uploadFile, String fileName) {
//         try {
//             amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
//                     .withCannedAcl(CannedAccessControlList.PublicRead));
//         } catch (Exception e) {
//             log.error("이미지 s3 업로드 실패");
//             log.error(e.getMessage());
//             removeNewFile(uploadFile);
//             throw new RuntimeException();
//         }
//
//         return amazonS3Client.getUrl(bucket, fileName).toString();
//     }
//
//     private Optional<File> convert(MultipartFile file) throws IOException {
//         File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
//         if (convertFile.createNewFile()) {
//             try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                 fos.write(file.getBytes());
//             }
//
//             return Optional.of(convertFile);
//         }
//
//         return Optional.empty();
//     }
// }
