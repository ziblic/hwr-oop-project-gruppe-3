package hwr.oop.tnp

        enum class Attack(val primitiveType: PrimitiveType, val damage: Int, val hitQuote: Double) {
    // NORMAL Type
    PUNCH(PrimitiveType.NORMAL, 15, 0.1),
    DRUM(PrimitiveType.NORMAL, 25, 0.2),
    NORMAL_SLAM(PrimitiveType.NORMAL, 35, 0.3),
    GROUND_HAMMER(PrimitiveType.NORMAL, 45, 0.2),

    // FIRE Type
    FLAME_WREATH(PrimitiveType.FIRE, 20, 0.4),
    SPARK_TAIL(PrimitiveType.FIRE, 10, 0.2),
    LAVA_FLOOD(PrimitiveType.FIRE, 40, 0.3),
    FIRE_VOW(PrimitiveType.FIRE, 30, 0.1),

    // WATER Type
    SPLASH(PrimitiveType.WATER, 20, 0.7),
    WATERFALL(PrimitiveType.WATER, 35, 0.2),
    DEEP_SEA_GRIP(PrimitiveType.WATER, 25, 0.1),
    TSUNAMI(PrimitiveType.WATER, 50, 0.4),

    // PLANT Type
    LEAF_GUN(PrimitiveType.PLANT, 20, 0.2),
    PREDATOR_LEAF(PrimitiveType.PLANT, 30, 0.1),
    ROOT_SHOT(PrimitiveType.PLANT, 25, 0.5),
    FOLIAGE_STORM(PrimitiveType.PLANT, 45, 0.2),

    // SPIRIT Type
    SPOOKY_BALL(PrimitiveType.SPIRIT, 20, 0.1),
    SHADOW_CLAW(PrimitiveType.SPIRIT, 30, 0.8),
    SPIRIT_WAVE(PrimitiveType.SPIRIT, 25, 0.9),
    NIGHT_SCREAM(PrimitiveType.SPIRIT, 40, 0.4);

    fun calcMultiplierHitQuote(
        critChance: Double,
        random: Double = Math.random()
    ): Double {
        require(critChance in 0.0..1.0) { "critChance must be between 0.0 and 1.0" }
        if (random < critChance) return 1.5 else return 1.0
    }
}
