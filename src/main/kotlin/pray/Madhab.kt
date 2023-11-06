package pray

import pray.internal.ShadowLength

/**
 * pray.Madhab for determining how Asr is calculated
 */
enum class Madhab {
    /**
     * Shafi pray.Madhab
     */
    SHAFI,

    /**
     * Hanafi pray.Madhab
     */
    HANAFI;

    val shadowLength: ShadowLength
        get() = when (this) {
            SHAFI -> {
                ShadowLength.SINGLE
            }

            HANAFI -> {
                ShadowLength.DOUBLE
            }

            else -> {
                throw IllegalArgumentException("Invalid pray.Madhab")
            }
        }
}
