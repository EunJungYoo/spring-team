package com.example.demo.service.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.domain.imageDomain.AwsS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AmazonS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public AwsS3 upload(MultipartFile multipartFile, String dirName) throws IOException {
        System.out.println("upload --> convertMultipartFileToFile");

        // convertMultipartFileToFile의 반환값은 Optional<File>이기 떄문에
        // null 값을 리턴하는 경우를 대비해 exception 처리!
        File file = convertMultipartFileToFile(multipartFile)
                .orElseThrow( ()-> new IllegalArgumentException("파일 변환 실패!"));
        System.out.println("file converted completely");

        System.out.println("AwsS3 Dto 객체생성");

        // 파일명에 디렉토리와 UUID를 붙여 새 파일명 생성
        String key = randomFileName(file, dirName);
        System.out.println("이게 key 값이다!!!!:    " + key);

        System.out.println("S3에 업로드 시작!");
        String path = putS3(file, key);

        System.out.println("Local 디렉토리 내에 생성했던 파일 삭제!");
        removeFile(file);

        return AwsS3
                .builder()
                .key(key)
                .path(path)
                .build();
    }

    /*
        S3 이미지 업로드하는 순서--------
        1. S3에 업로드하기 위해 MultipartFile -> File 객체로 변환 후, 현재 프로젝트에 경로 업로드
        2. 파일명에 UUID를 붙여 S3에 업로드 후, 이미지의 key값과 path를 반환
        3. key: 객체이름, path: 해당객체의 절대경로 값
        4. 현재 프로젝트 경로에 생성되었던 파일 제거
     */
    public Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        System.out.println("convert MultipartFile to File");

        // 일단 업로드된 파일을 프로젝트 내 폴더에 생성
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        System.out.println(file.getAbsolutePath());

        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    // 디렉토리와 UUID를 파일명에 붙여주는 메서드
    public String randomFileName(File file, String dirName) {
        String res = dirName + "/" + UUID.randomUUID() + file.getName();
        System.out.println("randomFileName = " + res);
        return res;
    }

    //S3에 file 업로드하는 메서드
    public String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return getS3(bucket, fileName);
    }

    //S3에 업로드 된 파일을 가져오는 메서드
    public String getS3(String bucket, String fileName) {
        String result = amazonS3.getUrl(bucket, fileName).toString();
        System.out.println("S3에서 가져온 파일명 = " + result);
        return result;
    }




    public void removeFile(File file) {
        file.delete();
    }



    public void remove(AwsS3 awsS3) {
        if(!amazonS3.doesObjectExist(bucket, awsS3.getKey())) {
            throw new AmazonS3Exception("Object " + awsS3.getKey() + " does not exist!");
        }
        amazonS3.deleteObject(bucket, awsS3.getKey());
    }
}
