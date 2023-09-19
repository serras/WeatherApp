package domain.weather

enum class WeatherIcon {
    SUNNY,
    CLOUDY,
    VERY_CLOUDY,
    RAINSHOWER,
    SNOWYRAINY,
    RAINY,
    SNOWY,
    HEAVYSNOW,
    THUNDER,
    RAINYTHUNDER;

    val iconName: String = name.lowercase()
}

enum class WeatherType(
    val weatherDesc: String,
    val icon: WeatherIcon
) {
    ClearSky("Clear sky", WeatherIcon.SUNNY),
    MainlyClear("Mainly clear", WeatherIcon.CLOUDY),
    PartlyCloudy("Partly cloudy", WeatherIcon.CLOUDY),
    Overcast("Overcast", WeatherIcon.CLOUDY),
    Foggy("Foggy", WeatherIcon.VERY_CLOUDY),
    DepositingRimeFog("Depositing rime fog", WeatherIcon.VERY_CLOUDY),
    LightDrizzle("Light drizzle", WeatherIcon.RAINSHOWER),
    ModerateDrizzle("Moderate drizzle", WeatherIcon.RAINSHOWER),
    DenseDrizzle("Dense drizzle", WeatherIcon.RAINSHOWER),
    LightFreezingDrizzle("Slight freezing drizzle", WeatherIcon.SNOWYRAINY),
    DenseFreezingDrizzle("Dense freezing drizzle", WeatherIcon.SNOWYRAINY),
    SlightRain("Slight rain", WeatherIcon.RAINY),
    ModerateRain("Moderate rain", WeatherIcon.RAINY),
    HeavyRain("Heavy rain", WeatherIcon.RAINY),
    HeavyFreezingRain("Heavy freezing rain", WeatherIcon.SNOWYRAINY),
    SlightSnowFall("Slight snow fall", WeatherIcon.SNOWY),
    ModerateSnowFall("Moderate snow fall", WeatherIcon.HEAVYSNOW),
    HeavySnowFall("Heavy snow fall", WeatherIcon.HEAVYSNOW),
    SnowGrains("Snow grains", WeatherIcon.HEAVYSNOW),
    SlightRainShowers("Slight rain showers", WeatherIcon.RAINSHOWER),
    ModerateRainShowers("Moderate rain showers", WeatherIcon.RAINSHOWER),
    ViolentRainShowers("Violent rain showers", WeatherIcon.RAINSHOWER),
    SlightSnowShowers("Light snow showers", WeatherIcon.SNOWY),
    HeavySnowShowers("Heavy snow showers", WeatherIcon.SNOWY),
    ModerateThunderstorm("Moderate thunderstorm", WeatherIcon.THUNDER),
    SlightHailThunderstorm("Thunderstorm with slight hail", WeatherIcon.RAINYTHUNDER),
    HeavyHailThunderstorm("Thunderstorm with heavy hail", WeatherIcon.RAINYTHUNDER);

    @Suppress("CyclomaticComplexMethod", "MagicNumber")
    companion object {
        fun fromWMO(code: Int): WeatherType {
            return when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
        }
    }
}
