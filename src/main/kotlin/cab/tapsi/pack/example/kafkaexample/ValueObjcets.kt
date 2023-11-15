package cab.tapsi.pack.example.kafkaexample

import cab.tapsi.pack.example.kafkaexample.driver.Coordinate
import cab.tapsi.pack.example.kafkaexample.driver.DriverCoordinate

data class Location(
  val latitude: Double,
  val longitude: Double,
  val bearing: Double,
  val speed: Double,
  val accuracy: Double,
  val isDenoised: Boolean,
  val isJammed: Boolean,
  val locationTime: Long,
  val time: Long,
  val altitude: Double,
  val isDebounced: Boolean,
  val isMocked: Boolean,
  val clientTimestamp: Long?
) {
  companion object {
    val DUMMY = Location(
      latitude = 37.4219999,
      longitude = -122.0840575,
      bearing = 0.0,
      speed = 0.0,
      accuracy = 0.0,
      isDenoised = false,
      isJammed = false,
      locationTime = 1633046400000L,
      time = 1633046400000L,
      altitude = 0.0,
      isDebounced = false,
      isMocked = false,
      clientTimestamp = null
    )
  }
}

data class WalkerFootstepUpdate(
  val walkerId: Long,
  val walkerRole: String,
  val rawLocations: List<Location>,
  val denoisedLocation: Location,
  val sentTime: Long
) {
  fun toDriverCoordinate(): DriverCoordinate = DriverCoordinate(
    id = null,
    driverId = this.walkerId.toString(),
    coordinate = Coordinate(
      latitude = this.denoisedLocation.latitude,
      longitude = this.denoisedLocation.longitude
    )
  )

  companion object {
    val DUMMY = WalkerFootstepUpdate(
      walkerId = 1234L,
      walkerRole = "Test Role",
      rawLocations = listOf(Location.DUMMY),
      denoisedLocation = Location.DUMMY,
      sentTime = 1633046400000L
    )
  }
}