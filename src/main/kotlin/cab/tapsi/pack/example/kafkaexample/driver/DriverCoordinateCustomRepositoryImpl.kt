package cab.tapsi.pack.example.kafkaexample.driver

import java.util.Date
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class DriverCoordinateCustomRepositoryImpl(
  val reactiveMongoTemplate: ReactiveMongoTemplate
) : DriverCoordinateCustomRepository {
  override fun upsertAll(entities: List<DriverCoordinate>): Flux<DriverCoordinate> =
    Flux.defer {
      val bulkOps = reactiveMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, DriverCoordinate::class.java)
      entities.forEach { entity ->
        val update = Update().set(DriverCoordinate::coordinate.name, entity.coordinate)
          .currentDate(DriverCoordinate::updatedDate.name)
          .setOnInsert(DriverCoordinate::createdDate.name, Date())
        val query = Query(Criteria.where(DriverCoordinate::driverId.name).`is`(entity.driverId))
        bulkOps.upsert(query, update)
      }
      bulkOps.execute().thenMany(Flux.fromIterable(entities))
    }
}