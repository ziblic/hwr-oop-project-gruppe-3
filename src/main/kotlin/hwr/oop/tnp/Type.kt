package hwr.oop.tnp

enum class Type {
    Normal,
    Water,
    Fire,
    Plant,
    Spirit;

    val effectiveAgainst: Type?
        get() = when (this) {
            Water  -> Fire
            Fire   -> Plant
            Plant  -> Water
            Spirit -> Spirit
            else   -> null
        }

    val effectlessAgainst: Type?
        get() = when (this) {
            Water  -> Plant
            Fire   -> Water
            Plant  -> Fire
            Spirit -> Spirit
            else   -> null
        }
}
