package com.mzuha.configclient.kafka;

import com.mzuha.configclient.model.ConfigResponse;
import java.util.logging.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigUpdateListener {

    private static final Logger LOGGER = Logger.getLogger(ConfigUpdateListener.class.getName());

    @KafkaListener(
            topics = "${app.kafka.topics.config-updates}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listenConfigUpdates(ConfigResponse message) {
        LOGGER.info("RECEIVED CONFIG UPDATE FOR " + message.application() + " : " + message.value());
    }
}
