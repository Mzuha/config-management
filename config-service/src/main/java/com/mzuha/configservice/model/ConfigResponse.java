package com.mzuha.configservice.model;

import java.time.LocalDateTime;

public record ConfigResponse(Long id, String application, String profile, String value,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
}