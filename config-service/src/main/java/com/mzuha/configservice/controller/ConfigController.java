package com.mzuha.configservice.controller;

import com.mzuha.configservice.model.ConfigRequest;
import com.mzuha.configservice.model.ConfigResponse;
import com.mzuha.configservice.service.ConfigService;
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
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping()
    public ResponseEntity<ConfigResponse> create(@RequestBody ConfigRequest configRequest) {
        ConfigResponse savedConfig = configService.save(configRequest);
        return savedConfig != null
                ? ResponseEntity.ok(savedConfig)
                : ResponseEntity.badRequest().build();
    }

    @GetMapping()
    public ResponseEntity<Set<ConfigResponse>> getAllForApplication(@RequestParam String application, @RequestParam String profile) {
        Set<ConfigResponse> configsByAppAndProfile = configService.getConfigsByAppAndProfile(application, profile);
        return ResponseEntity.ok(configsByAppAndProfile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfigResponse> getById(@PathVariable Long id) {
        ConfigResponse configByID = configService.getById(id);
        return ResponseEntity.ok(configByID);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfigResponse> update(@PathVariable Long id, @RequestBody ConfigRequest configRequest) {
        ConfigResponse updatedConfig = configService.update(id, configRequest);
        return ResponseEntity.ok(updatedConfig);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConfigResponse> delete(@PathVariable Long id) {
        configService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
