package org.bookwoori.core.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.global.exception.CustomException;
import org.bookwoori.core.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3 amazonS3;
    private static final Set<String> ALLOWED_FILE_EXTENSION = Set.of("jpg", "jpeg", "png");

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile, String dirName) {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        validateFileExtension(multipartFile);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        String fileName = dirName + "/" + UUID.randomUUID().toString() + "_"
            + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void validateFileExtension(MultipartFile multipartFile) {

        String fileName = multipartFile.getOriginalFilename();

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new CustomException(ErrorCode.INVALID_FILE_FORMAT);
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();

        if (!ALLOWED_FILE_EXTENSION.contains(extension)) {
            throw new CustomException(ErrorCode.UNSUPPORTED_FILE_FORMAT);
        }
    }

}
