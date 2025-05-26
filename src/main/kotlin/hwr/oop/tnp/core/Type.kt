package hwr.oop.tnp.core

enum class Type {
    NORMAL,
    WATER,
    FIRE,
    PLANT,
    SPIRIT;

    fun calculateDamangeMultiplier(monster: Monster): Double {
        return when (monster.type) {
            effectiveAgainst -> 2.0
            lessEffectiveAgainst -> 0.5
            noEffectAgainst -> 0.0
            else -> 1.0
        }
    }

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
