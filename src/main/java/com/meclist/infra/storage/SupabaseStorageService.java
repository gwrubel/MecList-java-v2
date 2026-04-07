package com.meclist.infra.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meclist.config.SupabaseProperties;
import com.meclist.interfaces.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupabaseStorageService implements StorageService {

    private final SupabaseProperties properties;
    private final HttpClient client = HttpClient.newHttpClient();

    @Retryable(retryFor = { IOException.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @Override
    public String upload(byte[] file, String path, String contentType) {
        try {
            String fileName = UUID.randomUUID().toString();
            String fullPath = path + "/" + fileName;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(properties.getUrl()
                            + "/storage/v1/object/"
                            + properties.getBucket()
                            + "/"
                            + fullPath))
                    .header("Authorization", "Bearer " + properties.getServiceKey())
                    .header("Content-Type", contentType)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(file))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return fullPath;
            }

            throw new RuntimeException("Erro upload: " + response.body());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar arquivo", e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(properties.getUrl()
                            + "/storage/v1/object/"
                            + properties.getBucket()
                            + "/"
                            + path))
                    .header("Authorization", "Bearer " + properties.getServiceKey())
                    .DELETE()
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo", e);
        }
    }

    @Retryable(retryFor = { IOException.class, RuntimeException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @Override
    public String generateSignedUrl(String path, int expiresIn) {
        try {
            String body = """
                    {
                      "expiresIn": %d
                    }
                    """.formatted(expiresIn);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(properties.getUrl()
                            + "/storage/v1/object/sign/"
                            + properties.getBucket()
                            + "/"
                            + path))
                    .header("Authorization", "Bearer " + properties.getServiceKey())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(response.body());
                String signedPath = node.get("signedURL").asText();
                String baseUrl = properties.getUrl().replaceAll("/$", "");
                return baseUrl + "/storage/v1" + signedPath;
            }

            throw new RuntimeException("Erro signed URL: " + response.body());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar signed URL", e);
        }
    }
}
