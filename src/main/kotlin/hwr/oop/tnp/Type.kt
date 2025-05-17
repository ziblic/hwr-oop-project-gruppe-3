package hwr.oop.tnp

enum class Type {
    NORMAL,
    WATER,
    FIRE,
    PLANT,
    SPIRIT;

    val effectiveAgainst: Type?
        get() =
            when (this) {
                WATER -> FIRE
                FIRE -> PLANT
                PLANT -> WATER
                else -> null
            }

    val lessEffectiveAgainst: Type?
        get() =
            when (this) {
                WATER -> PLANT
                FIRE -> WATER
                PLANT -> FIRE
                else -> null
            }

    val noEffectAgainst: Type?
        get() =
            when (this) {
                SPIRIT -> NORMAL
                NORMAL -> SPIRIT
                else -> null
            }
}
