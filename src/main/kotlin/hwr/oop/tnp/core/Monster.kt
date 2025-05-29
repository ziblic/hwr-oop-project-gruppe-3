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
        require(attacks.contains(attackUsed)) {
            "The used attack is not part of the attacks of the monster"
        }

        val damageAmount = attackUsed.calculateDamageAgainst(otherMonster)
        otherMonster.takeDamage(damageAmount)
    }

    fun isKO(): Boolean {
        return stats.hp == 0
    }

    private fun takeDamage(amountOfDamage: Int) {
        stats.hp = max(stats.hp - amountOfDamage, 0)
    }

    fun isFasterThan(otherMonster: Monster): Boolean {
        return stats.speed > otherMonster.stats.speed
    }
}
