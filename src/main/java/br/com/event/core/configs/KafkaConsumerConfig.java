package br.com.event.core.configs;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  @Value("${kafka.consumer.bootstrap-servers}")
  private String boostrapServers;

  @Value("${kafka.consumer.key-deserializer}")
  private String keyDeserializer;

  @Value("${kafka.consumer.value-deserializer}")
  private String valueDeserializer;

  @Value("${kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public DefaultKafkaConsumerFactory<String, String> defaultKafkaConsumerFactory() {
    Map<String, Object> properties = new HashMap<>();

    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    return new DefaultKafkaConsumerFactory<>(properties);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> singleKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(defaultKafkaConsumerFactory());

    return factory;
  }

}
