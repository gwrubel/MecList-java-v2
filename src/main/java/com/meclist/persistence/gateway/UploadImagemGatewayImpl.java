package com.meclist.persistence.gateway;

import java.text.Normalizer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.meclist.config.SupabaseProperties;
import com.meclist.interfaces.UploadImagemGateway;

@Component
public class UploadImagemGatewayImpl implements UploadImagemGateway {

    private static final Logger log = LoggerFactory.getLogger(UploadImagemGatewayImpl.class);

    private final SupabaseProperties properties;
    private final HttpClient client = HttpClient.newHttpClient();

    public UploadImagemGatewayImpl(SupabaseProperties properties) {
        this.properties = properties;
    }

    @Override
    public String upload(byte[] imagem, String caminhoRelativoComNome, String contentType) {
        try {
            String normalizedPath = sanitizarPathParaStorage(caminhoRelativoComNome);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrlSemBarra()
                            + "/storage/v1/object/"
                            + properties.getBucketItensPublic()
                            + "/"
                            + normalizedPath))
                    .header("Authorization", "Bearer " + properties.getServiceKey())
                    .header("Content-Type", contentType)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(imagem))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return construirUrlPublica(normalizedPath);
            }

            throw new RuntimeException("Erro ao enviar imagem para Supabase: " + response.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar imagem para Supabase", e);
        }
    }

    @Override
    public void delete(String imagemReferencia) {
        String path = extrairPathStorage(imagemReferencia);
        if (path == null || path.isBlank()) {
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrlSemBarra()
                            + "/storage/v1/object/"
                            + properties.getBucketItensPublic()
                            + "/"
                            + path))
                    .header("Authorization", "Bearer " + properties.getServiceKey())
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 404 pode acontecer se o arquivo já foi removido em uma operação anterior.
            if (response.statusCode() == 404) {
                log.debug("Imagem já não existe no bucket de itens: {}", path);
                return;
            }

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Erro ao deletar imagem no Supabase: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar imagem no Supabase", e);
        }
    }

    private String extrairPathStorage(String imagemReferencia) {
        if (imagemReferencia == null || imagemReferencia.isBlank()) {
            return null;
        }

        String valor = imagemReferencia.trim();

        String prefixoPublico = baseUrlSemBarra()
                + "/storage/v1/object/public/"
                + properties.getBucketItensPublic()
                + "/";

        if (valor.startsWith(prefixoPublico)) {
            return valor.substring(prefixoPublico.length());
        }

        String marcador = "/storage/v1/object/public/"
                + properties.getBucketItensPublic()
                + "/";
        int idx = valor.indexOf(marcador);
        if (idx >= 0) {
            return valor.substring(idx + marcador.length());
        }

        return normalizarPath(valor);
    }

    private String construirUrlPublica(String path) {
        return baseUrlSemBarra()
                + "/storage/v1/object/public/"
                + properties.getBucketItensPublic()
                + "/"
                + path;
    }

    private String baseUrlSemBarra() {
        return properties.getUrl().replaceAll("/$", "");
    }

    private String normalizarPath(String caminhoRelativoComNome) {
        return caminhoRelativoComNome.replace('\\', '/');
    }

    private String sanitizarPathParaStorage(String path) {
        String normalizedPath = normalizarPath(path).trim();
        String sanitized = Arrays.stream(normalizedPath.split("/"))
                .map(this::sanitizarSegmento)
                .collect(Collectors.joining("/"));

        return sanitized.replaceAll("/+", "/");
    }

    private String sanitizarSegmento(String segmento) {
        if (segmento == null || segmento.isBlank()) {
            return "arquivo";
        }

        String semAcentos = Normalizer.normalize(segmento, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");

        String limpo = semAcentos
                .replaceAll("[^a-zA-Z0-9._-]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_+|_+$", "");

        return limpo.isBlank() ? "arquivo" : limpo;
    }
}



