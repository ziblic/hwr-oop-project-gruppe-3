package hwr.oop.tnp

enum class Type {
    Normal,
    Water,
    Fire,
    Plant,
    Spirit;

    val effectiveAgainst: Type?
        get() = when (this) {
            Water -> Fire
            Fire -> Plant
            Plant -> Water
            else -> null
        }

    val lessEffectiveAgainst: Type?
        get() = when (this) {
            Water -> Plant
            Fire -> Water
            Plant -> Fire
            else -> null
        }
    val noEffectAgainst: Type?
        get() = when (this) {
            Spirit -> Normal
            Normal -> Spirit
            else -> null
        }
}
