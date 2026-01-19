package com.mzuha.configservice.model;

public record ConfigRequest(String application, String profile, String key, String value) {
}
