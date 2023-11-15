package cab.tapsi.pack.example.kafkaexample.driver

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverCoordinateRepository : ReactiveMongoRepository<DriverCoordinate, String>,
  DriverCoordinateCustomRepository