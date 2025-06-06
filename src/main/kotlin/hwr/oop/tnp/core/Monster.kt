package hwr.oop.tnp.core

import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
class Monster(
    val name: String,
    val stats: BattleStats,
    val primitiveType: PrimitiveType,
    val attacks: List<Attack>,
) {
    fun attack(attackUsed: Attack, otherMonster: Monster) {
        if(!attacks.contains(attackUsed)){
            throw MonsterDoesNotHaveAttackException("The used attack is not part of the attacks of the monster")
        }

        val damageAmount = attackUsed.calculateDamageAgainst(otherMonster)
        otherMonster.takeDamage(damageAmount)

    }
    class MonsterDoesNotHaveAttackException(message: String) : Exception(message)

    fun isKO(): Boolean {
        return stats.isKO()
    }

    private fun takeDamage(amountOfDamage: Int) {
        stats.takeDamage(amountOfDamage)
    }

    fun isFasterThan(otherMonster: Monster): Boolean {
        return stats.speed > otherMonster.stats.speed
    }
}
