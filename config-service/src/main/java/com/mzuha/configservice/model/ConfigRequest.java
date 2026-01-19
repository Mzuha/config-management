package com.mzuha.configservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfigRequest(
        @NotBlank(message = "Application name is required")
        @Size(min = 2, max = 50)
        String application,

        @NotBlank(message = "Profile is required")
        String profile,

        @NotBlank(message = "Key is required")
        String key,

        @NotBlank(message = "Value is required")
        String value
) {
}
