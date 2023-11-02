package pray;

import pray.internal.ShadowLength;

/**
 * pray.Madhab for determining how Asr is calculated
 */
public enum Madhab {
    /**
     * Shafi pray.Madhab
     */
    SHAFI,

    /**
     * Hanafi pray.Madhab
     */
    HANAFI;

    ShadowLength getShadowLength() {
        switch (this) {
            case SHAFI -> {
                return ShadowLength.SINGLE;
            }
            case HANAFI -> {
                return ShadowLength.DOUBLE;
            }
            default -> {
                throw new IllegalArgumentException("Invalid pray.Madhab");
            }
        }
    }
}
