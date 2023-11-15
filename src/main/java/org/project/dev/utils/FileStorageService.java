package org.project.dev.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    /*
    Todo
     1. code.aiaru@gmail.com
     2. 파일(이미지) 업로드 처리를 위한 클래스입니다.
        파일(이미지) 처리 부분은 여러 곳에 쓰일 것 같아서 재사용 가능하게 만들었습니다.
     3. x
     4. 해당 메소드들을 사용하시려면 다음과 같이 하셔야 합니다.
        1) 파일(이미지) 저장 경로는 yml에 기록하고, @Value로 저장 경로를 불러오도록 합니다.
        2) ProductUtilService 클래스의 saveProductImages 메소드를 참고하여 fileType을 전달합니다.
        3) String uploadDir; 부분의 else if 로직을 추가합니다.
     */

    // yml에 기록한 파일 저장 경로를 불러옵니다.
    // 만약 파일 저장 경로를 추가하고 싶다면, yml에 추가후 이곳에 같은 형식으로 작성하시면 됩니다.
    @Value("${file.productImgUploadDir}")
    private String productImgUploadDir;

    @Value("${file.reviewImgUploadDir}")
    private String reviewImgUploadDir;


    // 첨부된 파일의 고유한 이름을 생성하기 위한 메소드 (이름이 중복 되지 않도록)
    public String generateFileName(MultipartFile productImages) {
        String originalName = productImages.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        return uuid + extension;
    }

    // 첨부된 파일을 로컬저장소에 저장하기 위한 메소드
    public String storeFile(String fileType, MultipartFile productImages) throws IOException {

        if (productImages.isEmpty()) {
            throw new RuntimeException("첨부된 파일이 없습니다.");
        }

        String uploadDir;

        if ("product".equals(fileType)) {
            uploadDir = productImgUploadDir;
        } else if ("review".equals(fileType)) {
            uploadDir = reviewImgUploadDir;

            // 만약 파일 저장 경로를 추가하려면, 이곳에 else if를 사용해서 위와 동일한 로직을 만드셔야 합니다.

        } else {
            throw new RuntimeException("유효한 파일 형식이 아닙니다.");
        }

        String fileName = generateFileName(productImages);
        String filePath = uploadDir + fileName;

        productImages.transferTo(new File(filePath));

        return filePath;
    }
}
