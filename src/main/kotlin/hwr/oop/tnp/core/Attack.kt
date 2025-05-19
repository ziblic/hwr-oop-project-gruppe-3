package hwr.oop.tnp.core

enum class Attack(val type: Type, val damage: Int, val hitQuote: Double) {
    // NORMAL Type
    PUNCH(Type.NORMAL, 15, 0.1),
    DRUM(Type.NORMAL, 25, 0.2),
    NORMAL_SLAM(Type.NORMAL, 35, 0.3),
    GROUND_HAMMER(Type.NORMAL, 45, 0.2),

    // FIRE Type
    FLAME_WREATH(Type.FIRE, 20, 0.4),
    SPARK_TAIL(Type.FIRE, 10, 0.2),
    LAVA_FLOOD(Type.FIRE, 40, 0.3),
    FIRE_VOW(Type.FIRE, 30, 0.1),

    // WATER Type
    SPLASH(Type.WATER, 20, 0.7),
    WATERFALL(Type.WATER, 35, 0.2),
    DEEP_SEA_GRIP(Type.WATER, 25, 0.1),
    TSUNAMI(Type.WATER, 50, 0.4),

    // PLANT Type
    LEAF_GUN(Type.PLANT, 20, 0.2),
    PREDATOR_LEAF(Type.PLANT, 30, 0.1),
    ROOT_SHOT(Type.PLANT, 25, 0.5),
    FOLIAGE_STORM(Type.PLANT, 45, 0.2),

    // SPIRIT Type
    SPOOKY_BALL(Type.SPIRIT, 20, 0.1),
    SHADOW_CLAW(Type.SPIRIT, 30, 0.8),
    SPIRIT_WAVE(Type.SPIRIT, 25, 0.9),
    NIGHT_SCREAM(Type.SPIRIT, 40, 0.4);

    public fun calcMultiplierHitQuote(
        critChance: Double,
        random: Double = Math.random()
    ): Double {
        require(critChance in 0.0..1.0) { "critChance must be between 0.0 and 1.0" }
        if (random < critChance) return 1.5 else return 1.0
    }
}
