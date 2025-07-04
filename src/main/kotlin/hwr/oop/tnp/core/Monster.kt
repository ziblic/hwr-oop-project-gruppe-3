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
    if (!attacks.contains(attackUsed)) {
      throw MonsterDoesNotHaveAttackException(
        "The used attack is not part of the attacks of the monster",
      )
    }

    val damageAmount = damageStrategy.calculateDamage(attackUsed, this, otherMonster)
    otherMonster.takeDamage(damageAmount)
  }

  class MonsterDoesNotHaveAttackException(
    message: String,
  ) : Exception(message)

  fun isKO(): Boolean = stats.isKO()

  private fun takeDamage(amountOfDamage: Int) {
    stats.takeDamage(amountOfDamage)
  }

  fun isFasterThan(otherMonster: Monster): Boolean = stats.speed > otherMonster.stats.speed
}
