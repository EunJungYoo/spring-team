package com.example.demo.service.fileUpload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.demo.domain.Post;
import com.example.demo.domain.imageDomain.AwsS3;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AmazonS3Service {

    private final AmazonS3 amazonS3;
    private final PostRepository postRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public AwsS3 upload(MultipartFile multipartFile, String dirName) throws IOException {

        // convertMultipartFileToFile의 반환값은 Optional<File>이기 떄문에
        // null 값을 리턴하는 경우를 대비해 exception 처리!
        File file = convertMultipartFileToFile(multipartFile)
                .orElseThrow( ()-> new IllegalArgumentException("파일 변환 실패!"));

        // 파일명에 디렉토리와 UUID를 붙여 새 파일명 생성
        String key = randomFileName(file, dirName);

        //새 파일명을 가지고 S3에 업로드
        String path = putS3(file, key);

        // 로컬 디렉토리에 생성된 파일 삭제 메서드
        file.delete();

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
        // 일단 업로드된 파일을 프로젝트 내 폴더에 생성
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());

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
        return dirName + "/" + UUID.randomUUID() + file.getName();
    }

    //S3에 file 업로드하는 메서드
    public String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return getS3(bucket, fileName);
    }

    //S3에 업로드 된 파일을 가져오는 메서드
    public String getS3(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }



    /*
        매일 새벽1시에 사용되고 있지않은 이미지 파일을 S3저장소에서 제거해주는 스케줄러

        <순서>
        1. S3에 "upload" 디렉토리에 저장된 모든 파일리스트 불러오기
        2. PostRepository에 저장된 모든 게시글 리스트 불러오기
        3. S3 파일리스트에서 파일하나씩을 가져와 게시글 리스트에 imageUrl과 비교
            -> 만약, 게시글리스트에 imageUrl과 같은 파일이 S3에 없다면 이 파일은 삭제!
            -> 현재 사용중이지 않다는 증거이기 때문
     */
    @Scheduled(cron = "0 0 1 * * *") // 오전 1시
    public void remove() throws InterruptedException {

        //1단계: S3에 저장된 모든 파일리스트 불러오기
        ObjectListing objectListing = amazonS3.listObjects(bucket, "upload");

        //2단계: 모든 게시글 리스트 불러오기
        List<Post> allPosts = postRepository.findAll();

        //3단계: S3에 저장된 파일을 하나씩 모든게시글의 imageUrl과 비교
        for (S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()) {
            boolean flag = false;
            for (int i = 0; i < allPosts.size(); i++) {
                TimeUnit.SECONDS.sleep(1);
                String imageUrl = allPosts.get(i).getImageUrl();

                //게시글 imageUrl이 S3의 파일과 같은 것이 있다면 현재 사용중인 이미지이므로 삭제할 필요X
                if(imageUrl.contains(URLEncoder.encode(s3ObjectSummary.getKey(), StandardCharsets.US_ASCII))){
                    flag = true;
                    break;
                }
            }

            //모든 게시글과 비교했지만 같은 파일명이 없다면, 현재 사용중인 이미지가 아닌것이므로 S3에서 삭제
            if(!flag) {
                amazonS3.deleteObject(bucket,s3ObjectSummary.getKey());
            }
        }
    }
}


