package com.mzuha.configservice.model;

public record ConfigResponse(Long id, String application, String profile, String value) {
}