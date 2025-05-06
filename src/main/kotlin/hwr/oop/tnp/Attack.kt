package hwr.oop.tnp
enum class Attack(val type: Type, val damage: Int, val hitQuote: Double) {
    SCHLAG(Type.Normal, 15, 0.1),
    TROMMEL(Type.Normal, 25, 0.2),
    NORMALRAMME(Type.Normal, 35, 0.3),
    BODENHAMMER(Type.Normal, 45, 0.2),

    FLAMMKRANZ(Type.Fire, 20, 0.4),
    FUNKENSCHNUPPE(Type.Fire, 10, 0.2),
    LAVAFLUT(Type.Fire, 40, 0.3),
    FEUERSCHWUR(Type.Fire, 30, 0.1),

    SPRITZER(Type.Water, 20, 0.7),
    WASSERFALL(Type.Water, 35, 0.2),
    TIEFSEEGRIFF(Type.Water, 25, 0.1),
    TSUNAMI(Type.Water, 50, 0.4),

    BLATTPISTOLE(Type.Plant, 20, 0.2),
    RAUBBLATT(Type.Plant, 30, 0.1),
    WURZELSCHUSS(Type.Plant, 25, 0.5),
    LAUBSTURM(Type.Plant, 45, 0.2),

    SPUKBALL(Type.Spirit, 20, 0.1),
    SCHATTENKLAUE(Type.Spirit, 30, 0.8),
    GEISTWELL(Type.Spirit, 25, 0.9),
    NACHTSCHREI(Type.Spirit, 40, 0.4),

}

fun calcMultiplierHitQuote(critChance: Double): Double {
    require(critChance in 0.0..1.0) { "critChance must be between 0.0 and 1.0" }

    val isCriticalHit = Math.random() < critChance
    val multiplier = if (isCriticalHit) 1.5 else 1.0

    return multiplier
}


fun getAttacks(): List<Attack>{
    return Attack.entries
}
