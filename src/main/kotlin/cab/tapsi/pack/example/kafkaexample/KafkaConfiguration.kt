package cab.tapsi.pack.example.kafkaexample

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.micrometer.core.instrument.MeterRegistry
import java.time.Duration
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.support.JacksonUtils
import org.springframework.kafka.support.serializer.JsonDeserializer
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.MicrometerConsumerListener
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.MicrometerProducerListener
import reactor.kafka.sender.SenderOptions

@Configuration
class KafkaConfiguration {
  @Bean
  fun kafkaProducer(
    kafkaProperties: KafkaProperties,
    meterRegistry: MeterRegistry
  ): KafkaSender<String, WalkerFootstepUpdate> =
    KafkaSender.create(
      SenderOptions.create<String, WalkerFootstepUpdate>(kafkaProperties.buildProducerProperties())
        .producerListener(MicrometerProducerListener(meterRegistry))
    )

  @Bean
  fun kafkaConsumer(
    kafkaProperties: KafkaProperties,
    meterRegistry: MeterRegistry
  ): KafkaReceiver<String, WalkerFootstepUpdate> {
    val consumerProperties = kafkaProperties.buildConsumerProperties()
    return KafkaReceiver.create(
      ReceiverOptions.create<String, WalkerFootstepUpdate>(consumerProperties)
        .consumerListener(MicrometerConsumerListener(meterRegistry))
        .consumerProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
        .withValueDeserializer(
          JsonDeserializer(
            /* targetType = */ WalkerFootstepUpdate::class.java,
            /* objectMapper = */ JacksonUtils.enhancedObjectMapper().registerKotlinModule(),
            /* useHeadersIfPresent = */ false
          ).apply {
            addTrustedPackages("*")
          }
        )
        .commitBatchSize(500)
        .commitInterval(Duration.ofMillis(1000))
        .subscription(listOf("demo-topic"))
    )
  }
}