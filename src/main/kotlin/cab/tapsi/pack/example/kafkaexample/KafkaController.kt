package cab.tapsi.pack.example.kafkaexample

import cab.tapsi.pack.example.kafkaexample.driver.Coordinate
import cab.tapsi.pack.example.kafkaexample.driver.DriverCoordinateRepository
import java.time.Duration
import java.time.Instant
import java.util.Date
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord

@RestController
@RequestMapping("/kafka")
class KafkaController(
  private val kafkaSender: KafkaSender<String, WalkerFootstepUpdate>,
  private val kafkaReceiver: KafkaReceiver<String, WalkerFootstepUpdate>,
  private val driverCoordinateRepository: DriverCoordinateRepository
) {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @GetMapping("/produce")
  fun produce(): Mono<String> {
    Flux.interval(Duration.ofSeconds(1))
      .flatMap {
        return@flatMap Flux.range(0, 5000)
          .map {
            val value = WalkerFootstepUpdate.DUMMY.copy(
              walkerId = it.toLong() % 100,
              denoisedLocation = Location.DUMMY.copy(
                latitude = Location.DUMMY.latitude + it * 0.0001,
                longitude = Location.DUMMY.longitude + it * 0.0001
              )
            )
            val producerRecord = ProducerRecord("demo-topic", System.currentTimeMillis().toString(), value)
            return@map SenderRecord.create(producerRecord, null)
          }
          .transform { kafkaSender.send(it) }
          .collectList()
          .map { "Produced With success $it" }
      }.subscribe({
      }, {
        logger.error("Error while producing $it")
      })
    return Mono.just("Start to produce")
  }

  @GetMapping("/consume")
  fun consume(): Mono<String> {
    kafkaReceiver.receiveAutoAck(100)
      .concatMap { it }
      .windowTimeout(500, Duration.ofSeconds(1))
      .flatMap { flux ->
        flux.map {
          it.value().toDriverCoordinate()
        }.distinct { it.driverId }
          .collectList()
          .filter { it.isNotEmpty() }
          .flatMapMany { driverCoordinates -> driverCoordinateRepository.upsertAll(driverCoordinates) }
      }.subscribe({
//        logger.info("upserted with success for driver id ${it.driverId}")
      }, {
        logger.error("Error while upserting $it")
      })

    return Mono.just("Consumed with success")
  }
}