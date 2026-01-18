package com.mzuha.configservice.service;

import com.mzuha.configservice.exception.ResourceNotFoundException;
import com.mzuha.configservice.model.ConfigEntity;
import com.mzuha.configservice.model.ConfigRequest;
import com.mzuha.configservice.model.ConfigResponse;
import com.mzuha.configservice.repository.ConfigRepository;
import com.mzuha.configservice.util.ConfigMapper;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    private final ConfigRepository configRepository;
    private final ConfigMapper configMapper;

    public ConfigService(ConfigRepository configRepository, ConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    public ConfigResponse save(ConfigRequest configRequest) {
        ConfigEntity requestConfigEntity = configMapper.mapRequestToEntity(configRequest);
        ConfigEntity savedEntity = configRepository.save(requestConfigEntity);
        return configMapper.mapEntityToResponse(savedEntity);
    }

    public Set<ConfigResponse> getConfigsByAppAndProfile(String application, String profile) {
        Set<ConfigEntity> configEntities = configRepository.findByApplicationAndProfile(application, profile);
        return configEntities.stream().map(configMapper::mapEntityToResponse).collect(Collectors.toSet());
    }

    public ConfigResponse getById(Long id) {
        return configRepository.findById(id)
                .map(configMapper::mapEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Config not found for given id"));
    }

    @Transactional
    public ConfigResponse update(Long id, ConfigRequest configRequest) {
        Optional<ConfigEntity> configEntityById = configRepository.findById(id);
        return configEntityById.map(configEntity -> {
            configEntity.setValue(configRequest.value());
            return configMapper.mapEntityToResponse(configRepository.save(configEntity));
        }).orElseThrow(() -> new ResourceNotFoundException("Config not found for given id"));
    }

    public void deleteById(Long id) {
        configRepository.deleteById(id);
    }
}
