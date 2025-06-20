package hwr.oop.tnp.core

enum class DamageStrategy {
  DETERMINISTIC,
  RANDOM,
  ;

  fun calculateDamage(
    attack: Attack,
    attacker: Monster,
    defender: Monster,
  ): Int {
    val multiplier = attack.primitiveType.calculateDamangeMultiplier(defender)

    val attackStat =
      when (attack.category) {
        AttackCategory.PHYSICAL -> attacker.stats.attack
        AttackCategory.SPECIAL -> attacker.stats.specialAttack
      }

    val defenseStat =
      when (attack.category) {
        AttackCategory.PHYSICAL -> defender.stats.defense
        AttackCategory.SPECIAL -> defender.stats.specialDefense
      }

    val baseDamage = ((attackStat * attack.damage) / (defenseStat + 1)).coerceAtLeast(1)

    return when (this) {
      DETERMINISTIC -> (baseDamage * multiplier).toInt()
      RANDOM -> {
        val crit = if (Math.random() < attack.critChance) 1.5 else 1.0
        (baseDamage * multiplier * crit).toInt()
      }
    }
  }
}
