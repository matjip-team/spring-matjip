package com.restaurant.matjip.data.service;

import com.restaurant.matjip.data.dto.RestaurantLicensePresignRequest;
import com.restaurant.matjip.data.dto.RestaurantLicensePresignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantLicenseFileService {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public RestaurantLicensePresignResponse createPresignedUpload(RestaurantLicensePresignRequest request) {
        String extension = extractExtension(request.getFileName());
        String key = "tmp/licenses/" + UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(request.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        String uploadUrl = s3Presigner.presignPutObject(presignRequest).url().toString();
        return new RestaurantLicensePresignResponse(uploadUrl, key, "PUT");
    }

    public String createPresignedViewUrl(String fileKey) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        GetObjectPresignRequest getPresignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return s3Presigner.presignGetObject(getPresignRequest).url().toString();
    }

    public void deleteObject(String fileKey) {
        if (fileKey == null || fileKey.isBlank()) {
            return;
        }

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    private String extractExtension(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "";
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return "";
        }

        String extension = fileName.substring(dotIndex).toLowerCase(Locale.ROOT);
        return extension.length() > 10 ? "" : extension;
    }
}
