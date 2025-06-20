package hwr.oop.tnp.core

import kotlinx.serialization.Serializable

@Serializable
class Monster(
  val name: String,
  val stats: BattleStats,
  val primitiveType: PrimitiveType,
  val attacks: List<Attack>,
) {
  fun attack(
    attackUsed: Attack,
    otherMonster: Monster,
    damageStrategy: DamageStrategy,
  ) {
    require(attacks.contains(attackUsed)) {
      "The used attack is not part of the attacks of the monster"
    }

    val damageAmount = attackUsed.calculateDamageAgainst(this, otherMonster, damageStrategy)
    otherMonster.takeDamage(damageAmount)
  }

  fun isKO(): Boolean = stats.isKO()

  private fun takeDamage(amountOfDamage: Int) {
    stats.takeDamage(amountOfDamage)
  }

  fun isFasterThan(otherMonster: Monster): Boolean = stats.speed > otherMonster.stats.speed
}
