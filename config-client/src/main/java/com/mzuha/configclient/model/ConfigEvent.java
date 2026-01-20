package com.mzuha.configclient.model;

import java.time.LocalDateTime;

public record ConfigEvent(String application,
                          String profile,
                          String configKey,
                          String configValue,
                          LocalDateTime timestamp) {
}