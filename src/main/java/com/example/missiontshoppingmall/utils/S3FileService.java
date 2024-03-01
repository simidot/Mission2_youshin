package com.example.missiontshoppingmall.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3FileService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3에 업로드하기
    @Transactional
    public List<String> uploadIntoS3(String folder, List<MultipartFile> multipartFileList) {

        List<String> imgUrlList = new ArrayList<>();
        // forEach구문을 통해 multipartFile로 넘어온 파일들을 하나씩 fileNameList에 추가
        for (MultipartFile file : multipartFileList) {
            String originalFileName = file.getOriginalFilename();
            // 업로드 파일의 확장자명이 올바른지 확인
            if (!this.checkFileExtension(originalFileName)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "업로드 이미지 확장자명 불일치");
            }
            // 업로드될 이미지 파일의 고유한 파일 이름 생성하기 (UUID)
            String uploadFileName = UUID.randomUUID().toString().concat(this.getFileExtension(originalFileName));
            log.info("uploadFilename:: "+uploadFileName);

            // 객체의 metadata 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            log.info("content type: "+file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                // S3에 Object로 넣어준다.
                amazonS3.putObject(
                        new PutObjectRequest(bucket + folder, // 버킷이름
                                uploadFileName, // 제목
                                inputStream, //InputStream
                                metadata)// ObjectMetadata
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );

                // 성공적으로 업로드되면 imgUrlList에 추가
                imgUrlList.add(amazonS3.getUrl(bucket + folder, uploadFileName).toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return imgUrlList;
    }


    // 파일이름에서 확장명 추출 메서드
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "적절한 이미지가 아닙니다.");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    // 확장명이 올바른지 확인하는 메서드
    private boolean checkFileExtension(String fileName) {
        List<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".gif");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        fileValidate.add(".GIF");

        for (String validate : fileValidate) {
            if (this.getFileExtension(fileName).equals(validate)) {
                return true;
            }
        }
        log.info("file Extension: "+this.getFileExtension(fileName));
        return false;
    }

    // S3에서 삭제하기
    public void deleteImage(String folder, String originalFileName) {
        log.info("삭제할 거에요: " + bucket + folder);
        log.info("삭제할 originalFileName: " + originalFileName);
        amazonS3.deleteObject(bucket + folder, originalFileName);
    }
}

