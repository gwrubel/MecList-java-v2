package com.meclist.interfaces;

public interface StorageService {
    
    String upload(byte[] file, String path, String contentType);

    void delete(String path);

    String generateSignedUrl(String path, int expiresIn);
}