package com.mdt.backend.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileSearchException.class)
    public ResponseEntity<String> handleNotFoundFilePathException() {
        return ResponseEntity.status(500).body("파일 검색 중 오류가 발생하였습니다!");
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<String> handleFileDownloadException() {
        return ResponseEntity.status(500).body("파일 조회 중 오류가 발생하였습니다!");
    }

}
