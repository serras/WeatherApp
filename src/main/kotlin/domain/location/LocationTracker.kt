package domain.location

data class Location(val name: String?, val latitude: Double, val longitude: Double)

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}
