package hwr.oop.tnp.core

enum class Attack(
  val primitiveType: PrimitiveType,
  val damage: Int,
  val critChance: Double,
  val category: AttackCategory,
) {
  // NORMAL Type
  PUNCH(PrimitiveType.NORMAL, 15, 0.1, AttackCategory.PHYSICAL),
  DRUM(PrimitiveType.NORMAL, 25, 0.2, AttackCategory.PHYSICAL),
  NORMAL_SLAM(PrimitiveType.NORMAL, 35, 0.3, AttackCategory.SPECIAL),
  GROUND_HAMMER(PrimitiveType.NORMAL, 45, 0.2, AttackCategory.SPECIAL),

  // FIRE Type
  FLAME_WREATH(PrimitiveType.FIRE, 20, 0.4, AttackCategory.PHYSICAL),
  SPARK_TAIL(PrimitiveType.FIRE, 10, 0.2, AttackCategory.PHYSICAL),
  LAVA_FLOOD(PrimitiveType.FIRE, 40, 0.3, AttackCategory.SPECIAL),
  FIRE_VOW(PrimitiveType.FIRE, 30, 0.1, AttackCategory.SPECIAL),

  // WATER Type
  SPLASH(PrimitiveType.WATER, 20, 0.7, AttackCategory.PHYSICAL),
  WATERFALL(PrimitiveType.WATER, 35, 0.2, AttackCategory.PHYSICAL),
  DEEP_SEA_GRIP(PrimitiveType.WATER, 25, 0.1, AttackCategory.SPECIAL),
  TSUNAMI(PrimitiveType.WATER, 50, 0.4, AttackCategory.SPECIAL),

  // PLANT Type
  LEAF_GUN(PrimitiveType.PLANT, 20, 0.2, AttackCategory.PHYSICAL),
  PREDATOR_LEAF(PrimitiveType.PLANT, 30, 0.1, AttackCategory.PHYSICAL),
  ROOT_SHOT(PrimitiveType.PLANT, 25, 0.5, AttackCategory.SPECIAL),
  FOLIAGE_STORM(PrimitiveType.PLANT, 45, 0.2, AttackCategory.SPECIAL),

  // SPIRIT Type
  SPOOKY_BALL(PrimitiveType.SPIRIT, 20, 0.1, AttackCategory.PHYSICAL),
  SHADOW_CLAW(PrimitiveType.SPIRIT, 30, 0.8, AttackCategory.PHYSICAL),
  SPIRIT_WAVE(PrimitiveType.SPIRIT, 25, 0.9, AttackCategory.SPECIAL),
  NIGHT_SCREAM(PrimitiveType.SPIRIT, 40, 0.4, AttackCategory.SPECIAL),
  ;

  fun calculateDamageAgainst(
    attacker: Monster,
    defender: Monster,
    damageStrategy: DamageStrategy,
  ): Int =
    damageStrategy.calculateDamage(
      this,
      attacker,
      defender,
    )
}
