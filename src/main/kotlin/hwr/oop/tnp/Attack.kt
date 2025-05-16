package hwr.oop.tnp

enum class Attack(val type: Type, val damage: Int, val hitQuote: Double) {
    // Normal Type
    PUNCH(Type.Normal, 15, 0.1),
    DRUM(Type.Normal, 25, 0.2),
    NORMAL_SLAM(Type.Normal, 35, 0.3),
    GROUND_HAMMER(Type.Normal, 45, 0.2),

    // Fire Type
    FLAME_WREATH(Type.Fire, 20, 0.4),
    SPARK_TAIL(Type.Fire, 10, 0.2),
    LAVA_FLOOD(Type.Fire, 40, 0.3),
    FIRE_VOW(Type.Fire, 30, 0.1),

    // Water Type
    SPLASH(Type.Water, 20, 0.7),
    WATERFALL(Type.Water, 35, 0.2),
    DEEP_SEA_GRIP(Type.Water, 25, 0.1),
    TSUNAMI(Type.Water, 50, 0.4),

    // Plant Type
    LEAF_GUN(Type.Plant, 20, 0.2),
    PREDATOR_LEAF(Type.Plant, 30, 0.1),
    ROOT_SHOT(Type.Plant, 25, 0.5),
    FOLIAGE_STORM(Type.Plant, 45, 0.2),

    // Spirit Type
    SPOOKY_BALL(Type.Spirit, 20, 0.1),
    SHADOW_CLAW(Type.Spirit, 30, 0.8),
    SPIRIT_WAVE(Type.Spirit, 25, 0.9),
    NIGHT_SCREAM(Type.Spirit, 40, 0.4);

    fun calcMultiplierHitQuote(critChance: Double, random: Double = Math.random()): Double {
        require(critChance in 0.0..1.0) { "critChance must be between 0.0 and 1.0" }
        if (random < critChance) return 1.5 else return 1.0
    }
}
