package com.mzuha.configservice.controller;

import com.mzuha.configservice.model.ConfigRequest;
import com.mzuha.configservice.model.ConfigResponse;
import com.mzuha.configservice.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@Tag(name = "Configuration Management", description = "APIs for managing application configurations")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping()
    @Operation(summary = "Create a new configuration", description = "Creates a new configuration for an application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid configuration request")
    })
    public ResponseEntity<ConfigResponse> create(@RequestBody @Valid ConfigRequest configRequest) {
        ConfigResponse savedConfig = configService.save(configRequest);
        return savedConfig != null
                ? ResponseEntity.ok(savedConfig)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping()
    @Operation(summary = "Get all configurations for application", description = "Retrieves all configurations for a specific application and profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configurations retrieved successfully")
    })
    public ResponseEntity<Set<ConfigResponse>> getAllForApplication(
            @Parameter(description = "Application name", required = true) @RequestParam String application,
            @Parameter(description = "Profile name", required = true) @RequestParam String profile) {
        Set<ConfigResponse> configsByAppAndProfile = configService.getConfigsByAppAndProfile(application, profile);
        return ResponseEntity.ok(configsByAppAndProfile);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get configuration by ID", description = "Retrieves a specific configuration by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration found and returned"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigResponse> getById(
            @Parameter(description = "Configuration ID", required = true) @PathVariable Long id) {
        ConfigResponse configByID = configService.getById(id);
        return ResponseEntity.ok(configByID);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update configuration", description = "Updates an existing configuration by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration updated successfully"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigResponse> update(
            @Parameter(description = "Configuration ID", required = true) @PathVariable Long id,
            @RequestBody ConfigRequest configRequest) {
        ConfigResponse updatedConfig = configService.update(id, configRequest);
        return ResponseEntity.ok(updatedConfig);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete configuration", description = "Deletes a configuration by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuration deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Configuration not found")
    })
    public ResponseEntity<ConfigResponse> delete(
            @Parameter(description = "Configuration ID", required = true) @PathVariable Long id) {
        configService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
