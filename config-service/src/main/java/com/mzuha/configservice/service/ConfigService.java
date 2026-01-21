package com.mzuha.configservice.service;

import com.mzuha.configservice.exception.ResourceNotFoundException;
import com.mzuha.configservice.model.ConfigEntity;
import com.mzuha.configservice.model.ConfigEvent;
import com.mzuha.configservice.model.ConfigRequest;
import com.mzuha.configservice.model.ConfigResponse;
import com.mzuha.configservice.repository.ConfigRepository;
import com.mzuha.configservice.util.ConfigMapper;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigService {

    private final String configUpdatesTopic;
    private final ConfigRepository configRepository;
    private final ConfigMapper configMapper;
    private final KafkaTemplate<String, ConfigEvent> kafkaTemplate;
    Logger LOGGER = LoggerFactory.getLogger(ConfigService.class);

    public ConfigService(
            @Value("${app.kafka.topics.config-updates}") String configUpdatesTopic,
            ConfigRepository configRepository,
            ConfigMapper configMapper,
            KafkaTemplate<String, ConfigEvent> kafkaTemplate
    ) {
        this.configUpdatesTopic = configUpdatesTopic;
        this.configRepository = configRepository;
        this.configMapper = configMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public ConfigResponse save(ConfigRequest configRequest) {
        ConfigEntity requestConfigEntity = configMapper.mapRequestToEntity(configRequest);
        ConfigEntity savedEntity = configRepository.save(requestConfigEntity);

        return configMapper.mapEntityToResponse(savedEntity);
    }

    @Transactional(readOnly = true)
    public Set<ConfigResponse> getConfigsByAppAndProfile(String application, String profile) {
        Set<ConfigEntity> configEntities = configRepository.findByApplicationAndProfile(application, profile);
        return configEntities.stream().map(configMapper::mapEntityToResponse).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public ConfigResponse getById(Long id) {
        return configRepository.findById(id)
                .map(configMapper::mapEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Config not found for given id"));
    }

    @Transactional
    public ConfigResponse update(Long id, ConfigRequest configRequest) {
        ConfigEntity configEntity = configRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Config not found for id: " + id));

        configEntity.setKey(configRequest.key());
        configEntity.setValue(configRequest.value());

        ConfigEntity updatedEntity = configRepository.save(configEntity);
        ConfigResponse configResponse = configMapper.mapEntityToResponse(updatedEntity);

        notifyUpdate(configResponse);

        return configResponse;
    }

    private void notifyUpdate(ConfigResponse configResponse) {
        ConfigEvent configEvent = new ConfigEvent(
                configResponse.application(),
                configResponse.profile(),
                configResponse.key(),
                configResponse.value(),
                configResponse.updatedAt()
        );
        kafkaTemplate.send(configUpdatesTopic, configEvent)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        LOGGER.info("Update config sent successfully");
                    } else {
                        LOGGER.error("Failed to send update config after retries", ex);
                    }
                });

    }

    @Transactional
    public void deleteById(Long id) {
        configRepository.deleteById(id);
    }
}
