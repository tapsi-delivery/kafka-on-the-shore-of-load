package cab.tapsi.pack.example.kafkaexample.driver

import java.util.Date
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("drivers_coordinates")
data class DriverCoordinate(
  @Id var id: String? = null,
  @Indexed(unique = true) val driverId: String,
  val coordinate: Coordinate,
  @CreatedDate val createdDate: Date? = null,
  @LastModifiedDate val updatedDate: Date? = null
)

data class Coordinate(
  val latitude: Double,
  val longitude: Double
)
