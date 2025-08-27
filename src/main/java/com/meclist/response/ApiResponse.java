package com.meclist.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    private T data;

    public ApiResponse(int status, String message, String path, T data) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, String path, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, path, data);
    }

    public static <T> ApiResponse<T> created(String message, String path, T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), message, path, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, String path) {
        return new ApiResponse<>(status.value(), message, path, null);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    
}
