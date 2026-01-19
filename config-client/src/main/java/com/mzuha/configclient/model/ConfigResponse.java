package com.mzuha.configclient.model;

public record ConfigResponse(Long id, String application, String profile, String value) {
}