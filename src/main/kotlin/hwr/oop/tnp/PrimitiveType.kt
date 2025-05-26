package hwr.oop.tnp

enum class PrimitiveType {
    NORMAL,
    WATER,
    FIRE,
    PLANT,
    SPIRIT;

    val effectiveAgainst: PrimitiveType?
        get() =
            when (this) {
                WATER -> FIRE
                FIRE -> PLANT
                PLANT -> WATER
                else -> null
            }

    val lessEffectiveAgainst: PrimitiveType?
        get() =
            when (this) {
                WATER -> PLANT
                FIRE -> WATER
                PLANT -> FIRE
                else -> null
            }

    val noEffectAgainst: PrimitiveType?
        get() =
            when (this) {
                SPIRIT -> NORMAL
                NORMAL -> SPIRIT
                else -> null
            }
    fun getEffectivenessAgainst(defender: PrimitiveType): Double {
        return when (defender) {
            this.effectiveAgainst -> 2.0
            this.lessEffectiveAgainst -> 0.5
            this.noEffectAgainst -> 0.0
            else -> 1.0
        }
    }
}
