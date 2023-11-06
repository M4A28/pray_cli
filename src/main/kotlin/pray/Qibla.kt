package pray

import pray.internal.Coordinates
import pray.internal.QiblaUtil

class Qibla(coordinates: Coordinates?) {
    val direction: Double

    init {
        direction = QiblaUtil.calculateQiblaDirection(coordinates!!)
    }

    companion object {
        private val MAKKAH = Coordinates(21.4225241, 39.8261818)
    }
}
