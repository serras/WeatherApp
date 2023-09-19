package data.location

import arrow.core.raise.catch
import arrow.core.raise.nullable
import arrow.fx.coroutines.autoCloseable
import arrow.fx.coroutines.resourceScope
import com.maxmind.geoip2.DatabaseReader
import domain.location.Location
import domain.location.LocationTracker
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import java.io.InputStream
import java.net.InetAddress

class LocationTrackerImpl : LocationTracker, AutoCloseable {
    private val dbResource: InputStream = ClassLoader.getSystemResourceAsStream("geoip/GeoLite2-City.mmdb")!!
    private val db: DatabaseReader = DatabaseReader.Builder(dbResource).build()

    override fun close() {
        db.close()
        dbResource.close()
    }

    // https://www.baeldung.com/geolocation-by-ip-with-maxmind
    override suspend fun getCurrentLocation(): Location? = nullable {
        val ip = getCurrentIp().bind()
        val city = db.city(ip).bind()
        Location(city.city.name, city.location.latitude, city.location.longitude)
    }

    // https://www.baeldung.com/java-get-ip-address#find-the-public-ip-address
    private suspend fun getCurrentIp(): InetAddress? = nullable {
        resourceScope {
            val client = autoCloseable { HttpClient { } }
            val response =
                catch({ client.get("https://checkip.amazonaws.com/") }) { raise(null) }
            ensure(response.status.isSuccess())
            val line = ensureNotNull(response.bodyAsText().lines().firstOrNull())
            InetAddress.getByName(line)
        }
    }
}
