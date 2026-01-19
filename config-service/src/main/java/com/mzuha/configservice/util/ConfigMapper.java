package com.mzuha.configservice.util;

import com.mzuha.configservice.model.ConfigEntity;
import com.mzuha.configservice.model.ConfigRequest;
import com.mzuha.configservice.model.ConfigResponse;
import org.springframework.stereotype.Component;

@Component
public class ConfigMapper {
    public ConfigResponse mapEntityToResponse(ConfigEntity configEntity) {
        return new ConfigResponse(
                configEntity.getId(),
                configEntity.getApplication(),
                configEntity.getProfile(),
                configEntity.getValue(),
                configEntity.getCreatedAt(),
                configEntity.getUpdatedAt()
        );
    }

    public ConfigEntity mapRequestToEntity(ConfigRequest configRequest) {
        return new ConfigEntity(
                null,
                configRequest.application(),
                configRequest.profile(),
                configRequest.key(),
                configRequest.value(),
                null,
                null
        );
    }
}
