package domain

import domain.location.Location
import domain.location.LocationTracker

data class FakeLocationTracker(
    val location: Location?
) : LocationTracker {
    override suspend fun getCurrentLocation(): Location? = location
}
