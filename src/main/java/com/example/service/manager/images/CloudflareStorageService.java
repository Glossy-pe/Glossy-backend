package com.example.service.manager.images;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
public class CloudflareStorageService {

    private final S3AsyncClient r2Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    public CloudflareStorageService(S3AsyncClient r2Client) {
        this.r2Client = r2Client;
    }

    public Mono<String> upload(FilePart filePart, String folder) {
        String extension = extractExtension(filePart.filename());
        String key = folder + "/" + UUID.randomUUID() + extension;
        String contentType = filePart.headers().getContentType() != null
                ? filePart.headers().getContentType().toString()
                : "application/octet-stream";

        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    PutObjectRequest request = PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(contentType)
                            .build();

                    return Mono.fromFuture(
                            r2Client.putObject(request, AsyncRequestBody.fromBytes(bytes))
                    ).thenReturn(publicUrl + "/" + key);
                });
    }

    public Mono<Void> delete(String url) {
        String key = url.replace(publicUrl + "/", "");

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return Mono.fromFuture(r2Client.deleteObject(request)).then();
    }

    private String extractExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        return dotIndex != -1 ? filename.substring(dotIndex) : "";
    }
}