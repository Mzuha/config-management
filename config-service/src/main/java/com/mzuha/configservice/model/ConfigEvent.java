package com.mzuha.configservice.model;

import java.time.LocalDateTime;

public record ConfigEvent(String application,
                          String profile,
                          String configKey,
                          String configValue,
                          LocalDateTime timestamp) {
}