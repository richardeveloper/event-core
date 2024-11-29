package br.com.event.core.configs;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {

  @Value("${kafka.producer.bootstrap-servers}")
  private String boostrapServers;

  @Value("${kafka.producer.key-deserializer}")
  private String keySerializer;

  @Value("${kafka.producer.value-deserializer}")
  private String valueSerializer;

  @Bean
  public DefaultKafkaProducerFactory<String, String> defaultKafkaProducerFactory() {
    Map<String, Object> properties = new HashMap<>();

    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(defaultKafkaProducerFactory());
  }

}
