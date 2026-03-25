package com.flowmatic.flowmatic_back.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private Map<String, Object> buildError(int status, String error, String message) {
    Map<String, Object> body = new HashMap<>();
    body.put("status", status);
    body.put("error", error);
    body.put("message", message);
    body.put("timestamp", LocalDateTime.now());
    return body;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(buildError(404, "Not Found", ex.getMessage()));
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(buildError(403, "Forbidden", ex.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(buildError(400, "Bad Request", ex.getMessage()));
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(buildError(409, "Conflict", ex.getMessage()));
  }

  @ExceptionHandler(InvalidStatusTransitionException.class)
  public ResponseEntity<Map<String, Object>> handleInvalidStatus(InvalidStatusTransitionException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(buildError(400, "Bad Request", ex.getMessage()));
  }

  @ExceptionHandler(ExtractionFailedException.class)
  public ResponseEntity<Map<String, Object>> handleExtraction(ExtractionFailedException ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(buildError(502, "Extraction Failed", ex.getMessage()));
  }

  @ExceptionHandler(CloudinaryUploadException.class)
  public ResponseEntity<Map<String, Object>> handleCloudinary(CloudinaryUploadException ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(buildError(502, "Upload Failed", ex.getMessage()));
  }

  @ExceptionHandler(ImageSearchException.class)
  public ResponseEntity<Map<String, Object>> handleImageSearch(ImageSearchException ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(buildError(502, "Image Search Failed", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(buildError(400, "Validation Error", message));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(buildError(500, "Internal Server Error", "Une erreur inattendue s'est produite."));
  }
}
