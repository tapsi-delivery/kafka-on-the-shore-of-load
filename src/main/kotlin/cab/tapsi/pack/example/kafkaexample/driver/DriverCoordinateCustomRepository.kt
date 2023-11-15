package cab.tapsi.pack.example.kafkaexample.driver

import reactor.core.publisher.Flux

interface DriverCoordinateCustomRepository {
  fun upsertAll(entities: List<DriverCoordinate>): Flux<DriverCoordinate>
}